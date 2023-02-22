package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.*;
import zzq.zzqsimpleframeworklog.entity.RemoteDigestLogEntity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 事件监听器 -- 用户输出核心日志
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 17:02
 */
class LoggingEventListener extends EventListener {

    private void addEventLog(Call call, String log) {
        Request request = call.request();
        RemoteDigestLogEntity remoteDigestLogEntity = request.tag(RemoteDigestLogEntity.class);
        if (remoteDigestLogEntity != null) {
            LocalDateTime nowTime = LocalDateTime.now();
            if (log.startsWith("callStart")) {
                remoteDigestLogEntity.setStartTime(nowTime);
                remoteDigestLogEntity.getRequestDetail().append("\n");
            }
            long elapseTime = Duration.between(remoteDigestLogEntity.getStartTime(), nowTime).toMillis();
            remoteDigestLogEntity.getRequestDetail().append(String.format("%s %s %s%n", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")), "【" + elapseTime + "ms】", log));
        }
    }

    @Override
    public void callStart(Call call) {
        addEventLog(call, "callStart:" + call.request().url() + "," + call.request().headers());
    }

    @Override
    public void proxySelectStart(Call call, HttpUrl url) {
        addEventLog(call, "proxySelectStart:" + url);
    }

    @Override
    public void proxySelectEnd(Call call, HttpUrl url, List<Proxy> proxies) {
        addEventLog(call, "proxySelectEnd:" + proxies);
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        addEventLog(call, "dnsStart:" + domainName);
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        addEventLog(call, "dnsEnd:" + inetAddressList);
    }

    @Override
    public void connectStart(
            Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        addEventLog(call, "connectStart:" + inetSocketAddress + " " + proxy);
    }

    @Override
    public void secureConnectStart(Call call) {
        addEventLog(call, "secureConnectStart");
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        addEventLog(call, "secureConnectEnd");
    }

    @Override
    public void connectEnd(
            Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        addEventLog(call, "connectEnd:" + protocol);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy,
                              Protocol protocol, IOException ioe) {
        addEventLog(call, "connectFailed:" + protocol + " " + ioe);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        addEventLog(call, "connectionAcquired:" + connection);
    }


    @Override
    public void connectionReleased(Call call, Connection connection) {
        addEventLog(call, "connectionReleased");
    }


    @Override
    public void requestHeadersStart(Call call) {
        addEventLog(call, "requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        addEventLog(call, "requestHeadersEnd");
    }

    @Override
    public void requestBodyStart(Call call) {
        addEventLog(call, "requestBodyStart");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        addEventLog(call, "requestBodyEnd: byteCount=" + byteCount);
    }

    @Override
    public void requestFailed(Call call, IOException ioe) {
        addEventLog(call, "requestFailed:" + ioe);
    }

    @Override
    public void responseHeadersStart(Call call) {
        addEventLog(call, "responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        addEventLog(call, "responseHeadersEnd:" + response);
    }

    @Override
    public void responseBodyStart(Call call) {
        addEventLog(call, "responseBodyStart");
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        addEventLog(call, "responseBodyEnd:byteCount=" + byteCount);
    }

    @Override
    public void responseFailed(Call call, IOException ioe) {
        addEventLog(call, "responseFailed:" + ioe);
    }

    @Override
    public void callEnd(Call call) {
        addEventLog(call, "callEnd");
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        addEventLog(call, "callFailed:" + ioe);
    }
}
