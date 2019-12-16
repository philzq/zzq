package zzq.sms;


import zzq.sms.service.SMSBombing;
import zzq.sms.service.impl.SegmentFault;

/**
 * 短信轰炸
 */
public class SMSBombingApplication {

    public static void main(String[] args) {
        String phone = "";
        SMSBombing smsBombing = new SegmentFault();
        smsBombing.sendSms(phone);
    }
}
