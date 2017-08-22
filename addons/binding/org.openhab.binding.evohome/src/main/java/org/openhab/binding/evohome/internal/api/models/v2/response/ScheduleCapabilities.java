/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v2.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for the schedule capabilities
 * @author Jasper van Zuijlen
 *
 */
public class ScheduleCapabilities {

    @SerializedName("maxSwitchpointsPerDay")
    public int maxSwitchpointsPerDay;

    @SerializedName("minSwitchpointsPerDay")
    public int minSwitchpointsPerDay;

    @SerializedName("setpointValueResolution")
    public double setpointValueResolution;

    //TODO Should be of time time, format: 00:10:00
    @SerializedName("timingResolution")
    public String timingResolution;

}
