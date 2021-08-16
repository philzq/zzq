package zzq.plugins.mybatis.generator.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.util.StringUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class RemarkUtils {
    private static HashMap<String, String> TABLE_COLUMN_COMMENTS = new HashMap<>();

    public static String getRemark(IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String key = introspectedTable.getFullyQualifiedTable().toString();
        if (introspectedColumn != null) {
            key += "@_@" + introspectedColumn.getActualColumnName();
        }

        String remarks;

        if (TABLE_COLUMN_COMMENTS.size() > 0) {
            // context.getJdbcConnectionConfiguration().getDriverClass().contains("sqlserver")
            //SqlServer的用新的
            remarks = TABLE_COLUMN_COMMENTS.get(key);
        } else {
            //其他DB用自带的
            if (introspectedColumn != null) {
                remarks = introspectedColumn.getRemarks();
            } else {
                remarks = introspectedTable.getRemarks();
                if (StringUtility.stringHasValue(remarks)) {
                    remarks += "\r\n@author MBG(MyBatisGenerator)";
                } else {
                    remarks = "@author MBG(MyBatisGenerator)";
                }

            }

        }
        if (!StringUtility.stringHasValue(remarks)) {
            remarks = "";
        } else {
            remarks = remarks.replaceAll("(\r\n|\r|\n)", introspectedColumn != null ? "$1     * " : "$1 * ");
        }


        return remarks;
    }

    public static void generateTableColumnRemark(Context context) {
        try {
            Connection conn = new JDBCConnectionFactory(context.getJdbcConnectionConfiguration()).getConnection();

            String sqlTables = "SELECT  A.name TableName,convert(nvarchar(1000), C.VALUE) AS Remarks FROM  sys.tables A\n" +
                    "LEFT JOIN sys.extended_properties C ON C.major_id = A.object_id AND C.minor_id=0";
            ResultSet rsTables = conn.prepareStatement(sqlTables).executeQuery();

            while (rsTables.next()) {
                String key = rsTables.getString("TableName");
                TABLE_COLUMN_COMMENTS.put(key, rsTables.getString("Remarks"));
            }

            String sqlColumns = "SELECT  A.name TableName,B.name ColumnName,convert(nvarchar(1000), C.VALUE) AS Remarks FROM  sys.tables A \n" +
                    "INNER JOIN sys.columns B ON B.object_id = A.object_id\n" +
                    "LEFT JOIN sys.extended_properties C ON C.major_id = B.object_id AND C.minor_id = B.column_id";
            ResultSet rsColumns = conn.prepareStatement(sqlColumns).executeQuery();

            while (rsColumns.next()) {
                String key = rsColumns.getString("TableName") + "@_@" + rsColumns.getString("ColumnName");
                TABLE_COLUMN_COMMENTS.put(key, rsColumns.getString("Remarks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
