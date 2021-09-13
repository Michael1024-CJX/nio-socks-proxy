package sun.nio.ch;

/**
 * From java.net.SocksConsts
 * Constants used by the SOCKS protocol implementation.
 *
 * @author chenjx
 */
final class SocksConsts {
    static final byte PROTO_VERS4                = 4;
    static final byte PROTO_VERS         = 5;
    static final int DEFAULT_PORT               = 1080;
    static final byte RESERVED_BYTE               = 0;

    static final byte NO_AUTH            = 0;
    static final byte GSSAPI             = 1;
    static final byte USER_PASSW         = 2;
    static final byte NO_METHODS         = -1;

    static final byte CONNECT            = 1;
    static final byte BIND                       = 2;
    static final byte UDP_ASSOC          = 3;

    static final byte IPV4                       = 1;
    static final byte DOMAIN_NAME                = 3;
    static final byte IPV6                       = 4;

    static final byte REQUEST_OK         = 0;
    static final byte GENERAL_FAILURE    = 1;
    static final byte NOT_ALLOWED                = 2;
    static final byte NET_UNREACHABLE    = 3;
    static final byte HOST_UNREACHABLE   = 4;
    static final byte CONN_REFUSED               = 5;
    static final byte TTL_EXPIRED                = 6;
    static final byte CMD_NOT_SUPPORTED  = 7;
    static final byte ADDR_TYPE_NOT_SUP  = 8;
}
