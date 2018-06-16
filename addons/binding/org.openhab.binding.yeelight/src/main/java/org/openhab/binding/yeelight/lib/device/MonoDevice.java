/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yeelight.lib.device;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openhab.binding.yeelight.lib.device.connection.WifiConnection;
import org.openhab.binding.yeelight.lib.enums.DeviceType;

public class MonoDevice extends DeviceBase {

    public MonoDevice(String id) {
        super(id);
        mDeviceType = DeviceType.mono;
        mConnection = new WifiConnection(this);
    }

    @Override
    public void onNotify(String msg) {
        JsonObject result = new JsonParser().parse(msg).getAsJsonObject();
        try {
            String id = "-1";
            if (result.has("id")) {
                id = result.get("id").getAsString();
                // for cmd transaction.

                if (mQueryList.contains(id)) {
                    mQueryList.remove(id);
                    // DeviceMethod(MethodAction.PROP, new Object[] { "power", "name", "bright" });
                    JsonArray status = result.get("result").getAsJsonArray();

                    // power:
                    if (status.get(0).toString().equals("\"off\"")) {
                        mDeviceStatus.setPowerOff(true);
                    } else if (status.get(0).toString().equals("\"on\"")) {
                        mDeviceStatus.setPowerOff(false);
                    }

                    // name:
                    mDeviceStatus.setName(status.get(1).getAsString());

                    // brightness:
                    mDeviceStatus.setBrightness(status.get(2).getAsInt());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onNotify(msg);
    }
}
