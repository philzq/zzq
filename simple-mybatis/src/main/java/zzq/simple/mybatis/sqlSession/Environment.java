package zzq.simple.mybatis.sqlSession;

import zzq.simple.mybatis.transaction.JdbcTransaction;

import javax.sql.DataSource;

/**
 * @author Clinton Begin
 */
public final class Environment {
  private final String id;
  private final JdbcTransaction jdbcTransaction;
  private final DataSource dataSource;

  public Environment(String id, JdbcTransaction jdbcTransaction, DataSource dataSource) {
    if (id == null) {
      throw new IllegalArgumentException("Parameter 'id' must not be null");
    }
    if (jdbcTransaction == null) {
      throw new IllegalArgumentException("Parameter 'jdbcTransaction' must not be null");
    }
    this.id = id;
    if (dataSource == null) {
      throw new IllegalArgumentException("Parameter 'dataSource' must not be null");
    }
    this.jdbcTransaction = jdbcTransaction;
    this.dataSource = dataSource;
  }

  public String getId() {
    return id;
  }

  public JdbcTransaction getJdbcTransaction() {
    return jdbcTransaction;
  }

  public DataSource getDataSource() {
    return dataSource;
  }
}
