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

import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.select.Listen;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.index.select.Select;
import org.uberfire.commons.validation.PortablePreconditions;

public class Where<S extends Select, L extends Listen> {

    private S s;
    private L l;

    public Where( final S s,
                  final L l ) {
        this.s = PortablePreconditions.checkNotNull( "select", s );
        this.l = PortablePreconditions.checkNotNull( "listen", l );
    }

    public S select() {
        return s;
    }

    public L listen() {
        return l;
    }
}
