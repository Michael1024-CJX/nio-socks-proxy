package org.socks;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * @author chenjx
 */
public class SocksAgent {
    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        String nioSocksProxyJar = System.getProperty("nioSocksProxyJar");

        if (nioSocksProxyJar != null) {
            File jarFile = new File(nioSocksProxyJar);
            if (jarFile.exists()) {
                inst.appendToBootstrapClassLoaderSearch(new JarFile(jarFile));
                System.setProperty("java.nio.channels.spi.SelectorProvider",
                        "org.socks.provider.SocksProxySelectorProvider");
            }
        }

    }
}
