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
package org.unitils.dataset.comparison;

import org.unitils.dataset.factory.DataSetRowSource;
import org.unitils.dataset.loader.impl.Database;

import java.util.List;
import java.util.Properties;

/**
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public interface ExpectedDataSetStrategy {


    void init(Properties configuration, Database database);

    /**
     * Asserts that the given expected schema is equal to the actual schema.
     * Tables, rows or columns that are not specified in the expected schema will be ignored.
     * If an empty table is specified in the expected schema, it will check that the actual table is also be empty.
     *
     * @param dataSetRowSource The expected data set, not null
     * @param variables        Variables that will be replaced in the data set if needed, not null
     * @throws AssertionError When the assertion fails.
     */
    void assertExpectedDataSet(DataSetRowSource dataSetRowSource, List<String> variables, boolean logDatabaseContentOnAssertionError) throws AssertionError;

}