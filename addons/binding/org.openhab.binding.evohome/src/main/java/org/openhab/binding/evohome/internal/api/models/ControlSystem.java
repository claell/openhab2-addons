/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models;

import org.openhab.binding.evohome.internal.api.models.v2.response.TemperatureControlSystem;

/**
 * Interface defining a control system. Acts like an entry point for controlling the evohome display
 * @author Jasper van Zuijlen
 *
 */
public interface ControlSystem {
    /**
     * Gets the ID of the control system
     * @return The ID as a string
     */
    public String getId();

    /**
     * Gets the name of the control system
     * @return The name as a string
     */
    public String getName();

    /**
     * Gets the list of available modes
     * @return The list of available modes as strings
     */
    public String[] getModes();

    /**
     * Gets the current mode
     * @return The current mode as a string
     */
    public String getCurrentMode();

    /**
     * Updated the current mode
     * @param mode The mode to transition to as a string
     */
    public void setMode(String mode);

    /**
     * Gets the heating zones associated with this control system (display)
     * @return The heating zones as a TemperatureControlSystem instance
     */
    TemperatureControlSystem getHeatingZones();
}
