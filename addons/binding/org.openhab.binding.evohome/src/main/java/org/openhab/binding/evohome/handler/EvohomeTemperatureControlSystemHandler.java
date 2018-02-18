/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.handler;

import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.evohome.EvohomeBindingConstants;
import org.openhab.binding.evohome.internal.api.EvohomeApiClient;
import org.openhab.binding.evohome.internal.api.models.ControlSystem;

import java.util.Map;

/**
 * Handler for a temperature control system. Gets and sets global system mode.
 * @author Jasper van Zuijlen
 *
 */
public class EvohomeTemperatureControlSystemHandler extends BaseEvohomeHandler {
    private String controlSystemName;
    private String controlSystemId;

    public EvohomeTemperatureControlSystemHandler(Thing thing) {
        super(thing);

        Map<String, String> props = thing.getProperties();
        controlSystemId   = props.get(EvohomeBindingConstants.DEVICE_ID);
        controlSystemName = props.get(EvohomeBindingConstants.DEVICE_NAME);
    }

    @Override
    public void initialize() {
       updateStatus(ThingStatus.UNKNOWN);
    }


    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        EvohomeGatewayHandler gateway       = (EvohomeGatewayHandler)getBridge().getHandler();
        EvohomeApiClient      client        = gateway.getApiClient();
        ControlSystem         controlSystem = client.getControlSystem(controlSystemId);

        if (controlSystem == null) {
            updateStatus(ThingStatus.OFFLINE);
        } else if (getBridge().getStatus() != ThingStatus.OFFLINE) {
            if (command == RefreshType.REFRESH) {
                //TODO can probably go if we implement this in the Bridge
                update(client);
            } else if (channelUID.getId().equals(EvohomeBindingConstants.SYSTEM_MODE_CHANNEL)) {
                controlSystem.setMode(command.toString());
            }
            updateStatus(ThingStatus.ONLINE);
        }
    }

    @Override
    public void update(EvohomeApiClient client) {
        ControlSystem controlSystem = client.getControlSystem(controlSystemId);

        if (controlSystem != null) {
            String mode = controlSystem.getCurrentMode();
            updateState(EvohomeBindingConstants.SYSTEM_MODE_CHANNEL, new StringType(mode));
        }
    }

}
