package zzq.simple.mybatis.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mariadb.jdbc.MariaDbDataSource;
import zzq.simple.mybatis.config.MappedStatement;
import zzq.simple.mybatis.sqlSession.Configuration;
import zzq.simple.mybatis.sqlSession.Environment;
import zzq.simple.mybatis.transaction.JdbcTransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 加载mapper.xml
 */
public class XMLConfigBuilder {

    private final ResourceBundle resourceBundle;

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    private String environment = XMLConfigBuilder.class.getSimpleName();

    public XMLConfigBuilder(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Configuration parse() {
        Configuration configuration = new Configuration();
        try {
            //加载数据源
            MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
            mariaDbDataSource.setUrl(resourceBundle.getString("jdbc.url"));
            mariaDbDataSource.setUserName(resourceBundle.getString("jdbc.userName"));
            mariaDbDataSource.setPassword(resourceBundle.getString("jdbc.passWord"));
            //创建JdbcTransaction事物实例
            JdbcTransaction jdbcTransaction = new JdbcTransaction(mariaDbDataSource);
            Environment env = new Environment(this.environment,jdbcTransaction,mariaDbDataSource);
            configuration.setEnvironment(env);
            //获取配置的所有xml
            String mapperLocation = resourceBundle.getString("mapperLocation");
            List<File> files = getFiles(mapperLocation);
            //加载mapper.xml，同时生成对应接口的代理实例
            readMapper(files,configuration);
        }catch (Exception e){
            e.getStackTrace();
        }
        return configuration;
    }

    private List<File> getFiles(String mapperLocation) {
        String rootDirPath = determineRootDir(mapperLocation);
        String subPattern = mapperLocation.substring(rootDirPath.length());
        rootDirPath = rootDirPath.substring(CLASSPATH_URL_PREFIX.length());
        URL url = resolveURL(rootDirPath);
        return getFiles(url, subPattern);
    }

    public void readMapper(List<File> files,Configuration configuration) throws Exception{
        if(files==null){
            return;
        }
        for(File file : files){
            InputStream inputStream = new FileInputStream(file);
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            String nameSpace = root.attributeValue("nameSpace").trim();//把mapper节点的nameSpace值存为接口名
            for(Iterator rootIter = root.elementIterator(); rootIter.hasNext();) {//遍历根节点下所有子节点
                MappedStatement mappedStatement = new MappedStatement();    //用来存储一条方法的信息
                Element e = (Element) rootIter.next();
                String sqltype = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}");
                String parseSql = genericTokenParser.parse(sql);
                String resultType = e.attributeValue("resultType").trim();
                mappedStatement.setSqltype(sqltype);
                mappedStatement.setFuncName(funcName);
                mappedStatement.setResultType(resultType);
                mappedStatement.setSql(parseSql);
                configuration.addMappedStatement(nameSpace+"."+funcName,mappedStatement);
            }
            //加载接口,生成代理对象
            configuration.addMapper(Class.forName(nameSpace));
        }
    }

    /**
     * 去掉路径后缀
     * @param location
     * @return
     */
    private String determineRootDir(String location) {
        int prefixEnd = location.indexOf(':') + 1;
        int rootDirEnd = location.length();
        rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return location.substring(0, rootDirEnd);
    }

    /**
     * 将路径解析为url
     * @param path
     * @return
     */
    private URL resolveURL(String path) {
        return this.getClass().getClassLoader().getResource(path);
    }

    /**
     * 获取所有匹配文件
     * @param url
     * @param subPattern
     * @return
     */
    private List<File> getFiles(URL url,String subPattern){
        List<File> files = new ArrayList<>();
        resolveFile(new File(url.getFile()),subPattern,files);
        return files;
    }

    private void resolveFile(File rootFile,String subPattern,List<File> files) {
        if(rootFile == null){
            return;
        }
        if(rootFile.isDirectory()){
            File[] listFiles = rootFile.listFiles();
            for(File file:listFiles) {
                if(file.isFile()) {
                    String fileName = file.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if(subPattern==null){
                        files.add(file);
                    } else if(subPattern.toLowerCase().endsWith(suffix.toLowerCase())){
                        files.add(file);
                    }
                }else {
                    resolveFile(file,subPattern,files);
                }
            }
        }else{
            String fileName = rootFile.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if(subPattern==null){
                files.add(rootFile);
            } else if(subPattern.toLowerCase().endsWith(suffix.toLowerCase())){
                files.add(rootFile);
            }
        }
    }
}
