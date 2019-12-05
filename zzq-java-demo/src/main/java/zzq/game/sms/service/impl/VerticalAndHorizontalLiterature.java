package zzq.game.sms.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import zzq.game.sms.service.SMSBombing;

/**
 * 纵横文学
 */
public class VerticalAndHorizontalLiterature implements SMSBombing {

    @Override
    public void sendSms(String phone) {
        sendLoginSms(phone);
    }

    /**
     * 纵横文学登录短信验证码
     * @param phone
     */
    private void sendLoginSms(String phone) {
        String TK = null;
        String captKey = null;

        String mainBody = HttpRequest.get("https://passport.zongheng.com/")
                .execute()
                .body();
        if(mainBody!=null){
            int beginIndex = mainBody.indexOf("var TK    = \"");
            TK = mainBody.substring(beginIndex+13,beginIndex+13+19);
        }

        String preregcheckParams = "tk="+TK+"&unam="+phone+"&t="+System.currentTimeMillis();
        String preregcheckBody = HttpRequest.post("https://passport.zongheng.com/preregcheck.do")
                .body(preregcheckParams)
                .execute()
                .body();
        JSONObject preregcheckJsonObject = JSONUtil.parseObj(preregcheckBody);
        captKey = preregcheckJsonObject.getJSONObject("data").getStr("captKey");

        String sendloginsmsParams = "phone="+phone+"&tk="+TK+"&captkey="+captKey+"&capt=";
        String body = HttpRequest.post("https://passport.zongheng.com/sendloginsms.do")
                .body(sendloginsmsParams)
                .execute().body();
    }
}
