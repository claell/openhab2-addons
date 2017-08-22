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
 * Response model for the location info
 * @author Jasper van Zuijlen
 *
 */
public class LocationInfo {

    @SerializedName("locationId")
    public String locationId;

    @SerializedName("name")
    public String name;

    @SerializedName("streetAddress")
    public String streetAddress;

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("postcode")
    public String postcode;

    @SerializedName("locationType")
    public String locationType;

    @SerializedName("useDaylightSaveSwitching")
    public boolean useDaylightSaveSwitching;

    @SerializedName("timeZone")
    public TimeZone timeZone;

    @SerializedName("locationOwner")
    public LocationOwner locationOwner;

}
