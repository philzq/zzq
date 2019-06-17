package zzq.service.Impl;

import zzq.config.JdbcPool;
import zzq.service.TableService;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class TableServiceImpl implements TableService {

    @Override
    public Map<String, Object> queryTable(String tableName) {
        JdbcPool jdbcPool=new JdbcPool();
        try {
            ResultSet rs=jdbcPool.excuteQuery("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables\n" +
                    "where table_schema = (select database()) and table_name = ?",tableName);
            List<Map<String, Object>> result=jdbcPool.getResult(rs);
            if(result.size() > 0){
                return result.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jdbcPool.freeConnection();
        }
        return null;
    }

    @Override
    public List<Map<String,Object>> queryTables() {
        JdbcPool jdbcPool=new JdbcPool();
        List<Map<String, Object>> result = null;
        try {
            ResultSet rs=jdbcPool.excuteQuery("select table_name tableName from information_schema.tables " +
                    "where table_schema = (select database()) and Table_type = 'BASE TABLE'");
            result=jdbcPool.getResult(rs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jdbcPool.freeConnection();
        }
        return result;
    }
}
