package zzq.plugins.mybatis.generator.plugins;

import zzq.plugins.mybatis.generator.util.ContextUtils;
import org.mybatis.generator.api.PluginAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;


public class DeleteGeneratedFilesPlugin extends PluginAdapter {
    private boolean deleteJavaModel = true;
    private boolean deleteSqlMap = true;
    private boolean deleteJavaClient = true;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        deleteJavaModel = isTrue(properties.getProperty("deleteJavaModel"));
        deleteSqlMap = isTrue(properties.getProperty("deleteSqlMap"));
        deleteJavaClient = isTrue(properties.getProperty("deleteJavaClient"));
    }

    private boolean isTrue(String value) {
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validate(List<String> list) {
        try {
            deleteIt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void deleteIt() throws Exception {
        if (deleteJavaClient) {
            Path path = ContextUtils.getDaoPath(context);
            deleteFilesInPath(path, "JavaClient");
        }

        if (deleteJavaModel) {
            Path path =  ContextUtils.getModelPath(context);
            deleteFilesInPath(path, "JavaModel");
        }

        if (deleteSqlMap) {
            Path path =  ContextUtils.getXmlPath(context);
            deleteFilesInPath(path, "SqlMap");
        }
    }

    private void deleteFilesInPath(Path path, String head) throws IOException {
        System.out.println("[INFO] ================== delete " + head + " =====================");
        Files.list(path).forEach(file -> {
            try {
                Files.deleteIfExists(file);
                System.out.println("[INFO] deleted  " + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
