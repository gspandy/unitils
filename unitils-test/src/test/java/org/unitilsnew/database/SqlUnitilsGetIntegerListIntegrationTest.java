/*
 * Copyright 2012,  Unitils.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.unitilsnew.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unitils.core.UnitilsException;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;
import static org.unitilsnew.database.SqlUnitils.executeUpdate;
import static org.unitilsnew.database.SqlUnitils.executeUpdateQuietly;

/**
 * @author Tim Ducheyne
 */
public class SqlUnitilsGetIntegerListIntegrationTest {

    @Before
    public void initialize() {
        cleanup();
        executeUpdate("create table my_table (value int, other varchar)");
        executeUpdate("create table my_table (value int)", "database2");
        executeUpdate("insert into my_table (value, other) values (111, 'xxx')");
        executeUpdate("insert into my_table (value) values (222)");
        executeUpdate("insert into my_table (value) values (333)", "database2");
    }

    @After
    public void cleanup() {
        executeUpdateQuietly("drop table my_table", "database1");
        executeUpdateQuietly("drop table my_table", "database2");
    }


    @Test
    public void defaultDatabase() throws Exception {
        List<Integer> result = SqlUnitils.getIntegerList("select value from my_table");
        assertLenientEquals(asList(111, 222), result);
    }

    @Test
    public void namedDatabase() throws Exception {
        List<Integer> result = SqlUnitils.getIntegerList("select value from my_table", "database2");
        assertLenientEquals(asList(333), result);
    }

    @Test
    public void nullValue() throws Exception {
        executeUpdate("update my_table set value = null");

        List<Integer> result = SqlUnitils.getIntegerList("select value from my_table");
        assertLenientEquals(asList(null, null), result);
    }

    @Test
    public void exceptionWhenWhenNotAnIntegerValue() throws Exception {
        try {
            SqlUnitils.getIntegerList("select other from my_table where value = 111");
            fail("UnitilsException expected");
        } catch (UnitilsException e) {
            assertEquals("Unable to execute statement: 'select other from my_table where value = 111'.\n" +
                    "Reason: BadSqlGrammarException: StatementCallback; bad SQL grammar [select other from my_table where value = 111]; nested exception is java.sql.SQLException: Wrong data type: type: VARCHAR (12) expected: INTEGER value: xxx", e.getMessage());
        }
    }

    @Test
    public void emptyListWhenNoResultsFound() throws Exception {
        List<Integer> result = SqlUnitils.getIntegerList("select value from my_table where value = '999'");
        assertTrue(result.isEmpty());
    }

    @Test
    public void exceptionWhenDatabaseNameNotFound() throws Exception {
        try {
            SqlUnitils.getIntegerList("select value from my_table", "xxx");
            fail("UnitilsException expected");
        } catch (UnitilsException e) {
            assertEquals("No configuration found for database with name 'xxx'", e.getMessage());
        }
    }

    @Test
    public void defaultDatabaseWhenNullDatabaseName() throws Exception {
        List<Integer> result = SqlUnitils.getIntegerList("select value from my_table", null);
        assertLenientEquals(asList(111, 222), result);
    }

    @Test
    public void exceptionWhenInvalidStatement() throws Exception {
        try {
            SqlUnitils.getIntegerList("xxx");
            fail("UnitilsException expected");
        } catch (UnitilsException e) {
            assertEquals("Unable to execute statement: 'xxx'.\n" +
                    "Reason: BadSqlGrammarException: StatementCallback; bad SQL grammar [xxx]; nested exception is java.sql.SQLException: Unexpected token: XXX in statement [xxx]", e.getMessage());
        }
    }
}
