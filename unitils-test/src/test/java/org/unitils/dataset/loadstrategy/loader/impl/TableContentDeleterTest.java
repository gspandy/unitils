/*
 * Copyright Unitils.org
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
package org.unitils.dataset.loadstrategy.loader.impl;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.core.UnitilsException;
import org.unitils.dataset.database.DatabaseAccessor;
import org.unitils.dataset.database.DatabaseMetaData;
import org.unitils.dataset.loadstrategy.impl.IdentifierNameProcessor;
import org.unitils.dataset.loadstrategy.impl.TableContentDeleter;
import org.unitils.dataset.model.dataset.DataSetRow;
import org.unitils.dataset.rowsource.DataSetRowSource;
import org.unitils.mock.Mock;

import static org.junit.Assert.fail;

/**
 * Tests for deleting all data from tables.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class TableContentDeleterTest extends UnitilsJUnit4 {

    /* Tested object */
    private TableContentDeleter tableContentDeleter;

    protected Mock<DataSetRowSource> dataSetRowSource;
    protected Mock<DatabaseAccessor> databaseAccessor;
    protected Mock<IdentifierNameProcessor> identifierNameProcessor;
    protected Mock<DatabaseMetaData> databaseMetaData;

    private DataSetRow dataSetRowTableA;
    private DataSetRow dataSetRowTableB;
    private DataSetRow dataSetRowTableCaseSensitive;


    @Before
    public void initialize() throws Exception {
        tableContentDeleter = new TableContentDeleter(identifierNameProcessor.getMock(), databaseAccessor.getMock(), databaseMetaData.getMock());

        dataSetRowTableA = new DataSetRow("schema_a", "table_a", null, false, null);
        dataSetRowTableB = new DataSetRow("schema_b", "table_b", null, false, null);
        dataSetRowTableCaseSensitive = new DataSetRow("schema_c", "table_c", null, false, null);

        identifierNameProcessor.returns("schema_a.table_a").getQualifiedTableName(dataSetRowTableA);
        identifierNameProcessor.returns("schema_b.table_b").getQualifiedTableName(dataSetRowTableB);
        identifierNameProcessor.returns("\"schema_c\".\"table_c\"").getQualifiedTableName(dataSetRowTableCaseSensitive);
    }


    @Test
    public void deleteDataFromTablesInReverseOrder() throws Exception {
        dataSetRowSource.onceReturns(dataSetRowTableA).getNextDataSetRow();
        dataSetRowSource.onceReturns(dataSetRowTableB).getNextDataSetRow();

        tableContentDeleter.deleteDataFromTablesInReverseOrder(dataSetRowSource.getMock());

        databaseAccessor.assertInvoked().executeUpdate("delete from schema_b.table_b", null);
        databaseAccessor.assertInvoked().executeUpdate("delete from schema_a.table_a", null);
    }

    @Test
    public void caseSensitiveDataSet() throws Exception {
        dataSetRowSource.onceReturns(dataSetRowTableCaseSensitive).getNextDataSetRow();

        tableContentDeleter.deleteDataFromTablesInReverseOrder(dataSetRowSource.getMock());

        databaseAccessor.assertInvoked().executeUpdate("delete from \"schema_c\".\"table_c\"", null);
    }

    @Test
    public void emptyDataSet() throws Exception {
        tableContentDeleter.deleteDataFromTablesInReverseOrder(dataSetRowSource.getMock());
        databaseAccessor.assertNotInvoked().executeUpdate(null, null);
    }

    @Test
    public void tableDoesNotExist() throws Exception {
        dataSetRowSource.onceReturns(dataSetRowTableA).getNextDataSetRow();
        databaseMetaData.raises(UnitilsException.class).assertTableNameExists("schema_a.table_a");
        try {
            tableContentDeleter.deleteDataFromTablesInReverseOrder(dataSetRowSource.getMock());
            fail("Expected UnitilsException");
        } catch (UnitilsException e) {
            //expected
        }
    }
}