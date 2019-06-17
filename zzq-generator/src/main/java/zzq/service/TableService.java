package zzq.service;

import java.util.List;
import java.util.Map;

/**
 * 表
 */
public interface TableService {

    /**
     * 查詢所有的表名
     * @return
     */
    List<Map<String,Object>> queryTables();

    /**
     * 查询表信息
     * @param tableName
     * @return
     */
    Map<String, Object> queryTable(String tableName);
}
