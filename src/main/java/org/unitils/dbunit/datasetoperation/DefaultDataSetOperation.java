package org.unitils.dbunit.datasetoperation;

import org.dbunit.dataset.IDataSet;
import org.unitils.dbunit.util.DbUnitDatabaseConnection;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class DefaultDataSetOperation implements DataSetOperation {
    
    public void execute(DbUnitDatabaseConnection dbUnitDatabaseConnection, IDataSet dataSet) {
        throw new UnsupportedOperationException("This dataSet operation is a surrogate representing the default " +
                "DataSetOperation, which should be specified in one of the unitils configuration files. It should not " +
                "be executed directly");
    }
}