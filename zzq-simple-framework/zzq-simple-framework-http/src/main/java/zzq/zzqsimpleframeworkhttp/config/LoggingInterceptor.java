package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * 日志拦截器
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-17 14:45
 */
class LoggingInterceptor implements Interceptor {

    private static final String MILLIS_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private LogLevel logLevel = LogLevel.BODY;

    private String toDateTimeStr(Long millis, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(millis);
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public enum LogLevel {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    private void logResponse(Response response, StringBuffer logStringBuffer) {
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
        logBuffer.append("<<<<<<<<<<<<<<<<<<<<<<<<Response<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        logBuffer.append("\n");
        switch (logLevel) {
            case NONE:
                break;
            case BASIC:
                logBasicRsp(logBuffer, response);
                break;
            case HEADERS:
                logHeadersRsp(logBuffer, response);
                break;
            case BODY:
                logHeadersRsp(logBuffer, response);
                ResponseBody peekBody;
                try {
                    peekBody = response.peekBody(1024 * 1024);
                    logBuffer.append("response body:\n" + new String(peekBody.bytes(), Charset.defaultCharset()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        logBuffer.append("\n--------------------------------------------------------------------\n");
        logStringBuffer.append(logBuffer);
    }

    private void logRequest(Request request, Connection connection, StringBuffer logStringBuffer) {
        StringBuffer log = new StringBuffer();
        log.append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
        log.append(">>>>>>>>>>>>>>>>>>>>>>>>>>request>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.append("\n");
        switch (logLevel) {
            case BODY:
                logBodyReq(log, request, connection);
                log.append("\n");
                break;
            case NONE:
                break;
            case BASIC:
                logBasicReq(log, request, connection);
                break;
            case HEADERS:
                logHeaderReq(log, request, connection);
                break;
        }
        log.append("--------------------------------------------------------------------\n");
        logStringBuffer.append(log);
    }


    private void logBasicRsp(StringBuffer sb, Response response) {
        String s = toDateTimeStr(response.sentRequestAtMillis(), MILLIS_PATTERN);
        sb.append("response protocol: ");
        sb.append(response.protocol());
        sb.append("\n");
        sb.append("response code: ").append(response.code());
        sb.append("\n");
        sb.append("response message: ").append(response.message());
        sb.append("\n");
        sb.append("response request Url: ").append(decodeUrlString(response.request().url().toString()));
        sb.append("\n");
        sb.append("response sentRequestTime:").append(s);
        sb.append("\n");

    }

    private void logHeadersRsp(StringBuffer sb, Response response) {
        logBasicRsp(sb, response);
        Headers headers = response.headers();
        for (int i = 0; i < headers.size(); i++) {
            sb.append("response Header:").append(headers.name(i)).append(" = ").append(headers.value(i));
            sb.append("\n");
        }
    }


    private String decodeUrlString(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void logBodyReq(StringBuffer sb, Request request, Connection connection) {
        logHeaderReq(sb, request, connection);
        sb.append("RequestBody: ").append(Objects.requireNonNull(bodyToString(request)));
    }

    private void logHeaderReq(StringBuffer sb, Request request, Connection connection) {
        logBasicReq(sb, request, connection);
        Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            String headersStr = "request Header: " + name + "=" + value + "\n";
            sb.append(headersStr);
        }
    }

    private String bodyToString(Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            RequestBody body = copy.body();
            if (body != null) {
                body.writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "error";
        }
    }

    private void logBasicReq(StringBuffer sb, Request request, Connection connection) {
        sb.append("method: ");
        sb.append(request.method());
        sb.append("\n");
        sb.append("url: ");
        sb.append(decodeUrlString(request.url().toString()));
        sb.append("\n");
        sb.append("protocol:  ");
        if (connection != null) {
            sb.append(connection.protocol());
        } else {
            sb.append(okhttp3.Protocol.HTTP_1_1);
        }
        sb.append("\n");
    }


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Connection connection = chain.connection();
        Request request = chain.request();
        StringBuffer logStringBuffer = request.tag(StringBuffer.class);
        if (logStringBuffer != null) {
            logRequest(request, connection, logStringBuffer);
        }
        Response proceed = chain.proceed(request);
        if (proceed.isSuccessful()) {
            if (logLevel == LogLevel.NONE) {
                return proceed;
            }
            if (logStringBuffer != null) {
                logResponse(proceed, logStringBuffer);
            }
        } else {
            String message = proceed.message();
            if (logStringBuffer != null) {
                logStringBuffer.append(String.format("%s %s", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")), message));
            }
        }
        return proceed;
    }
}
