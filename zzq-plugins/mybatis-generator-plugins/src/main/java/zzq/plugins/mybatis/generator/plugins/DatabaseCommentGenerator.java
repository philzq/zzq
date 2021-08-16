package zzq.plugins.mybatis.generator.plugins;

import zzq.plugins.mybatis.generator.util.RemarkUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

//Comment比较特殊FixSqlServerComment后这里拿不到，其他AdapterPlugin里可以拿到
public class DatabaseCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String remarks = RemarkUtils.getRemark(introspectedTable, introspectedColumn);
        boolean isSerialVersionUID = field.getName().equals("serialVersionUID");
        if (StringUtility.stringHasValue(remarks)) {
            field.getJavaDocLines().clear();
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + remarks);
            field.addJavaDocLine(" */");
        }
    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String remarks = RemarkUtils.getRemark(introspectedTable, null);
        if (StringUtility.stringHasValue(remarks)) {
            topLevelClass.getJavaDocLines().clear();
            topLevelClass.addJavaDocLine("/**");
            topLevelClass.addJavaDocLine(" * " + remarks);
            topLevelClass.addJavaDocLine(" */");
        }

    }


}
