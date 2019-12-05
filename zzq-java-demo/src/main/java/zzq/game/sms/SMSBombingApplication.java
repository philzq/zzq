package zzq.game.sms;

import zzq.game.sms.service.SMSBombing;
import zzq.game.sms.service.impl.VerticalAndHorizontalLiterature;

/**
 * 短信轰炸
 */
public class SMSBombingApplication {

    public static void main(String[] args) {
        String phone = "";
        SMSBombing smsBombing = new VerticalAndHorizontalLiterature();
        smsBombing.sendSms(phone);
    }
}
