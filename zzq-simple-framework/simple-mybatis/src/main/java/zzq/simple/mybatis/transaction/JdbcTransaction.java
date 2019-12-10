package zzq.simple.mybatis.transaction;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事物
 */
public class JdbcTransaction{

  protected Connection connection;
  protected DataSource dataSource;
  protected boolean autoCommit;

  public JdbcTransaction(DataSource dataSource) {
    this(dataSource,true);
  }

  public JdbcTransaction(DataSource dataSource, boolean autoCommit) {
    this.dataSource = dataSource;
    this.autoCommit = autoCommit;
  }

  public JdbcTransaction(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() throws SQLException {
    if (connection == null) {
      openConnection();
    }
    return connection;
  }

  public void commit() throws SQLException {
    if (connection != null && !connection.getAutoCommit()) {
      connection.commit();
    }
  }

  public void rollback() throws SQLException {
    if (connection != null && !connection.getAutoCommit()) {
      connection.rollback();
    }
  }

  public void close() throws SQLException {
    if (connection != null) {
      resetAutoCommit();
      connection.close();
    }
  }

  protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
    try {
      if (connection.getAutoCommit() != desiredAutoCommit) {
        connection.setAutoCommit(desiredAutoCommit);
      }
    } catch (SQLException e) {
      // Only a very poorly implemented driver would fail here,
      // and there's not much we can do about that.
      throw new RuntimeException("Error configuring AutoCommit.  "
          + "Your driver may not support getAutoCommit() or setAutoCommit(). "
          + "Requested setting: " + desiredAutoCommit + ".  Cause: " + e, e);
    }
  }

  protected void resetAutoCommit() {
    try {
      if (!connection.getAutoCommit()) {
        // MyBatis does not call commit/rollback on a connection if just selects were performed.
        // Some databases start transactions with select statements
        // and they mandate a commit/rollback before closing the connection.
        // A workaround is setting the autocommit to true before closing the connection.
        // Sybase throws an exception here.
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  protected void openConnection() throws SQLException {
    connection = dataSource.getConnection();
    setDesiredAutoCommit(autoCommit);
  }

  public Integer getTimeout() throws SQLException {
    return null;
  }

}
