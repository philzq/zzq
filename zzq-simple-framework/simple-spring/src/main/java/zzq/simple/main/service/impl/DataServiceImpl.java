package zzq.simple.main.service.impl;

import zzq.simple.main.service.DataService;
import zzq.simple.spring.framework.annotation.Service;
import zzq.simple.spring.framework.bean.Data;
import zzq.simple.spring.framework.bean.Param;

@Service
public class DataServiceImpl implements DataService {

    @Override
    public Data getData(Param param) {
        Data data = new Data(param);
        return data;
    }
}
