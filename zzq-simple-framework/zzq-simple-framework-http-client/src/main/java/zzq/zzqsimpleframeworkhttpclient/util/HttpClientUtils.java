package zzq.zzqsimpleframeworkhttpclient.util;

import zzq.zzqsimpleframeworkhttp.config.HttpClient;

/**
 * 测试TestHttpClient配置
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-06 11:24
 */
public class HttpClientUtils {

    public static final HttpClient testHttpClient = new HttpClient("http://localhost:9964/", 100);


}
