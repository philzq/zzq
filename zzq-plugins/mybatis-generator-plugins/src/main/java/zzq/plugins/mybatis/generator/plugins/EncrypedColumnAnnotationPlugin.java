package zzq.plugins.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EncrypedColumnAnnotationPlugin extends PluginAdapter {

    private List<String> encrypedColumns = new ArrayList<>();

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String encryptedColumns = introspectedTable.getTableConfiguration()
                .getProperty("encryptedColumns");

        if (encryptedColumns != null) {
            StringTokenizer st = new StringTokenizer(encryptedColumns, ", ", false); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String column = st.nextToken();
                encrypedColumns.add(column);
            }
        }
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }


    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String columnName = introspectedColumn.getActualColumnName();

        if (StringUtility.stringHasValue(columnName) && encrypedColumns.size() > 0 && encrypedColumns.contains(columnName)) {
            String annotaionClass = getAnnotationClassName();
            if(annotaionClass == null || annotaionClass.equals("")){
                return true;
            }

            String annotationClassValue = getAnnotationClassValue();
            if(annotationClassValue != null && !annotationClassValue.equals("")){
                annotationClassValue = "value = \"" + annotationClassValue + "\"";
            }

            String annotation = String.format("@%s(%s)",annotaionClass, annotationClassValue);
            field.addAnnotation(annotation);
        }

        return true;
    }


    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String importClass = getAnnotationClassFullName();
        if(importClass != null && !importClass.equals("")){
            topLevelClass.addImportedType(importClass);
        }

        return true;
    }


    private String getAnnotationClassFullName() {
        String annotaionClassFullName = properties.getProperty("annotaionClass");
        return annotaionClassFullName;
    }

    private String getAnnotationClassName(){
        String annotaionClassFullName = properties.getProperty("annotaionClass");
        if(annotaionClassFullName == null || annotaionClassFullName.equals("")){
            return null;
        }

        String[] parts = annotaionClassFullName.split("\\.");
        if(parts != null && parts.length > 0){
            return parts[parts.length-1];
        }
        else{
            return null;
        }
    }

    private String getAnnotationClassValue(){
        String annotaionClassValue = properties.getProperty("annotaionClassValue");
        return annotaionClassValue;
    }


}
