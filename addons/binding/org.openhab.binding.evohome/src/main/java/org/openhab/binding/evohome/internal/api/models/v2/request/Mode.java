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

/**
 * Response model for the mode
 * @author Jasper van Zuijlen
 *
 */
public class Mode {

    public Mode(String mode) {
        systemMode = mode;
        timeUntil  = null;
        permanent  = true;
    }

    public Mode(String mode, String time) {
        systemMode = mode;
        timeUntil  = null;
        permanent  = false;
        // TODO {"SystemMode":mode,"TimeUntil":"%sT00:00:00Z" % until.strftime('%Y-%m-%d'),"Permanent":False}
    }

    @SerializedName("SystemMode")
    public String systemMode;

    @SerializedName("TimeUntil")
    public String timeUntil;

    @SerializedName("Permanent")
    public boolean permanent;

}
