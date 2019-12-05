package zzq.sms;


import zzq.sms.service.SMSBombing;
import zzq.sms.service.impl.VerticalAndHorizontalLiterature;

/**
 * 短信轰炸
 */
public class SMSBombingApplication {

    public static void main(String[] args) {
        String phone = "18770911080";
        SMSBombing smsBombing = new VerticalAndHorizontalLiterature();
        smsBombing.sendSms(phone);
    }
}
