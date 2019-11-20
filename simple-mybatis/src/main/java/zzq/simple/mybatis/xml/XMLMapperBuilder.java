package zzq.simple.mybatis.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import zzq.simple.mybatis.config.MappedStatement;
import zzq.simple.mybatis.sqlSession.Configuration;

import java.io.InputStream;
import java.util.Iterator;

/**
 * 加载mapper.xml
 */
public class XMLMapperBuilder {

    private final Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void readMapper(Resource[] mapperLocations) throws Exception{
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
