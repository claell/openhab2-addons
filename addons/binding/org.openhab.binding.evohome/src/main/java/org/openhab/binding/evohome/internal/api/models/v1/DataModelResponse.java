/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v1;

/**
 * Response model for the data model
 * @author Neil Renaud
 *
 */
public class DataModelResponse {

    private String locationId;
    private String name;
    private Device[] devices;
    private Weather weather;

    public String getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public Device[] getDevices() {
        return devices;
    }

    public Weather getWeather() {
        return weather;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("locationId[" + locationId + "] name[" + name + "]\n");
        builder.append("device[").append("\n");
        for (Device device : devices) {
            builder.append(" ").append(device).append("\n");
        }
        builder.append("]\n");
        builder.append("weather[" + weather + "]");

        return builder.toString();
    }
}
