package zzq.nio.http.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.nio.http.server.HttpParser;
import zzq.nio.http.server.Request;
import zzq.nio.http.server.util.CloudHttpConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NioServer
 *
 */
public class NioServer implements Server, Runnable {
    private final static Logger logger = LoggerFactory.getLogger(NioServer.class);

    private Thread serverThread;

    private Integer port;

    private boolean running = false;

    private static volatile NioServer server;

    private CloudService cloudService;

    private ServerSocketChannel serverSocketChannel;

    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

    public static final ExecutorService requestWork = Executors.newCachedThreadPool();

    public static NioServer getServerInstance () {
        if (server == null) {
            synchronized (NioServer.class) {
                if (server == null) {
                    server = new NioServer();
                }
            }
        }
        return server;
    }

    public NioServer() {
        this.cloudService = new CloudService();
        port = Integer.valueOf(CloudHttpConfig.getValue("port", "8080"));
    }


    @Override
    public synchronized void start() {
        if (running) {
            logger.info("服务器已经启动");
            return;
        }
        serverThread = new Thread(this);
        serverThread.start();
        this.running = true;
    }

    @Override
    public void stop() {
        try {
            serverSocketChannel.close();
            serverThread.stop();
        } catch (IOException e) {
            logger.error("error_NioServer_stop", e);
        }
    }

    @Override
    public void run() {

        try {
            //打开ServerSocketChannel通道
            serverSocketChannel = ServerSocketChannel.open();
            //得到ServerSocket对象
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                SelectionKey key = null;
                while (selectedKeys.hasNext()) {
                    key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                    if (key.isWritable()) {
                        write(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("error_NioServer_run", e);
        }
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.error("error_NioServer_accept", e);
        }
    }

    private Request read(SelectionKey key) {
        Request request = null;
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            int readNum = socketChannel.read(readBuffer);
            if (readNum == -1) {
                socketChannel.close();
                key.cancel();
                return null;
            }
            readBuffer.flip();
            byte [] buffers = new byte[readBuffer.limit()];
            readBuffer.get(buffers);
            readBuffer.clear();
            request = HttpParser.decodeReq(buffers);
            requestWork.execute(new RequestWorker(request, key, cloudService));
        } catch (IOException e) {
            logger.error("error_NioServer_read", e);
        }
        return request;
    }

    private void write(SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        if(buffer == null || !buffer.hasRemaining()) {
            return;
        }
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(buffer);
        if(!buffer.hasRemaining()){
            key.interestOps(SelectionKey.OP_READ);
            buffer.clear();
        }
        socketChannel.close();
    }
}
