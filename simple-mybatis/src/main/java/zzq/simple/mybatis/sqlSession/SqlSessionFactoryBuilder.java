package zzq.simple.mybatis.sqlSession;


import java.util.Properties;

/**
 * Builds {@link SqlSession} instances.
 *
 * @author Clinton Begin
 */
public class SqlSessionFactoryBuilder {
  /**
   * 加载全局配置，塑造SqlSessionFactory，此处只加载数据源配置
   * @param properties
   * @return
   */
  public SqlSessionFactory build(Properties properties) {
    //利用数据源配置，塑造SqlSessionFactory对象
    return new SqlSessionFactory();
  }

}
