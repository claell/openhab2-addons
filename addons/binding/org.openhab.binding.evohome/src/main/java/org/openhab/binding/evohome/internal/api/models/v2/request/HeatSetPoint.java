/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v2.request;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Request for heating set point.
 * @author Nigel Magnay
 */
public class HeatSetPoint {
    @SerializedName("HeatSetpointValue")
    public double heatSetpointValue;

    @SerializedName("SetpointMode")
    public SetpointMode setpointMode;

    @SerializedName("TimeUntil")
    public String timeUntil;

    public void setTimeUntil(LocalDateTime timeUntil) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date out = Date.from(timeUntil.atZone(ZoneId.systemDefault()).toInstant());
        this.timeUntil = simpleDateFormat.format(out);
    }
}
