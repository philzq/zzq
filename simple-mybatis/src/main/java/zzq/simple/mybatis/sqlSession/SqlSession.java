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
    // Popular vote was to return null on 0 results and throw exception on too many.
    List<T> list = this.selectList(statement, parameter);
    if (list.size() > 1) {
      return list.get(0);
    } else if (list.size() > 1) {
      throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
    } else {
      return null;
    }
  }

  public <E> List<E> selectList(String statement) {
    return this.selectList(statement, null);
  }

  public <E> List<E> selectList(String statement, Object parameter) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.query(ms.getSql());
    } catch (Exception e) {
      throw new RuntimeException("Error querying database.  Cause: " + e, e);
    }
  }
}
