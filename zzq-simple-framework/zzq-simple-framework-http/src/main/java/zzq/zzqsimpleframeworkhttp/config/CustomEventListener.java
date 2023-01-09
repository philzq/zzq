package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * 事件监听器 -- 用户输出核心日志
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 17:02
 */
class CustomEventListener extends EventListener {

    @Override
    public void callStart(Call call) {
        LogEntity.collectLog("callStart:" + call.request());
    }

    @Override
    public void proxySelectStart(Call call, HttpUrl url) {
        LogEntity.collectLog("proxySelectStart:" + url);
    }

    @Override
    public void proxySelectEnd(Call call, HttpUrl url, List<Proxy> proxies) {
        LogEntity.collectLog("proxySelectEnd:" + proxies);
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        LogEntity.collectLog("dnsStart:" + domainName);
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        LogEntity.collectLog("dnsEnd:" + inetAddressList);
    }

    @Override
    public void connectStart(
            Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        LogEntity.collectLog("connectStart:" + inetSocketAddress + " " + proxy);
    }

    @Override
    public void secureConnectStart(Call call) {
        LogEntity.collectLog("secureConnectStart");
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        LogEntity.collectLog("secureConnectEnd");
    }

    @Override
    public void connectEnd(
            Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        LogEntity.collectLog("connectEnd:" + protocol);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy,
                              Protocol protocol, IOException ioe) {
        LogEntity.collectLog("connectFailed:" + protocol + " " + ioe);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        LogEntity.collectLog("connectionAcquired:" + connection);
    }


    @Override
    public void connectionReleased(Call call, Connection connection) {
        LogEntity.collectLog("connectionReleased");
    }


    @Override
    public void requestHeadersStart(Call call) {
        LogEntity.collectLog("requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        LogEntity.collectLog("requestHeadersEnd");
    }

    @Override
    public void requestBodyStart(Call call) {
        LogEntity.collectLog("requestBodyStart");
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        LogEntity.collectLog("requestBodyEnd: byteCount=" + byteCount);
    }

    @Override
    public void requestFailed(Call call, IOException ioe) {
        LogEntity.collectLog("requestFailed:" + ioe);
    }

    @Override
    public void responseHeadersStart(Call call) {
        LogEntity.collectLog("responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        LogEntity.collectLog("responseHeadersEnd:" + response);
    }

    @Override
    public void responseBodyStart(Call call) {
        LogEntity.collectLog("responseBodyStart");
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        LogEntity.collectLog("responseBodyEnd:byteCount=" + byteCount);
    }

    @Override
    public void responseFailed(Call call, IOException ioe) {
        LogEntity.collectLog("responseFailed:" + ioe);
    }

    @Override
    public void callEnd(Call call) {
        LogEntity.collectLog("callEnd");
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        LogEntity.collectLog("callFailed:" + ioe);
    }
}
