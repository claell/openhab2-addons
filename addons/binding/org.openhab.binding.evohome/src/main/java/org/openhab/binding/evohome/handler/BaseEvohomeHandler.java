/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.handler;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.openhab.binding.evohome.internal.api.EvohomeApiClient;

/**
 * Base class for an evohome handler
 * @author Jasper van Zuijlen
 *
 */
public abstract class BaseEvohomeHandler extends BaseThingHandler {

    public BaseEvohomeHandler(Thing thing) {
        super(thing);
    }

    /**
     * Updates the actual values of the connection
     * @param client The client to use to update the actuals
     */
    public abstract void update(EvohomeApiClient client);
}
