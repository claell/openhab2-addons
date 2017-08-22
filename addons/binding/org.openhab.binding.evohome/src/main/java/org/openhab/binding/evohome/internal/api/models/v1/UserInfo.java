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
 * Response model for the user info
 * @author Neil Renaud
 *
 */
public class UserInfo {

    @SerializedName("userID")
    private String userId;

    private String username;

    private String firstname;

    private String lastname;

    @Override
    public String toString() {
        return "userId[" + userId + "] username[" + username + "] firstname[" + firstname + "] lastname[" + lastname
                + "]";
    }

    public String getUserId() {
        return userId;
    }

}
