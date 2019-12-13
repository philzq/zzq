package zzq.nio.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.nio.http.server.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpParser
 */
public class HttpParser {

    private final static Logger logger = LoggerFactory.getLogger(HttpParser.class);

    /**
     * <p>解析http请求体</p>
     *
     * @param buffers
     * @return
     */
    public static Request decodeReq(byte [] buffers) {
        Request request = new Request();
        if (buffers != null) {
            String resString = new String(buffers);
            String[] headers = resString.trim().split("\r\n");
            if (headers.length > 0) {
                String firstline = headers[0];

                // 按空格分割字符串
                // 解析 method uri 协议版本
                String mainInfo [] = firstline.split("\\s+");
                request.setMethod(mainInfo[0]);
                try {
                    request.setUri(URLDecoder.decode(mainInfo[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("error_HttpParser_URLDecode, uri = {}", mainInfo[1], e);
                }
                request.setHttpVersion(mainInfo[2]);

                // 解析header
                Map<String, String> headersMap = new HashMap<>();
                for (int i = 1; i < headers.length; i++) {
                    String entryStr = headers[i];
                    String entry [] = entryStr.trim().split(":");
                    headersMap.put(entry[0].trim(), entry[1].trim());
                }
                request.setHeaders(headersMap);

                // 解析参数
                String uri = request.getUri();
                Map<String, String> attribute = new HashMap<>();
                request.setPath(uri);
                if (StringUtils.isNotEmpty(uri)) {
                    int indexOfParam = uri.indexOf("?");
                    if (indexOfParam > 0) {
                        // 设置path
                        request.setPath(uri.substring(0, indexOfParam));
                        String queryString = uri.substring(indexOfParam + 1, uri.length());
                        String paramEntrys [] = queryString.split("&");

                        for (String paramEntry : paramEntrys) {
                            String [] entry = paramEntry.split("=");
                            if (entry.length > 0) {
                                String key = entry[0];
                                String value = entry.length > 1 ? entry[1] : "";
                                attribute.put(key, value);
                            }
                        }
                    }
                }
                request.setAttribute(attribute);
            }
        }
        return request;
    }


    /**
     * <p>返回http响应字节流</p>
     *
     * @param response
     * @return
     */
    public static byte[] encodeResHeader(Response response) {
        StringBuilder resBuild = new StringBuilder();
        resBuild.append(response.getProtocol() + " " + response.getCode() + " " + response.getMsg());
        resBuild.append("\r\n");
        Map<String, String> headers = response.getHeaders();
        headers.entrySet().forEach(entry -> {
            resBuild.append(entry.getKey());
            resBuild.append(": ");
            resBuild.append(entry.getValue());
            resBuild.append("\r\n");
        });
        resBuild.append("\r\n");
        String resString = resBuild.toString();
        byte [] bytes = null;
        try {
            bytes = resString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("error_encodeResHeader", e);
        }
        return bytes;

    }
}
