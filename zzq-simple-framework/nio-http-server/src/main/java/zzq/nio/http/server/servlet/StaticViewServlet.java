package zzq.nio.http.server.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.nio.http.server.Request;
import zzq.nio.http.server.Response;
import zzq.nio.http.server.exception.ViewNotFoundException;
import zzq.nio.http.server.util.CloudHttpConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StaticViewServlet
 *
 */
public class StaticViewServlet implements CloudServlet {

    private final static Logger logger = LoggerFactory.getLogger(StaticViewServlet.class);

    private String staticRootPath;

    public static Pattern p = Pattern.compile("^/static/\\S+");

    @Override
    public boolean match(Request request) {
        String path = request.getPath();
        Matcher matcher = p.matcher(path);
        return matcher.matches();
    }

    @Override
    public void init(Request request, Response response) {
        staticRootPath = CloudHttpConfig.getValue("static.resource.path");
    }

    @Override
    public void doService(Request request, Response response) {
        String path = request.getPath();

        String absolutePath = Thread.currentThread().getContextClassLoader().getResource(path.substring(1)).getPath();

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(absolutePath, "r");
            FileChannel fileChannel = randomAccessFile.getChannel();
            ByteBuffer htmBuffer = ByteBuffer.allocate((int)fileChannel.size());
            fileChannel.read(htmBuffer);
            htmBuffer.flip();
            byte [] htmByte = new byte[htmBuffer.limit()];
            htmBuffer.get(htmByte);
            response.getOutPutStream().write(htmByte);
        } catch (FileNotFoundException e) {
            throw new ViewNotFoundException();
        } catch (IOException e) {
            logger.error("error_StaticViewServlet_doService 异常", e);
        }
    }
}
