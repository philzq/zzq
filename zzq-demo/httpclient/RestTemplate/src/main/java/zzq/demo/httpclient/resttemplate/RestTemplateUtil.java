package zzq.demo.httpclient.resttemplate;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestTemplateUtil {

    private static RestTemplate restTemplate = HttpClient.getRestTemplate();

    private static HttpHeaders headers = new HttpHeaders();

    {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }


    public static String doGetSsh(String url) {
        var outStreamStr = "";
        try {
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            outStreamStr = responseEntity.getBody();
            printStatus(outStreamStr, responseEntity);
        } catch (Exception e) {
            System.out.println("没有返回,报错：\n");
            e.printStackTrace();
        }
        return outStreamStr;
    }

    public static String doPostSsh(String body, String url) {
        var outStreamStr = "";
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);

            outStreamStr = responseEntity.getBody();
            printStatus(outStreamStr, responseEntity);
        } catch (Exception e) {
            System.out.println("没有返回,报错：\n");
            e.printStackTrace();
        }
        return outStreamStr;
    }

    private static void printStatus(String outStreamStr, ResponseEntity<String> responseEntity) {
        if (200 <= responseEntity.getStatusCodeValue() && responseEntity.getStatusCodeValue() < 300) {
            System.out.println("正常返回的Body:\n" + outStreamStr);
        } else {
            System.out.println("异常返回的Body:\n" + outStreamStr);
        }
    }
}
