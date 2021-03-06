/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.imports.Import;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.ExtendedGuidedDecisionTableBuilder;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.UpdateHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.decoratedgrid.client.widget.data.Coordinate;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class RuleInspectorCacheTest {

    private RuleInspectorCache    cache;
    private GuidedDecisionTable52 table52;

    @Mock
    private UpdateHandler updateHandler;

    @GwtMock
    DateTimeFormat dateTimeFormat;

    @Before
    public void setUp() throws Exception {
        Map<String, String> preferences = new HashMap<String, String>();
        preferences.put( ApplicationPreferences.DATE_FORMAT, "dd-MMM-yyyy" );
        ApplicationPreferences.setUp( preferences );

        table52 = new ExtendedGuidedDecisionTableBuilder( "org.test",
                                                          new ArrayList<Import>(),
                                                          "mytable" )
                .withConditionIntegerColumn( "a", "Person", "age", "==" )
                .withConditionIntegerColumn( "a", "Person", "age", "==" )
                .withActionSetField( "a", "approved", DataType.TYPE_BOOLEAN )
                .withData( new Object[][]{
                        {1, "description", 0, 1, true},
                        {2, "description", 0, 1, true},
                        {3, "description", 0, 1, true},
                        {4, "description", 0, 1, true},
                        {5, "description", 0, 1, false},
                        {6, "description", 0, 1, true},
                        {7, "description", 0, 1, true}} )
                .build();

        cache = new RuleInspectorCache( mock( AsyncPackageDataModelOracle.class ),
                                        table52,
                                        updateHandler );
    }

    @Test
    public void testInit() throws Exception {
        assertEquals( 7, cache.all().size() );
    }

    @Test
    public void testRemoveRow() throws Exception {
        cache.removeRow( 3 );

        final Collection<RuleInspector> all = cache.all();
        assertEquals( 6, all.size() );


        assertContainsRowNumbers( all,
                                  0, 1, 2, 3, 4, 5 );
    }

    private void assertContainsRowNumbers( final Collection<RuleInspector> all,
                                           final int... numbers ) {
        final ArrayList<Integer> rowNumbers = new ArrayList<>();
        for ( final RuleInspector ruleInspector : all ) {
            final int rowIndex = ruleInspector.getRowIndex();
            rowNumbers.add( rowIndex );
        }

        for ( final int number : numbers ) {
            assertTrue( rowNumbers.toString(),
                        rowNumbers.contains( number ) );
        }
    }

    @Test
    public void testRemoveColumn() throws Exception {

        table52.getActionCols().clear();
        table52.getData().get( 0 ).remove( 4 );
        table52.getData().get( 1 ).remove( 4 );
        table52.getData().get( 2 ).remove( 4 );
        table52.getData().get( 3 ).remove( 4 );
        table52.getData().get( 4 ).remove( 4 );
        table52.getData().get( 5 ).remove( 4 );
        table52.getData().get( 6 ).remove( 4 );

        cache.deleteColumns( 4, 1 );

        Collection<RuleInspector> all = cache.all();
        assertEquals( 7, all.size() );

        for ( RuleInspector ruleInspector : all ) {
            assertFalse( ruleInspector.atLeastOneActionHasAValue() );
        }
    }

    @Test
    public void testUpdate() throws Exception {
        assertEquals( 7, cache.all().size() );

        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add( new Coordinate( 3, 3 ) );

        List<List<DTCellValue52>> data = table52.getData();
        data.get( 3 ).get( 3 ).setNumericValue( 0 );

        cache.updateRuleInspectors( coordinates,
                                    table52 );

        ArgumentCaptor<List> coordinatesCaptor = ArgumentCaptor.forClass( List.class );
        verify( updateHandler ).updateCoordinates( coordinatesCaptor.capture() );
        final List<Coordinate> updatedCoordinates = coordinatesCaptor.getValue();
        assertEquals( 1, updatedCoordinates.size() );
        assertEquals( 3, updatedCoordinates.get( 0 ).getRow() );

        assertEquals( 7, cache.all().size() );
        assertContainsRowNumbers( cache.all(),
                                  0, 1, 2, 3, 4, 5, 6 );
    }

}