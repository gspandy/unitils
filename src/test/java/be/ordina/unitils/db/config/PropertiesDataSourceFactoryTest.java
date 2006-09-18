/*
 * Copyright (C) 2006, Ordina
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package be.ordina.unitils.db.config;

import be.ordina.unitils.util.PropertiesUtils;
import junit.framework.TestCase;
import org.apache.commons.dbcp.BasicDataSource;
import static org.easymock.classextension.EasyMock.*;

/**
 * todo
 */
public class PropertiesDataSourceFactoryTest extends TestCase {

    private PropertiesDataSourceFactory propertiesFileDataSourceConfig;

    private BasicDataSource mockBasicDataSource;

    public void setUp() throws Exception {
        mockBasicDataSource = createMock(BasicDataSource.class);
        propertiesFileDataSourceConfig = new PropertiesDataSourceFactory() {
            protected BasicDataSource getNewDataSource() {
                return mockBasicDataSource;
            }
        };
        propertiesFileDataSourceConfig.init(PropertiesUtils.loadPropertiesFromClasspath("/be/ordina/unitils/db/config/testproperties.properties"));
    }

    public void testCreateDataSource() {
        // expectations
        mockBasicDataSource.setUsername("testusername");
        mockBasicDataSource.setPassword("testpassword");
        mockBasicDataSource.setUrl("testurl");
        mockBasicDataSource.setDriverClassName("testdriver");

        replay(mockBasicDataSource);

        propertiesFileDataSourceConfig.createDataSource();

        // check expectations
        verify(mockBasicDataSource);
    }

}