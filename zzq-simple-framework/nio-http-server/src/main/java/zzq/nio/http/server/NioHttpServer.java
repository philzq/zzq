package zzq.nio.http.server;

import zzq.nio.http.server.server.NioServer;

/**
 * CloudHttpServer
 *
 */
public class NioHttpServer {

    /**
     * 启动服务器
     */
    public static void startServer() {
        NioServer.getServerInstance().start();
    }

    public static void main(String[] args) {
        startServer();
    }

}
