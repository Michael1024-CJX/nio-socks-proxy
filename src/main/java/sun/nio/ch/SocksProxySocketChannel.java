package sun.nio.ch;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.SelectorProvider;

import static sun.nio.ch.SocksConsts.*;

public class SocksProxySocketChannel extends SocketChannelImpl {

    private static final int DEFAULT_ENCODER_BUFFER_SIZE = 1024;

    private InetSocketAddress externalAddress;
    private String proxyServer = null;
    private int proxyServerPort = DEFAULT_PORT;

    public SocksProxySocketChannel(SelectorProvider sp) throws IOException {
        super(sp);
        initSocks();
    }

    @Override
    public SocketAddress remoteAddress() {
        if (externalAddress != null && isSatisfy()) {
            return externalAddress;
        } else {
            return super.remoteAddress();
        }
    }

    @Override
    public boolean connect(SocketAddress sa) throws IOException {
        if (!(sa instanceof InetSocketAddress))
            throw new IllegalArgumentException("Unsupported address type");
        this.externalAddress = (InetSocketAddress) sa;

        boolean connect;
        InetSocketAddress socksProxy = getSocksProxy();

        if (socksProxy != null && isSatisfy()) {
            try {
                connect = socksConnect(socksProxy);
            } catch (Exception e) {
                System.out.println("未能连接代理服务器：" + externalAddress);
                e.printStackTrace();
                connect = super.connect(externalAddress);
            }
        } else {
            connect = super.connect(externalAddress);
        }
        return connect;
    }

    protected boolean isSatisfy() {
        // 排除本地调试时的情况
        String hostString = externalAddress.getHostString();
        return !hostString.equals("127.0.0.1");
    }

    private boolean socksConnect(InetSocketAddress socksProxy) throws IOException {
        boolean connect = super.connect(socksProxy);
        if (!connect) {
            connect = finishConnect();
            if (!connect) {
                int attempt = 0;
                while (attempt < 3) {
                    threadSleep(50);
                    connect = finishConnect();
                    attempt++;
                }
            }

            if (connect) {
                System.out.println("连接socks服务器成功");
            }else {
                System.out.println("连接socks服务器失败");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_ENCODER_BUFFER_SIZE);
        sendSockInitRequest(byteBuffer);
        sendSockCmdRequest(byteBuffer);
        return true;
    }

    private void sendSockInitRequest(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(PROTO_VERS);
        byteBuffer.put((byte) 2);
        byteBuffer.put(NO_AUTH);
        byteBuffer.put(USER_PASSW);
        byteBuffer.flip();
        write(byteBuffer);
        readSockReply(byteBuffer, 2);
    }

    private void sendSockCmdRequest(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(PROTO_VERS);
        byteBuffer.put(CONNECT);
        byteBuffer.put(RESERVED_BYTE);
        if (externalAddress.getAddress() instanceof Inet6Address) {
            byteBuffer.put(IPV6);
        } else {
            byteBuffer.put(IPV4);
        }
        byteBuffer.put(externalAddress.getAddress().getAddress());
        byteBuffer.put((byte) ((externalAddress.getPort() >> 8) & 0xff));
        byteBuffer.put((byte) ((externalAddress.getPort()) & 0xff));
        byteBuffer.flip();
        write(byteBuffer);
        readSockReply(byteBuffer, 4);

        readSockReply(byteBuffer, 4);
        readSockReply(byteBuffer, 2);
    }

    private void readSockReply(ByteBuffer byteBuffer, int expect) throws IOException {
        byteBuffer.clear();
        byteBuffer.limit(expect);
        int attempts = 0;
        while (byteBuffer.position() < expect && attempts < 3) {
            threadSleep(100);
            read(byteBuffer);
            attempts++;
        }
        byteBuffer.flip();
    }

    private void initSocks() {
        proxyServer = System.getProperty("socksProxyHost");
        if (proxyServer != null) {
            final String socksProxyPort = System.getProperty("socksProxyPort");
            if (socksProxyPort != null) {
                proxyServerPort = Integer.parseInt(socksProxyPort);
            }

        }else {
            proxyServer = System.getProperty("nioSocksProxyHost");
            final String socksProxyPort = System.getProperty("nioSocksProxyPort");
            if (socksProxyPort != null) {
                proxyServerPort = Integer.parseInt(socksProxyPort);
            }
        }

        if (proxyServer == null) {
            throw new IllegalArgumentException("未设置NIO socks 代理");
        }
    }

    private InetSocketAddress getSocksProxy() {
        if (proxyServer == null) {
            return null;
        }
        return new InetSocketAddress(proxyServer, proxyServerPort);
    }

    private void threadSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
