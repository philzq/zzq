package zzq.zzqsimpleframeworkjsonclient.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworkjsonclient.entity.JsonTestEntity;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 14:14
 */
@RestController
@RequestMapping("JsonTestController")
public class JsonTestController {

    @PostMapping("jsonTestEntity")
    private JsonTestEntity jsonTestEntity(@RequestBody JsonTestEntity jsonTestEntity){

        String toJSon = JacksonUtil.toJSon(jsonTestEntity);

        System.out.println("toJSon:\n" + toJSon);

        JsonTestEntity parseJson = JacksonUtil.parseJson(toJSon, new TypeReference<JsonTestEntity>() {
        });

        return parseJson;
    }


}
