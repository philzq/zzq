package zzq.service.Impl;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import zzq.entity.ColumnEntity;
import zzq.entity.TableEntity;
import zzq.service.ColumnService;
import zzq.service.GeneratorService;
import zzq.service.TableService;
import zzq.utils.DateUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class GeneratorServiceImpl implements GeneratorService {

    //配置信息
    private static Configuration config = getConfig();

    @Override
    public void generateCode() {
        TableService tableService = new TableServiceImpl();
        ColumnService columnService = new ColumnServiceImpl();
        List<Map<String, Object>> tables = tableService.queryTables();
        if (tables != null && !tables.isEmpty()) {
            tables.forEach(p -> {
                String tableName = p.get("tableName") + "";
                //查询表信息
                Map<String, Object> table = tableService.queryTable(tableName);
                //查询列信息
                List<Map<String, Object>> columns = columnService.queryColumns(tableName);
                //生成代碼並放入指定文件
                generatorCode(table, columns);
            });
        }
    }

    /**
     * 生成代码
     */
    private static void generatorCode(Map<String, Object> table,
                                      List<Map<String, Object>> columns) {
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName") + "");
        tableEntity.setComments(table.get("tableComment") + "");
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, Object> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName") + "");
            columnEntity.setDataType(column.get("dataType") + "");
            columnEntity.setComments(column.get("columnComment") + "");
            columnEntity.setExtra(column.get("extra") + "");

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(convertColumnName(column.get("columnName") + ""));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey") + "") && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        String mainPath = config.getString("mainPath" );

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("package", config.getString("package"));
        map.put("mainPath", mainPath);
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            OutputStream outputStream = null;
            try {
                String fileName = getFileName(config.getString("entityModuleName"), template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"));
                File file = new File(fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                outputStream = new FileOutputStream(file, false);
                //添加到zip
                IOUtils.write(sw.toString(), outputStream, "UTF-8");
                IOUtils.closeQuietly(sw);
            } catch (IOException e) {
                throw new RuntimeException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 首字母变小写
     *
     * @param columnName
     * @return
     */
    private static String convertColumnName(String columnName) {
        if (Character.isLowerCase(columnName.charAt(0)))
            return columnName;
        else
            return (new StringBuilder()).append(Character.toLowerCase(columnName.charAt(0))).append(columnName.substring(1)).toString();
    }

    /**
     * 获取配置信息
     */
    private static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件generator.properties失败");
        }
    }

    private static List<String> getTemplates() {
        URL url = GeneratorServiceImpl.class.getClassLoader().getResource(config.getString("templatePath"));
        if(url == null){
            throw new RuntimeException("模板文件不存在");
        }
        File templateFile = new File(url.getPath());
        List<String> templates = new ArrayList<String>();
        String[] list = templateFile.list();
        for(String file : list){
            templates.add(config.getString("templatePath")+"/"+file);
        }
        return templates;
    }

    /**
     * 表名转换成Java类名
     */
    private static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     */
    private static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取文件名
     */
    private static String getFileName(String entityModuleName, String template, String className, String packageName, String moduleName) {
        String packagePath = moduleName + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String resourcePath = moduleName + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        String entityModulePath = entityModuleName + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
            entityModulePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Entity.java.vm")) {
            return entityModulePath + File.separator + "entity" + File.separator + className + "DBEntity.java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Dao.xml.vm")) {
            return resourcePath + "mapper" + File.separator + className + "Dao.xml";
        }
        return null;
    }
}
