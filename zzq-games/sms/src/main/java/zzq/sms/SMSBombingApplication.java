package zzq.sms;


import zzq.sms.service.SMSBombing;
import zzq.sms.service.impl.SuNingYiPurchase;
import zzq.sms.service.impl.VerticalAndHorizontalLiterature;

/**
 * 短信轰炸
 */
public class SMSBombingApplication {

    public static void main(String[] args) {
        String phone = "";
        SMSBombing smsBombing = new SuNingYiPurchase();
        smsBombing.sendSms(phone);
    }
}
