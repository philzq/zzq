package zzq.spring.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RestController
public class SpringBootTest{

    public static void main(String[] args){
        SpringApplication.run(SpringBootTest.class,args);
    }

    @GetMapping("testSyncGet")
    public String testSyncGet() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8281/tomcat/getTestStr"))
                .build();
        String body = client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
        return body;
    }

    @GetMapping("testAsyncGet")
    public String testAsyncGet() throws Exception{
        HttpClient client = HttpClient.newBuilder()
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8281/tomcat/getTestStr"))
                .build();
        CompletableFuture<String> result = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
        System.out.println(result.get());
        return result.get();
    }


}
