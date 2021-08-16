package zzq.plugins.mybatis.generator.util;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContextUtils {

    public static Path getXmlPath(Context context) {
        Path path = Paths.get(
                context.getSqlMapGeneratorConfiguration().getTargetProject(),
                context.getSqlMapGeneratorConfiguration().getTargetPackage().replace(".", "/"));
        return path;
    }

    public static Path getModelPath(Context context) {
        Path path = Paths.get(
                context.getJavaModelGeneratorConfiguration().getTargetProject(),
                context.getJavaModelGeneratorConfiguration().getTargetPackage().replace(".", "/"));
        return path;
    }

    public static Path getDaoPath(Context context) {
        Path path = Paths.get(
                context.getJavaClientGeneratorConfiguration().getTargetProject(),
                context.getJavaClientGeneratorConfiguration().getTargetPackage().replace(".", "/"));
        return path;
    }


    public static File getDaoFile(Context context, IntrospectedTable introspectedTable) {
        String filename = introspectedTable.getMyBatis3XmlMapperFileName();
        filename = filename.replace(".xml", ".java");
        File result = Paths.get(getDaoPath(context).toString(), filename).toFile();
        return  result;
    }

    public static File getModelFile(Context context, IntrospectedTable introspectedTable) {
        String filename = introspectedTable.getBaseRecordType();
        filename = filename.substring(filename.lastIndexOf(".")+1);
        File result = Paths.get(getModelPath(context).toString(), filename+".java").toFile();
        return  result;
    }

    public  static String  readAllString(Path path)
    {
        try {
            return  new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
