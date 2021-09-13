package org.socks.provider;

import sun.nio.ch.DefaultSelectorProvider;
import sun.nio.ch.SocksProxySocketChannel;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class SocksProxySelectorProvider extends SelectorProvider {

    private static SelectorProvider innerProvider =
            AccessController.doPrivileged((PrivilegedAction<SelectorProvider>) DefaultSelectorProvider::create);

    public SocksProxySelectorProvider() {
    }

    @Override
    public DatagramChannel openDatagramChannel() throws IOException {
        return innerProvider.openDatagramChannel();
    }

    @Override
    public DatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
        return innerProvider.openDatagramChannel(family);
    }

    @Override
    public Pipe openPipe() throws IOException {
        return innerProvider.openPipe();
    }

    @Override
    public AbstractSelector openSelector() throws IOException {
        return innerProvider.openSelector();
    }

    @Override
    public ServerSocketChannel openServerSocketChannel() throws IOException {
        return innerProvider.openServerSocketChannel();
    }

    @Override
    public SocketChannel openSocketChannel() throws IOException {
        return new SocksProxySocketChannel(innerProvider);
    }
}
