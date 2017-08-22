/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api;

import org.openhab.binding.evohome.internal.api.models.ControlSystem;
import org.openhab.binding.evohome.internal.api.models.v1.DataModelResponse;
import org.openhab.binding.evohome.internal.api.models.v2.response.GatewayStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.ZoneStatus;

/**
 * Interface for interacting with a specific version of an evohome client. This interface currently supports one account.
 * @author Jasper van Zuijlen
 *
 */
public interface EvohomeApiClient {

    /**
     * Logs in the client
     * @return True when successful, false otherwise
     */
    boolean login();

    /**
     * Logs out the client
     */
    void logout();

    /**
     * Updates the actual values of the current connection
     */
    void update();

    /**
     * Gets all data including actuals
     * @return The actual data or null
     */
    DataModelResponse[] getData();

    /**
     * Gets the list of available control systems
     * @return The associated control systems as an array
     */
    ControlSystem[] getControlSystems();

    /**
     * Gets a specific control system
     * @param id The id of the control system to fetch
     * @return The specified control system or null
     */
    ControlSystem getControlSystem(String id);

    /**
     * Gets a specific heating zone from the registry
     * @param locationId The ID of the location where the zone is located
     * @param zoneId The ID zone to fetch
     * @return The heating zone or null
     */
    ZoneStatus getHeatingZone(String locationId, String zoneId);

    /**
     * Gets the gateways currently associated with this user
     * @return The list of Gateways or null
     */
    GatewayStatus[] getGateways();

}