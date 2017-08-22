/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v2.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for zone status
 * @author Jasper van Zuijlen
 *
 */
public class ZoneStatus {

    @SerializedName("zoneId")
    public String zoneId;

    @SerializedName("name")
    public String name;

    @SerializedName("temperatureStatus")
    public TemperatureStatus temperature;

    @SerializedName("heatSetpointStatus")
    public HeatSetpointStatus heatSetpoint;

    @SerializedName("activeFaults")
    public List<ActiveFault> activeFaults;

}
