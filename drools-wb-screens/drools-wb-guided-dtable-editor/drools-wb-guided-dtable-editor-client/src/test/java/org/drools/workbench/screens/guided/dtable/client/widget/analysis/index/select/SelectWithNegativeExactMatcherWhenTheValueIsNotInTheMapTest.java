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
package org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.select;

import java.util.Collection;

import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.HasKeys;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.KeyDefinition;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.KeyTreeMap;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.MultiMap;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.Key;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.UUIDKey;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.Value;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.matchers.ExactMatcher;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelectWithNegativeExactMatcherWhenTheValueIsNotInTheMapTest {

    private Select<Item> select;

    private MultiMap<Value, Item> makeMap() {
        final MultiMap<Value, Item> map = new MultiMap<>();

        map.put( new Value( 0 ),
                 new Item( 0 ) );
        map.put( new Value( 56 ),
                 new Item( 56 ) );
        map.put( new Value( 100 ),
                 new Item( 100 ) );
        map.put( new Value( 1200 ),
                 new Item( 1200 ) );
        return map;
    }

    private void fill( final KeyTreeMap<Item> itemKeyTreeMap,
                       final Item item ) {
        itemKeyTreeMap.put( item );
    }

    @Before
    public void setUp() throws Exception {
        this.select = new Select<>( makeMap(),
                                    new ExactMatcher( null,
                                                      "cost",
                                                      true ) );
    }

    @Test
    public void testAll() throws Exception {
        final Collection<Item> all = select.all();

        assertEquals( 4, all.size() );
    }

    @Test
    public void testFirst() throws Exception {
        assertEquals( 0,
                      select.first().cost );
    }

    @Test
    public void testLast() throws Exception {
        assertEquals( 1200,
                      select.last().cost );
    }

    private class Item
            implements HasKeys {

        private int cost;

        public Item( final int cost ) {
            this.cost = cost;
        }

        @Override
        public Key[] keys() {
            return new Key[]{
                    new UUIDKey( this ),
                    new Key( KeyDefinition.newKeyDefinition().withId( "cost" ).build(),
                             cost )
            };
        }
    }
}