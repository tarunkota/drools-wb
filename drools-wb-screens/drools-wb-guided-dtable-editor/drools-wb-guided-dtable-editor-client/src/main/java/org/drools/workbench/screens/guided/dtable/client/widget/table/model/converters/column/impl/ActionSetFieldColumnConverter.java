/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.workbench.screens.guided.dtable.client.widget.table.model.converters.column.impl;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTablePresenter;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseHeaderMetaData;

@Dependent
public class ActionSetFieldColumnConverter extends BaseColumnConverterImpl {

    @Override
    public boolean handles( final BaseColumn column ) {
        return column instanceof ActionSetFieldCol52;
    }

    @Override
    public GridColumn<?> convertColumn( final BaseColumn column,
                                        final GuidedDecisionTablePresenter.Access access,
                                        final GuidedDecisionTableView gridWidget ) {
        return convertColumn( (ActionSetFieldCol52) column,
                              access,
                              gridWidget );
    }

    private GridColumn<?> convertColumn( final ActionSetFieldCol52 column,
                                         final GuidedDecisionTablePresenter.Access access,
                                         final GuidedDecisionTableView gridWidget ) {
        //Check if the column has a "Value List" or an enumeration. Value List takes precedence
        final String factType = columnUtilities.getBoundFactType( column.getBoundName() );
        final String fieldName = column.getFactField();
        final DataType.DataTypes dataType = columnUtilities.getDataType( column );
        if ( columnUtilities.hasValueList( column ) ) {
            return newValueListColumn( column,
                                       access,
                                       gridWidget );

        } else if ( oracle.hasEnums( factType,
                                     fieldName ) ) {
            return newSingleSelectionEnumColumn( factType,
                                                 fieldName,
                                                 dataType,
                                                 column,
                                                 access,
                                                 gridWidget );
        }

        return newColumn( column,
                          access,
                          gridWidget );
    }

    @Override
    public List<GridColumn.HeaderMetaData> makeHeaderMetaData( final BaseColumn column ) {
        return new ArrayList<GridColumn.HeaderMetaData>() {{
            add( new BaseHeaderMetaData( column.getHeader(),
                                         ActionCol52.class.getName() ) );
        }};
    }

}
