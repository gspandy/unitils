package org.unitils.dbmaintainer.clear;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbutils.DbUtils;
import org.unitils.core.ConfigurationLoader;
import org.unitils.dbmaintainer.maintainer.DBMaintainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBClearer test for a hsqldb database
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class DB2DBClearerTest extends DBClearerTest {


    protected void createTestTrigger(String tableName, String triggerName) throws SQLException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            st.execute("create trigger " + triggerName + " before insert on " + tableName + " FOR EACH ROW begin atomic end");
        } finally {
            DbUtils.closeQuietly(conn, st, null);
        }
    }


    /**
     * Verifies wether the hsqldb dialect is activated
     *
     * @return True if the hsqldb dialect is activated, false otherwise
     */
    protected boolean isTestedDialectActivated() {
        Configuration config = new ConfigurationLoader().loadConfiguration();
        return "db2".equals(config.getString(DBMaintainer.PROPKEY_DATABASE_DIALECT));
    }
}
