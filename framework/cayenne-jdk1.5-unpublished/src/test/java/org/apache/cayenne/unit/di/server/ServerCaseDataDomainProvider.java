/*****************************************************************
 *   Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 ****************************************************************/
package org.apache.cayenne.unit.di.server;

import javax.sql.DataSource;

import org.apache.cayenne.access.DataDomain;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.dbsync.SkipSchemaUpdateStrategy;
import org.apache.cayenne.configuration.server.DataDomainProvider;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.map.DataMap;

class ServerCaseDataDomainProvider extends DataDomainProvider {

    @Inject
    protected DataSource dataSource;

    @Inject
    protected DbAdapter adapter;

    @Override
    protected DataDomain createDataDomain() throws Exception {
        DataDomain domain = super.createDataDomain();

        // add nodes dynamically
        // TODO: andrus, 06/14/2010 should probably map them in XML to avoid this mess...
        for (DataMap dataMap : domain.getDataMaps()) {

            DataNode node = new DataNode(dataMap.getName());
            node.setDataSource(dataSource);
            node.setAdapter(adapter);
            node.addDataMap(dataMap);
            node.setSchemaUpdateStrategy(new SkipSchemaUpdateStrategy());

            // customizations from SimpleAccessStackAdapter that are not yet ported...
            // those can be done better now

            // node
            // .getAdapter()
            // .getExtendedTypes()
            // .registerType(new StringET1ExtendedType());
            //
            // // tweak mapping with a delegate
            // for (Procedure proc : map.getProcedures()) {
            // getAdapter(node).tweakProcedure(proc);
            // }

            // use shared data source in all cases but the multi-node...
            // if (MultiNodeCase.NODE1.equals(node.getName())
            // || MultiNodeCase.NODE2.equals(node.getName())) {
            // node.setDataSource(resources.createDataSource());
            // }
            // else {
            // node.setDataSource(resources.getDataSource());
            // }

            domain.addNode(node);
        }

        return domain;
    }

}
