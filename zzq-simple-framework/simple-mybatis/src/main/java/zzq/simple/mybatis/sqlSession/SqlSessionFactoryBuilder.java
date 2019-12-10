package zzq.simple.mybatis.sqlSession;


import zzq.simple.mybatis.xml.XMLConfigBuilder;

import java.util.ResourceBundle;

/**
 * Builds {@link SqlSession} instances.
 *
 * @author Clinton Begin
 */
public class SqlSessionFactoryBuilder {
  /**
   * 加载全局配置，塑造SqlSessionFactory
   * @return
   */
  public SqlSessionFactory build(ResourceBundle resourceBundle){
    XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(resourceBundle);
    return build(xmlConfigBuilder.parse());
  }

  public SqlSessionFactory build(Configuration config) {
    return new SqlSessionFactory(config);
  }

}
