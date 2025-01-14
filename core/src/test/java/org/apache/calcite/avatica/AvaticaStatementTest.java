/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.avatica;

import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for AvaticaStatement
 */
public class AvaticaStatementTest {

  private AvaticaStatement statement;

  @Before public void setup() {
    statement = mock(AvaticaStatement.class);
  }

  @Test public void testUpdateCounts() throws SQLException {
    long[] longValues = new long[] {-1, -3, 1, 5, ((long) Integer.MAX_VALUE) + 1};
    int[] intValues = new int[] {-1, -3, 1, 5, Integer.MAX_VALUE};
    when(statement.executeBatch()).thenCallRealMethod();
    when(statement.executeLargeBatch()).thenCallRealMethod();
    when(statement.executeBatchInternal()).thenReturn(longValues);

    assertArrayEquals(intValues, statement.executeBatch());
    assertArrayEquals(longValues, statement.executeLargeBatch());
  }

  @Test public void testGetMoreResults() throws SQLException {
    AvaticaResultSet resultSet = mock(AvaticaResultSet.class);
    statement.openResultSet = resultSet;

    doCallRealMethod().when(statement).onResultSetClose(any(ResultSet.class));
    when(statement.getMoreResults()).thenCallRealMethod();
    when(statement.getMoreResults(anyInt())).thenCallRealMethod();

    assertFalse(statement.getMoreResults());
    verify(resultSet).close();
  }

  @Test public void testFetchSize() throws SQLException {
    doCallRealMethod().when(statement).setFetchSize(anyInt());
    when(statement.getFetchSize()).thenCallRealMethod();
    statement.setFetchSize(50);
    assertEquals(50, statement.getFetchSize());
  }

}

// End AvaticaStatementTest.java
