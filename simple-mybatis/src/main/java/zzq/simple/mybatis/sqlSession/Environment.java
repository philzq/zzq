/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
