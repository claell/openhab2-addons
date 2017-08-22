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
 * Response model for the login
 * @author Neil Renaud
 *
 */
public class LoginResponse {

    private String sessionId;

    private UserInfo userInfo;

    @Override
    public String toString() {
        return "sessionId[" + sessionId + "] userInfo[" + userInfo + "]";
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
