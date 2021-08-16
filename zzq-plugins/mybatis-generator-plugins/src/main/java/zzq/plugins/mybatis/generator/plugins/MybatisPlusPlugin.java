package zzq.plugins.mybatis.generator.plugins;

import zzq.plugins.mybatis.generator.util.ContextUtils;
import zzq.plugins.mybatis.generator.util.SelectSelectiveElementGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.util.*;


public class MybatisPlusPlugin extends FalseMethodPlugin {
    private Set<String> mappers = new HashSet<String>();
    private boolean isGenColumnTypeWithJdbcType = false;
    private boolean isMixModelImports = true;

    //shellCallback use TargetProject and TargetPackage to get targetFile
    ShellCallback shellCallback = new DefaultShellCallback(false);


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String mappers = properties.getProperty("mappers");
        isGenColumnTypeWithJdbcType = StringUtility.isTrue(properties.getProperty("isGenColumnTypeWithJdbcType"));
        isMixModelImports = StringUtility.isTrue(properties.getProperty("isMixModelImports"));

        if (StringUtility.stringHasValue(mappers)) {
            this.mappers.addAll(Arrays.asList(mappers.split(",")));
        } else {
            throw new RuntimeException("Mapper插件缺少必要的mappers属性!");
        }
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.preModelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.preModelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.preModelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        return true;
    }

    private boolean preModelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        String className = introspectedTable.getBaseRecordType();
        className = className.substring(className.lastIndexOf(".") + 1);

        //========================WARN===========================
        boolean isflat = context.getDefaultModelType() == ModelType.FLAT;
        if (!isflat) {
            System.out.println("[WARN]  defaultModelType != \"flat\" was not tested, Recomendation set to flat");
        }

        //========================Body===========================
        topLevelClass.addImportedType("com.baomidou.mybatisplus.annotation.*");
        topLevelClass.addAnnotation(String.format("@TableName(\"%s\")", tableName));

        //如果你的业务实体继承自数据实体，不加这个则会出现DTYPE的问题，很难解决
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        String columnName = introspectedColumn.getActualColumnName();
        int pkCount = introspectedTable.getPrimaryKeyColumns().size();

        //代码中只设置一个主键
        Optional<IntrospectedColumn> first = introspectedTable.getPrimaryKeyColumns().stream()
                .findFirst();
        boolean isPrimaryKey = first.isPresent() && first.get().equals(introspectedColumn);

        String jdbcTypeName = introspectedColumn.getJdbcTypeName();
        topLevelClass.addImportedType("org.apache.ibatis.type.JdbcType");

        if (isPrimaryKey) {
            if (introspectedColumn.isAutoIncrement()) {
                field.addAnnotation("@TableId(value = \"" + columnName + "\", type = IdType.AUTO)");
            } else {
                field.addAnnotation("@TableId(value = \"" + columnName + "\")");
            }
        } else {
            field.addAnnotation("@TableField(value = \"" + columnName + "\",jdbcType = JdbcType." + jdbcTypeName + ")");
        }

        //========================Fix PK （视图没有主键）===========================
        if (pkCount == 0) {
            String pluginUrl = "https://github.com/mybatis/generator/blob/master/core/mybatis-generator-core/src/main/java/org/mybatis/generator/plugins/VirtualPrimaryKeyPlugin.java";
            if (isPrimaryKey) {
                System.out.println("[WARN] No Id Add One! TableName:" + tableName + " ColumnName:" + columnName + " pls set by:" + pluginUrl);
            }
        }


        return true;
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    //=============================DAO=============================
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        File xmlFile = ContextUtils.getDaoFile(context, introspectedTable);
        if (xmlFile.exists()) {
            System.out.println(xmlFile + "已经存在，就不处理了");
            return false;
        }

        interfaze.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Repository"));
        interfaze.addAnnotation("@Repository");

        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * @author MBG(MyBatisGenerator)");
        interfaze.addJavaDocLine(" */");

        //获取实体类
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        //import接口
        for (String mapper : mappers) {
            interfaze.addImportedType(new FullyQualifiedJavaType(mapper));
            interfaze.addSuperInterface(new FullyQualifiedJavaType(mapper + "<" + entityType.getShortName() + ">"));
        }
        //import实体类
        interfaze.addImportedType(entityType);

        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        //添加个<select id="selectSelective"节点
        AbstractXmlElementGenerator gen = new SelectSelectiveElementGenerator();
        gen.setContext(context);
        gen.setIntrospectedTable(introspectedTable);
        gen.addElements(document.getRootElement());
        super.sqlMapDocumentGenerated(document, introspectedTable);
        return true;
    }
}

