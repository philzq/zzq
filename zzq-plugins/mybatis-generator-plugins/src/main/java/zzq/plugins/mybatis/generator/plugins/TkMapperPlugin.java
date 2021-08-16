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
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.util.*;


public class TkMapperPlugin extends FalseMethodPlugin {
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

    //===============Field加@ColumnType(jdbcType = JdbcType.NVARCHAR)=================
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (!isGenColumnTypeWithJdbcType) {
            return true;
        }

        //加上后就跟MyBatis里的写法一样了：#{resourceID,jdbcType=BIGINT}
        if (field.getAnnotations().stream().noneMatch(p -> p.contains("ColumnType"))) {
            topLevelClass.addImportedType("org.apache.ibatis.type.JdbcType");
            topLevelClass.addImportedType("tk.mybatis.mapper.annotation.ColumnType");
            field.addAnnotation("@ColumnType(jdbcType = JdbcType." + introspectedColumn.getJdbcTypeName() + ")");
        }
        return true;
    }
}

