package zzq.plugins.mybatis.generator.plugins;

import zzq.plugins.mybatis.generator.util.RemarkUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class FixSqlServerCommentPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        RemarkUtils.generateTableColumnRemark(context);
        return true;
    }


    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        introspectedTable.setRemarks(RemarkUtils.getRemark(introspectedTable, null));
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        introspectedColumn.setRemarks(RemarkUtils.getRemark(introspectedTable, introspectedColumn));
        return true;
    }

}
