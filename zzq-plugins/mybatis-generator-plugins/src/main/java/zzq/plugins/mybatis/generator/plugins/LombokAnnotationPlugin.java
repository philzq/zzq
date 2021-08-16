package zzq.plugins.mybatis.generator.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;


public class LombokAnnotationPlugin extends PluginAdapter {
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
        //        topLevelClass.addImportedType("lombok.*");
        //        topLevelClass.addAnnotation("@Data");
        //
        //        //topLevelClass.addImportedType("lombok.experimental.*");
        //        //topLevelClass.addAnnotation("@SuperBuilder");
        //
        //        topLevelClass.addAnnotation("@Builder");
        //        topLevelClass.addAnnotation("@AllArgsConstructor");
        //        topLevelClass.addAnnotation("@NoArgsConstructor");

        String[] imports = properties.getProperty("imports").split("\\|");
        for (String item : imports) {
            topLevelClass.addImportedType(item);
        }

        String[] annotations = properties.getProperty("annotations").split("\\|");
        for (String item : annotations) {
            topLevelClass.addAnnotation(item);
        }
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }
}
