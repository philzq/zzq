package zzq.demo.httpclient.resttemplate;

public class BestPractice {
    public static void main(String[] args) {

        RestTemplateUtil.doGetSsh( "https://www.baidu.com/");
        RestTemplateUtil.doPostSsh(null,"https://www.baidu.com/");
    }
}
