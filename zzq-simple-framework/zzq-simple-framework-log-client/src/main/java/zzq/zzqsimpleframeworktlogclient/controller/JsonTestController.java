package zzq.zzqsimpleframeworktlogclient.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.LogAdvancedUtil;
import zzq.zzqsimpleframeworklog.LogUtilFactory;
import zzq.zzqsimpleframeworklog.entity.WebDigestLogEntity;
import zzq.zzqsimpleframeworktlogclient.entity.JsonTestEntity;
import zzq.zzqsimpleframeworktlogclient.entity.TestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 14:14
 */
@RestController
@RequestMapping("JsonTestController")
public class JsonTestController {

    Logger logger = LoggerFactory.getLogger(JsonTestController.class);

    LogAdvancedUtil<TestLog> testLogLogAdvancedUtil = LogUtilFactory.getBusinessLogUtil("test.log", TestLog.class);

    @PostMapping("jsonTestEntity")
    private JsonTestEntity jsonTestEntity(@RequestBody JsonTestEntity jsonTestEntity) {

        logger.info("");

        WebDigestLogEntity webDigestLogEntity = WebDigestLogEntity.builder()
                .build();
        LogUtilFactory.WEB_DIGEST.info(webDigestLogEntity);

        LogUtilFactory.SYSTEM_INFO.info("测试", "哦哦哦");

        LogUtilFactory.SYSTEM_ERROR.error("测试", "哦哦哦");

        String toJSon = JacksonUtil.toJSon(jsonTestEntity);

        System.out.println("toJSon:\n" + toJSon);

        JsonTestEntity parseJson = JacksonUtil.parseJson(toJSon, new TypeReference<JsonTestEntity>() {
        });

        TestLog testLog = TestLog.builder()
                .test("测试呵呵呵哈哈哈哈哈哈哈")
                .build();
        testLogLogAdvancedUtil.info(testLog);

        if (true) {
            try {
                throw new RuntimeException("asd");
            } catch (Exception e) {
                logger.error("asdasd");
                logger.error("测试", e);
            }
        }


        return null;
    }


}
