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

import java.util.List;

/**
 * Response model for the gateway status
 * @author Jasper van Zuijlen
 *
 */
public class GatewayStatus {

    @SerializedName("gatewayId")
    public String gatewayId;

    @SerializedName("temperatureControlSystems")
    public List<TemperatureControlSystemStatus> temperatureControlSystems;

    @SerializedName("activeFaults")
    public List<ActiveFault> activeFaults;

}
