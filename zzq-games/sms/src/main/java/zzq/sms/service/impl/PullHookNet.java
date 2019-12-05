package zzq.sms.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import zzq.sms.service.SMSBombing;

/**
 * 拉勾网
 */
public class PullHookNet implements SMSBombing {


    @Override
    public void sendSms(String phone) {
        String gtJudgementResult = HttpRequest.post("https://api.geetest.com/gt_judgement?pt=0&gt=66442f2f720bfc86799932d8ad2eb6c7")
                .body("Fg(9f8jm0Au4c3H9wQV8gm)hafvv4TuJGyyGpkpcuvKPSr6aEAwzwoNT5n)GtAHVHD5BOl9HZ)3pAKp61Ns2YCxh(N2MHKMTnvCCPKCIrbuIvKwiX4IA2HtJo)V6dbYS8qzG4nB)PctY2Yn7E4tWtGsYBKjFc0GAJeCw9g4Dko4z6lV(44sIeujXaw08gngrDKxRt115mAJ7QlJLKE8pi05sJo1eZ9giwH0kbNVxslV1BYrKPSPyeaglm)zZVZ6kDRQv0QmxxPP5aNR4IVspsLciI9GBboPHK8qRBDKdq3Ts5bH)tpACjVJC4JJ3ygGWAW4IgH2dJXLg9U)mZG(d2aknszEohbw21Um(lphLJ(eOJRUijL2eeZbYyxcOn4EdPVa0D4Hfi0mpcdob6QkLpX93sonyCLc9iC6iSHdkYayE3rGyACVj2L)RV7OJ85qdCmTcFr7)Wc7jcQfX9OhkNNh)G(O9oYPEady(v(0rDDj7r6)8pE24HUS7zAGpZTm49gMBuA67c87GlrHddJYXLBIzfftbbkTSk2ZtRYTom1WNEXi)40dZ1uhUVdY49Xvy71Wtte3mB2LBzDDrTZreeVHEy8bQGhnji8mOgx7p8fYMsFIUo7xGNFMVREC7Wc3aNiwxjxtdHxSLTaaNxS01jxQILu7LaAf87OaUZ0g7plT2e52nD1lrwbpNnLi)WejPh0uVNmxA94pOyzjviqI7KeW6GpEOs)pSdO2KUUkGwZWK9QonXQu6zMAUOpz(OPxdtMA2igx0fW8ET2MnRVRgtNN3n7od7OgN5XiEuuVNCmqVGVhx318i9yr1O648KGRqYmEtSoq1YEoHD4vrNnd7Sp445k3DpQonGyt0dTkoOY6)XcP4Asq1HH0g4RJzUR7IB7hEEx(AayTV3n4vPBwzKW4i8D0zJEUz62z1hvsEmIW4rXPI02wsTiddvLDXXYJsSso10Izr5tivXRpZTOCjqZkD)R6btT0hENXYOykMpeCemgajOgiN0UZQNsndu86t0gI7SDBPX4DwDKVXdN1EN8SXQryNfmmJ2MXzeWa9LOn4BL2vhAD0Rdb54dxl8pgNyPmeJplhcpg83nyKSvNl0hOvTIMCqhLcQEN8ONSrBXV)IPmRx4bvRvHXCDS398HOwdMo9gJDwx(1KuM8LULpaDVc1MwbHGLJz3CZmOzWudpUfsQnVdtP78yoAy0VeCw()QCpDFAi2t)wfbiM1pEIoieU0AQWlW8lX3nQH1LoNIT6)7hhTJQfzRN10WgzVHiOA9XKKzK5A8d(PK)ujQa3BCKGdmI2oWJqwXYKGIJ(TmAb5lumdUU)vAlfPEsPnT4dq9R3LwrfYTOTHNQ9qXD(LH8c1e)4q8cCKDL)bKX6U9fN6nX56mOpUe4QP9AnVRa9SiUw6vJBUt89j3T3gzrlZ)ySu6rxuwD3oRToTjfhIsiyrwdN3y7mI1QczcHIGr6a9(WJoSfjvMc1lQEtC9dEt7S645xH0g8wxL5sxXBzg9OUTjNQZUgk6xR1lSz(hHzs7Uv4ZDQJXWe6V7RAJVEKStjXJslpMs2c9Deq5ST7Oq5Ok3sk3AKAhmswIS8g(MUFSxoiBVJ2F0BCmZu)ekCunKyhYd2KzfHpLUsUkzDT5XT2E34P07MUDH75EayX(N8vwKD)EETkoJAu0SacUm0IhaIttzkIm5chYIOxgQ5hjsrZiG)mLctQDkcsCXqMvT1hlibuutHQ5c8MJsYacuoP3Lux8YFY5YCtBIfpq1tOtsfwlOoffJphiQ86o(D5maLw3RXWkqh3hkOq9SehghoFJpjvMJcCzXgwTdWvhW8liNr7ks9PBONuV)(slzueictEegtncsWh4st49NBhQ26e9YbysNPQED3WKzUvtEZ9fNImnPDTSIxdVx4SWePmsYGtz)BtSXSpSNLTGZoN51s56QHYVFL6MDwQT(553IPG)zjNtKgjh1zef5Xs7Pfh5BSzOhWduvAFACuj5iaXlwj8UkM0guZEE04LsBIVBC9BHhMQlBb)dxmg5LIMRJAGhnkuhHhzHhOOpEsVw1OT6(CSjXG4NFUZT5XVyP4T4pWFcNQngrFVRhiaipddkhSAwZlS5OPMQbIj7fwXt55bXQn49Q)8e3Zv8q3CKuOFx2lyp76)rXTwCyVrHBsqpsghXmX)ytPvoC5Du5tTvW2mRKQ0ddXFmkc)AYP7zPAQlzhJQW)SPNMZclDiYSY77BY6AI1U19qsDTC6B8LHe7Uy4Jt4xBD427uaZfXt07Iqa7Q38v(hzJj62RHHekKX8OVCLiIH5LcKcFKnwzpZ4ouKU6zpN2s63dEIJrAKbsXNN2odzwdT0gWu2lc5(qICfnSuj6)6XJ6A02wGgkPFDFMhsOPA2KWRcpYBOWkWnW(FQBZbKU4AeLb215P7GbtxEQygcvWWU1W3vEmCNWBLnfoGDPuCGtDcFNRsY)84dgJ42O8qBGybxrqge9fCiVj1lgMxZtkIjzAGATn2RgvvZ4lE)MBFBMeeZ7w8gS4MHS11pJGBzNtRfffXnSm61Ezcw5cBzERR8QQYWp9kYXqe2fQghioVcWJzJhAQn(oPJX3mBo8L2KkDJo4twFbqsFlm6atk5tKxg(FTCi8Loh8i(MD51qXku)ugmTTibAH9qUj33SPd9jSnAQQxhY(8sr7YtM50H3W9MebwZagPJn7jQhcyvWcpwr7a4S4(d(ib8YG0bKL5PQ3xzaW7CBdwf5R)ZdcLzt7LwP2h1irOcDu9iEUaXEyKUnRB9C1eS9wI6nu4X9EBru59UqYMMthDD6bDkMsmHznLdnETwRaatzN4DQJ9Ix6ITY6)Viy2Pad9r3hlAXXsA8w.6db9960e9d8e677c24e6a17f5b217f72c53e6b94c3a5cc91fb2d91e6377b733ff51532f17ee7177b6a682fb69f29ed9dfb18e85a6044855fe09487d196e8fa8f2207dc6d39e8839d1cbfa706d56abab53be14b6a6147ea956213f491974a2cd3b0550110ac3f1b90d2594262bd0e2290d5f6481a68559ec9dbb604f78f67cb40")
                .execute()
                .body();
        JSONObject gtJudgementJsonObject = JSONUtil.parseObj(gtJudgementResult);
        System.out.println(gtJudgementJsonObject);

        String getPhpResult = HttpRequest.post("https://api.geetest.com/get.php")
                .body("{\"is_next\":true,\"type\":\"slide3\",\"gt\":\"66442f2f720bfc86799932d8ad2eb6c7\",\"challenge\":\"ea6b447a97d363767280001cc819a514\",\"lang\":\"zh-cn\",\"https\":true,\"protocol\":\"https://\",\"product\":\"embed\",\"width\":\"100%\",\"api_server\":\"api.geetest.com\",\"static_servers\":[\"static.geetest.com\",\"dn-staticdown.qbox.me\"],\"post\":true}")
                .execute()
                .body();
        System.out.println(getPhpResult);
        /*String body = HttpRequest.get("https://passport.lagou.com/register/getPhoneVerificationCode.json?jsoncallback=jQuery1113010071522068889527_1575528654962&challenge=f0c4a3aedddb42d61c4fd68204c5c3f3&countryCode=0086&type=0&phone="+phone+"&request_form_verifyCode=&_=1575528654964")
                .execute()
                .body();*/
    }
}
