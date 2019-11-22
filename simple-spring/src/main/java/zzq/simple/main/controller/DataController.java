package zzq.simple.main.controller;

import zzq.simple.main.service.DataService;
import zzq.simple.spring.framework.annotation.Autowired;
import zzq.simple.spring.framework.annotation.Controller;
import zzq.simple.spring.framework.annotation.RequestMapping;
import zzq.simple.spring.framework.bean.Data;
import zzq.simple.spring.framework.bean.Param;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    @RequestMapping("getData")
    public Data getData(Param param) {
        Data data = dataService.getData(param);
        return data;
    }
}
