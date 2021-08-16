package zzq.plugins.mybatis.generator.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;


public class SelectSelectiveElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        String baseRecordType = introspectedTable.getBaseRecordType();

        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectSelective"));
        answer.addAttribute(new Attribute("resultType", baseRecordType));
        answer.addAttribute(new Attribute("parameterType", baseRecordType));
        context.getCommentGenerator().addComment(answer);
        parentElement.addElement(new TextElement("<!--辅助代码，禁止转正-->"));
        parentElement.addElement(new TextElement("<!--suppress MybatisMapperXmlInspection -->"));


        fillSelectAll(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append(" where 1=1");
        answer.addElement(new TextElement(sb.toString()));
        introspectedTable.getPrimaryKeyColumns().forEach(introspectedColumn -> {
            sb.setLength(0);
            sb.append("and ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        });



        introspectedTable.getNonPrimaryKeyColumns().forEach(introspectedColumn -> {
            XmlElement isNotNullElement = new XmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null");
            if (introspectedColumn.isStringColumn()) {
                sb.append(" and ");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != ''");
            }
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            answer.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append("and ");
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            isNotNullElement.addElement(new TextElement(sb.toString()));


            if (introspectedColumn.isStringColumn()) {
                String jdbcDriver = context.getJdbcConnectionConfiguration().getDriverClass();
                if (jdbcDriver.contains("sqlserver")){
                    sb.setLength(0);
                    sb.append("and ");
                    sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                    sb.append(" like N'%' +");
                    sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                    sb.append(" + '%'");
                    isNotNullElement.addElement(new TextElement(sb.toString()));
                } else {
                    sb.setLength(0);
                    sb.append("and ");
                    sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                    sb.append(" like concat('%', ");
                    sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                    sb.append(", '%')");
                    isNotNullElement.addElement(new TextElement(sb.toString()));
                }

            }

        });

        parentElement.addElement(answer);
    }

    private void fillSelectAll(XmlElement answer) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (int i = 0; i < columns.size(); i++) {
            sb.append(MyBatis3FormattingUtilities.getSelectListPhrase(columns.get(i)));

            if (i < columns.size() - 1) {
                sb.append(", ");
                if (sb.length() > 80) {
                    sb.setLength(sb.length() - 1);
                    answer.addElement(new TextElement(sb.toString()));
                    sb.setLength(0);
                }
            } else {
                answer.addElement(new TextElement(sb.toString()));
            }

        }
    }
}

