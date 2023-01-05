package zzq.zzqsimpleframeworkhttp;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import zzq.zzqsimpleframeworkhttp.config.HttpClient;

import java.io.IOException;

public class SyncDemo {
    public static void main(String[] args) {
        //这里写了一堆url，只是便于测试，最终只会用最后一个，自行注释掉其他的来测试
        Request request = new Request.Builder()
                .url("http://asdfasdfasdfasfe333333.com/") //可以测试dns问题
                .url("https://self-signed.badssl.com/") //可以用原生的okhttpclient测试假证书问题
                .url("http://httpstat.us/500?sleep=5000") //测试延时
                .url("http://httpstat.us/404")
                .url("http://httpstat.us/502")
                //.url("http://httpstat.us/200")
                .header("Accept", "application/json")  //测试httpstat.us时需要加这个，不然获取到的body是空
                .build();

        //OkHttp好用的地方，可以直接复用之前定义好的对象，从而减少编码量，
        //如：request里有大量设置，而新的对象我只想改下url其他不变
        var request2 = request.newBuilder().url("http://httpstat.us/200").build();

        HttpClient httpClient = new HttpClient();
        //httpClient = new OkHttpClient(); //原生client，默认会校验证书

        try {
            //只要是Server响应了，就会有Response，包括：400,403,404,500,502,503等
            Call newCall = httpClient.OkHttpClient.newCall(request);
            Response response = newCall.execute(); //同步调用
            String body = httpClient.body(response);
            if (response.isSuccessful()) { // 200<=statusCode<300
                //200响应的进这里
                System.out.println("正常返回的Body:\n" + body);
            } else {
                //400,403,404,500,502,503等响应进这里
                System.out.println("异常返回的Body:\n" + body);
            }
        } catch (IOException e) {
            //证书错、dns错、等其他错误
            System.out.println("没有返回,报错：\n");
            e.printStackTrace();
        }
    }
}
