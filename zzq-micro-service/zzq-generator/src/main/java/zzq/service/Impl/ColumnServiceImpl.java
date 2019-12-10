package zzq.service.Impl;

import zzq.config.JdbcPool;
import zzq.service.ColumnService;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class ColumnServiceImpl implements ColumnService {

    @Override
    public List<Map<String, Object>> queryColumns(String tableName) {
        JdbcPool jdbcPool=new JdbcPool();
        List<Map<String, Object>> result = null;
        try {
            ResultSet rs=jdbcPool.excuteQuery("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns" +
                    " where table_name = ? and table_schema = (select database()) order by ordinal_position",tableName);
            result=jdbcPool.getResult(rs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jdbcPool.freeConnection();
        }
        return result;
    }
}
