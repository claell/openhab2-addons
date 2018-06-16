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

public class WonderDevice extends DeviceBase {

    public WonderDevice(String id) {
        super(id);
        mDeviceType = DeviceType.color;
        mConnection = new WifiConnection(this);
        mMinCt = 1700;
        mMaxCt = 6500;
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
                    // DeviceMethod(MethodAction.PROP,
                    // new Object[] { "power", "name", "bright", "ct", "rgb", "hue", "sat" });
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

                    // ct:
                    mDeviceStatus.setCt(status.get(3).getAsInt());

                    // color:
                    int color = status.get(4).getAsInt();
                    mDeviceStatus.setColor(color);
                    mDeviceStatus.setR((color >> 16) & 0xFF);
                    mDeviceStatus.setG((color >> 8) & 0xFF);
                    mDeviceStatus.setB(color & 0xFF);
                    mDeviceStatus.setColor(color);
                    mDeviceStatus.setHue(status.get(5).getAsInt());
                    mDeviceStatus.setSat(status.get(6).getAsInt());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onNotify(msg);
    }
}
