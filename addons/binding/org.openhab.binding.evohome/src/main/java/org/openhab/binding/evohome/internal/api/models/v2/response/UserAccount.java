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
 * Response model for the user account
 * @author Jasper van Zuijlen
 *
 */
public class UserAccount {

    @SerializedName("userId")
    public String userId;

    @SerializedName("username")
    public String userName;

    @SerializedName("firstname")
    public String firstName;

    @SerializedName("lastname")
    public String lastName;

    @SerializedName("streetAddress")
    public String streetAddress;

    @SerializedName("city")
    public String city;

    @SerializedName("postcode")
    public String postCode;

    @SerializedName("country")
    public String country;

    @SerializedName("language")
    public String language;

}
