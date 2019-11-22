package zzq.simple.main.service;

import zzq.simple.spring.framework.bean.Data;
import zzq.simple.spring.framework.bean.Param;

public interface DataService {

    /**
     * 通过日志id获取日志
     * @param param
     * @return
     */
    Data getData(Param param);
}
