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
 * Response model for the time zone
 * @author Jasper van Zuijlen
 *
 */
public class TimeZone {

    @SerializedName("timeZoneId")
    public String timeZoneId;

    @SerializedName("displayName")
    public String displayName;

    @SerializedName("offsetMinutes")
    public int offsetMinutes;

    @SerializedName("currentOffsetMinutes")
    public int currentOffsetMinutes;

    @SerializedName("supportsDaylightSaving")
    public boolean supportsDaylightSaving;

}
