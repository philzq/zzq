package zzq.simple.mybatis.sqlSession;

import zzq.simple.mybatis.config.MappedStatement;
import zzq.simple.mybatis.executor.Executor;

import java.util.List;

/**
 * The default implementation for {@link SqlSession}.
 * Note that this class is not Thread-Safe.
 *
 * @author Clinton Begin
 */
public class SqlSession{

  private final Configuration configuration;

  private final Executor executor;

  public SqlSession(Configuration configuration, Executor executor) {
    this.configuration = configuration;
    this.executor = executor;
  }

  public <T> T selectOne(String statement) {
    return this.selectOne(statement, null);
  }

  public <T> T selectOne(String statement, Object parameter) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.selectOne(ms,parameter);
    } catch (Exception e) {
      throw new RuntimeException("Error querying database.  Cause: " + e, e);
    }
  }

  public <E> List<E> selectList(String statement, Object parameter) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.selectList(ms,parameter);
    } catch (Exception e) {
      throw new RuntimeException("Error querying database.  Cause: " + e, e);
    }
  }

    public <T> T mockSelectOne(String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.mockSelectOne(ms,parameter);
        } catch (Exception e) {
            throw new RuntimeException("Error querying database.  Cause: " + e, e);
        }
    }

    public <E> List<E> mockSelectList(String statement, Object parameter) {
        try {
            MappedStatement ms = configuration.getMappedStatement(statement);
            return executor.mockSelectList(ms,parameter);
        } catch (Exception e) {
            throw new RuntimeException("Error querying database.  Cause: " + e, e);
        }
    }
}
