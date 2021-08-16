package zzq.plugins.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.ModelType;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class JpaAnnotationPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        String className = introspectedTable.getBaseRecordType();
        className = className.substring(className.lastIndexOf(".") + 1);
        int pkCount = introspectedTable.getPrimaryKeyColumns().size();

        //========================WARN===========================
        boolean isflat = context.getDefaultModelType() == ModelType.FLAT;
        if (!isflat) {
            System.out.println("[WARN]  defaultModelType != \"flat\" was not tested, Recomendation set to flat");
        }
        if (pkCount > 1) {
            introspectedTable.getPrimaryKeyColumns()
                    .stream()
                    .filter(IntrospectedColumn::isAutoIncrement)
                    .findFirst()
                    .ifPresent(introspectedColumn ->
                            System.out.println("[WARN] Table:" + tableName
                                    + "is Composite-Id,  but has a AutoIncrementColumn:"
                                    + introspectedColumn.getActualColumnName()));

        }

        //========================Body===========================
        topLevelClass.addImportedType("javax.persistence.*");
        topLevelClass.addAnnotation("@Entity");
        topLevelClass.addAnnotation(String.format("@Table(name = \"%s\")", tableName));

        //如果你的业务实体继承自数据实体，不加这个则会出现DTYPE的问题，很难解决
        topLevelClass.addAnnotation("@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)");
        if (pkCount > 1) {
            topLevelClass.addAnnotation(String.format("@IdClass(%s.PK.class)", className));

            InnerClass innerClass = new InnerClass("PK");
            innerClass.setStatic(true);
            innerClass.setVisibility(JavaVisibility.PUBLIC);
            innerClass.addSuperInterface(new FullyQualifiedJavaType(Serializable.class.getTypeName()));
            innerClass.addAnnotation("@Data");
            innerClass.addAnnotation("@AllArgsConstructor");
            innerClass.addAnnotation("@NoArgsConstructor");
            introspectedTable.getPrimaryKeyColumns().forEach(p -> {
                Field field = new Field(p.getJavaProperty(), p.getFullyQualifiedJavaType());
                innerClass.addField(field);
            });

            topLevelClass.addInnerClass(innerClass);
        }

        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        String columnName = introspectedColumn.getActualColumnName();
        boolean isPrimaryKey = introspectedTable.getPrimaryKeyColumns().stream().anyMatch(c -> c.getActualColumnName().equals(columnName));
        String jdbcDriver = context.getJdbcConnectionConfiguration().getDriverClass();
        int pkCount = introspectedTable.getPrimaryKeyColumns().size();

        if (isPrimaryKey) {
            field.addAnnotation("@Id");
        }

        if (introspectedColumn.isAutoIncrement()) {
            if (jdbcDriver.contains("sqlserver") || jdbcDriver.contains("mysql") || jdbcDriver.contains("mariadb")) {
                field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
            } else {
                field.addAnnotation("@GeneratedValue");
            }
            if (jdbcDriver.contains("sqlserver")) {
                field.addAnnotation(String.format("@Column(name = \"%s\", insertable = false, updatable = false)", "[" + columnName + "]"));
            } else {
                field.addAnnotation(String.format("@Column(name = \"%s\", insertable = false, updatable = false)", "`"+columnName+"`"));
            }
        } else {
            if (jdbcDriver.contains("sqlserver")) {
                field.addAnnotation(String.format("@Column(name = \"%s\")", "[" + columnName + "]"));
            } else {
                field.addAnnotation(String.format("@Column(name = \"%s\")", "`"+columnName+"`"));
            }
        }

        //========================Fix PK （视图没有主键）===========================
        if (pkCount == 0) {
            String pluginUrl = "https://github.com/mybatis/generator/blob/master/core/mybatis-generator-core/src/main/java/org/mybatis/generator/plugins/VirtualPrimaryKeyPlugin.java";
            Optional<IntrospectedColumn> first = introspectedTable.getAllColumns().stream().findFirst();
            if (first.isPresent() && first.get().equals(introspectedColumn)) {
                System.out.println("[WARN] No Id Add One! TableName:" + tableName + " ColumnName:" + columnName + " pls set by:" + pluginUrl);
                field.addAnnotation("@Id //没有主键但Jpa要，选择了他,请根据这个插件设置个：" + pluginUrl);
            }
        }


        return true;
    }
}

//参考了:
//https://github.com/thinking-github/nbone/blob/master/nbone/nbone-toolbox/src/main/java/org/nbone/mybatis/generator/plugins/JpaAnnotationPlugin.java
//https://github.com/liuyuyu/mbg-plus/blob/master/src/main/java/io/github/liuyuyu/mbg/plus/JPAAnnotationPlugin.java