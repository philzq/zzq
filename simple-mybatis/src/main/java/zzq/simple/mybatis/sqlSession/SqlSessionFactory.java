package zzq.simple.mybatis.sqlSession;

import zzq.simple.mybatis.executor.Executor;
import zzq.simple.mybatis.transaction.JdbcTransaction;

/**
 * @author Clinton Begin
 */
public class SqlSessionFactory {

    private final Configuration configuration;

    public SqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 可以根据各种配置塑造sqlsession对象(执行器，事物，等等)
     *
     * @return
     */
    public SqlSession openSession() {
        Environment environment = this.configuration.getEnvironment();
        //获取JdbcTransaction事物实例
        JdbcTransaction jdbcTransaction = environment.getJdbcTransaction();
        //获取执行器实例
        Executor executor = new Executor(jdbcTransaction);
        return new SqlSession(configuration, executor);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
