package zzq.zzqsimpleframeworkhttp;

import zzq.zzqsimpleframeworkhttp.config.HttpClient;
import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;

class InterceptorDemo {
    public static void main(String[] args) {
        //这里写了一堆url，只是便于测试，最终只会用最后一个，自行注释掉其他的来测试
        Request request = new Request.Builder()
                .url("http://asdfasdfasdfasfe333333.com/") //可以测试dns问题
                //.url("https://self-signed.badssl.com/") //可以用原生的okhttpclient测试假证书问题
                //.url("http://httpstat.us/500?sleep=5000") //测试延时
                //.url("http://httpstat.us/404")
                //.url("http://httpstat.us/502")
                .url("https://cdn.sstatic.net/Sites/stackoverflow/img/favicon.ico?v=4f32ecc8f43d")
                .url("https://www.baidu.com")
                .url("http://httpstat.us/200")
                .header("Accept", "application/json")  //测试httpstat.us时需要加这个，不然获取到的body是空
                .header("Accept-Encoding", "gzip, deflate")
                .build();

        var httpClient = new HttpClient("http://httpstat.us/", 100);

        /*ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 90; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //只要是Server响应了，就会有Response，包括：400,403,404,500,502,503等
                    String response = httpClient.post("http://httpstat.us/200", new TypeReference<String>() {
                    });
                    //httpClient.getOkHttpClient().newCall(request).cancel();
                }
            });
        }*/
        /*for (int i = 0; i < 90; i++){
            //只要是Server响应了，就会有Response，包括：400,403,404,500,502,503等
            String response = httpClient.post("http://httpstat.us/404", new TypeReference<String>() {
            });
        }*/
        Map<String, String> header = new HashMap<>();
        header.put("head1", "head1");
        header.put("head2", "head2");
        header.put("head3", "head3");
        Map<String, String> params = new HashMap<>();
        params.put("param1", "param1");
        params.put("param2", "param2");
        params.put("param3", "param3");
        String get = httpClient.get("200", params, header);
        String post = httpClient.post("200", params, header);
        String postByForm = httpClient.postByForm("200", params, header);

    }
}
