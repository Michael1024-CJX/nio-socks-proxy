package sun.nio.ch;

/**
 * From java.net.SocksConsts
 * Constants used by the SOCKS protocol implementation.
 *
 * @author chenjx
 */
final class SocksConsts {
    static final byte PROTO_VERS = 5;
    static final int DEFAULT_PORT = 1080;

    static final byte NO_AUTH = 0;
    static final byte USER_PASSW = 2;

    static final byte CONNECT = 1;
    static final byte RESERVED_BYTE = 0x00;
    static final byte IPV4 = 1;
    static final byte IPV6 = 4;
}
