package zzq.plugins.mybatis.generator.plugins;

import zzq.plugins.mybatis.generator.util.ContextUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.render.TopLevelClassRenderer;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model与XML增量更新插件
 * 增量更新插件
 */
public class IncrementalUpdatingPlugin extends FalseMethodPlugin {

    //shellCallback use TargetProject and TargetPackage to get targetFile
    ShellCallback shellCallback = new DefaultShellCallback(false);

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    //=============================Model=============================
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        try {
            String spliter4Model = "    //████████上面是自动生成的，改了会被覆盖，有需要可以添加到下面(不能用@Column,要加上@Transient)，此行不能删除████████";
            Pattern importPattern = Pattern.compile("import\\s+(\\S+)", Pattern.MULTILINE);

            File modelFile = ContextUtils.getModelFile(context, introspectedTable);
            modelFile.getParentFile().mkdirs();

            String newFileString = new TopLevelClassRenderer().render(topLevelClass);

            if (modelFile.exists()) {
                String oldFileString = ContextUtils.readAllString(modelFile.toPath());
                //imports合并
                ArrayList<String> newImports = new ArrayList<>();
                Matcher matcher2 = importPattern.matcher(newFileString);
                while (matcher2.find()) {
                    String group = matcher2.group(1);
                    newImports.add(group);
                }

                ArrayList<String> oldImports = new ArrayList<>();
                Matcher matcher = importPattern.matcher(oldFileString);
                while (matcher.find()) {
                    String group = matcher.group(1);
                    oldImports.add(group);
                }
                Collections.sort(oldImports);

                oldImports.removeAll(newImports);
                for (String item : oldImports) {
                    newFileString = newFileString.replaceAll("package .*", "$0\r\nimport " + item);
                }

                //将旧文件的分割线后的内容 贴到 新文件最后的“}”之前
                {
                    oldFileString = oldFileString.replaceAll("[\\s\\S]+█████\r\n", "");
                    newFileString = newFileString.substring(0, newFileString.lastIndexOf("}")) + spliter4Model + "\r\n" + oldFileString;
                }
            } else {
                newFileString = newFileString.substring(0, newFileString.lastIndexOf("}")) + spliter4Model + "\r\n}";
            }

            Files.write(modelFile.toPath(), newFileString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //已经是最终的了，合并后返回false，其他插件就不会再走整个方法了
        return false;
    }

    //=============================XML=============================
    //new nodes is generated,but not write on disk,we just need to filter.
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            String spliter4Xml = "    <!--████████上面是自动生成的，改了会被覆盖，有需要可以添加到下面，此行不能删除████████-->";

            File directory = shellCallback.getDirectory(sqlMap.getTargetProject(), sqlMap.getTargetPackage());
            File xmlFile = new File(directory, sqlMap.getFileName());

            String newFileString = sqlMap.getFormattedContent();

            //默认每级缩进2个空格，改成4个
            newFileString = newFileString.replaceAll("(?m)^ +", "$0$0");
            newFileString = newFileString.replace(" />", "/>");

            if (xmlFile.exists()) {
                String oldFileString = ContextUtils.readAllString(xmlFile.toPath());
                oldFileString = oldFileString.substring(oldFileString.indexOf(spliter4Xml) + spliter4Xml.length() + "\r\n".length());
                newFileString = newFileString.replace("</mapper>", spliter4Xml + "\r\n" + oldFileString);
            } else {
                newFileString = newFileString.replace("</mapper>", spliter4Xml + "\r\n</mapper>");
            }

            //MBG合并逻辑与我们预期不同，自行处理下原XML文件
            Files.write(xmlFile.toPath(), newFileString.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //已经是最终的了，合并后返回false，其他插件就不会再走整个方法了
        return false;
    }

    //=============================Dao=============================
    @Override
    public boolean clientGenerated(Interface interfaze,  IntrospectedTable introspectedTable) {
        File xmlFile = ContextUtils.getDaoFile(context, introspectedTable);
        if (xmlFile.exists()) {
            System.out.println(xmlFile + "已经存在，就不生成文件了");
            return false;
        }
        return true;
    }
}
