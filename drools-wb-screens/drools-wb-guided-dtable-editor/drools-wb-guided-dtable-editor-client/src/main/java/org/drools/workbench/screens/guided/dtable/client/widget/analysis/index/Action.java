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
package org.drools.workbench.screens.guided.dtable.client.widget.analysis.index;

import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.HasKeys;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.KeyDefinition;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.Key;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.UUIDKey;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.UpdatableKey;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.Value;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.keys.Values;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.matchers.UUIDMatchers;
import org.uberfire.commons.validation.PortablePreconditions;

public abstract class Action
        implements HasKeys {

    protected static final KeyDefinition VALUE       = KeyDefinition.newKeyDefinition().withId( "value" ).build();
    protected static final KeyDefinition SUPER_TYPE  = KeyDefinition.newKeyDefinition().withId( "superType" ).build();
    protected static final KeyDefinition COLUMN_UUID = KeyDefinition.newKeyDefinition().withId( "columnUUID" ).build();

    protected final UUIDKey uuidKey = new UUIDKey( this );
    protected final Column               column;
    private final   ActionSuperType      superType;
    protected       UpdatableKey<Action> valueKey;

    public Action( final Column column,
                   final ActionSuperType superType,
                   final Values values ) {
        this.column = PortablePreconditions.checkNotNull( "column", column );
        this.superType = PortablePreconditions.checkNotNull( "superType", superType );
        this.valueKey = new UpdatableKey<>( Action.VALUE,
                                            values );
    }

    public UUIDKey getUuidKey() {
        return uuidKey;
    }

    public static Matchers value() {
        return new Matchers( VALUE );
    }

    public static Matchers superType() {
        return new Matchers( SUPER_TYPE );
    }

    public static Matchers columnUUID() {
        return new Matchers( COLUMN_UUID );
    }

    public Values<Comparable> getValues() {
        final Values result = new Values<>();
        for ( final Value value : valueKey.getValue() ) {
            result.add( value.getComparable() );
        }
        return result;
    }

    public static Matchers uuid() {
        return new UUIDMatchers();
    }

    public static KeyDefinition[] keyDefinitions() {
        return new KeyDefinition[]{
                UUIDKey.UNIQUE_UUID,
                COLUMN_UUID,
                SUPER_TYPE,
                VALUE
        };
    }

    @Override
    public Key[] keys() {
        return new Key[]{
                uuidKey,
                new Key( SUPER_TYPE,
                         superType ),
                new Key( COLUMN_UUID,
                         column.getUuidKey() ),
                valueKey
        };
    }

    public void setValue( final Values values ) {
        if ( !Values.toValues( valueKey.getValue() ).isThereChanges( values ) ) {
            return;
        } else {
            final UpdatableKey<Action> oldKey = valueKey;

            final UpdatableKey<Action> newKey = new UpdatableKey<>( Action.VALUE,
                                                                    values );
            valueKey = newKey;

            oldKey.update( newKey,
                            this );
        }
    }
}
