/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v1;

import com.google.gson.annotations.SerializedName;

/**
 * Request model for the login
 * @author Neil Renaud
 *
 */
public class LoginRequest {

    @SerializedName("Username")
    private final String username;

    @SerializedName("Password")
    private final String password;

    @SerializedName("ApplicationId")
    private final String applicationId;

    //
    public LoginRequest(String username, String password, String applicationId) {
        this.username = username;
        this.password = password;
        this.applicationId = applicationId;
    }
}
