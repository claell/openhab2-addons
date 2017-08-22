/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models;

/**
 * Base class for an evohome control system
 * @author Jasper van Zuijlen
 *
 */
public abstract class BaseControlSystem implements ControlSystem  {

    private String id;
    private String name;

    public BaseControlSystem(String id, String name) {
        this.id   = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

}
