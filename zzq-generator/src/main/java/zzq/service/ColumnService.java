package zzq.service;

import java.util.List;
import java.util.Map;

/**
 * 字段
 */
public interface ColumnService {

    /**
     * 查询列信息
     * @param tableName
     * @return
     */
    List<Map<String, Object>> queryColumns(String tableName);
}
