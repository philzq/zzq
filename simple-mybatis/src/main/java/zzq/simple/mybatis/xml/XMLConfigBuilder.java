package zzq.simple.mybatis.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import zzq.simple.mybatis.config.MappedStatement;
import zzq.simple.mybatis.mapper.ClassPathMapperScanner;
import zzq.simple.mybatis.sqlSession.Configuration;
import zzq.simple.mybatis.sqlSession.Environment;
import zzq.simple.mybatis.transaction.JdbcTransaction;

import java.io.InputStream;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * 加载mapper.xml
 */
public class XMLConfigBuilder {

    private final ResourceBundle resourceBundle;

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
            //加载接口
            ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(configuration);
            classPathMapperScanner.doScanMapper();
            //加载mapper.xml
            Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
                    .getResources(resourceBundle.getString("mapperLocation"));
            readMapper(mapperLocations,configuration);
        }catch (Exception e){
            e.getStackTrace();
        }
        return configuration;
    }

    public void readMapper(Resource[] mapperLocations,Configuration configuration) throws Exception{
        if(mapperLocations==null){
            return;
        }
        for(Resource resource : mapperLocations){
            InputStream inputStream = resource.getInputStream();
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
        }
    }
}
