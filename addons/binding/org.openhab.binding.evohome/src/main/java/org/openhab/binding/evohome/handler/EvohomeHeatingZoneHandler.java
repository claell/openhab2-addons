/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.handler;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.evohome.EvohomeBindingConstants;
import org.openhab.binding.evohome.internal.api.EvohomeApiClient;
import org.openhab.binding.evohome.internal.api.models.v2.response.ZoneStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EvohomeHeatingZoneHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jasper van Zuijlen - Initial contribution
 * @author Neil Renaud - Working implementation
 */
public class EvohomeHeatingZoneHandler extends BaseEvohomeHandler {

    private final Logger logger = LoggerFactory.getLogger(EvohomeHeatingZoneHandler.class);

    private int zoneId;

    public EvohomeHeatingZoneHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.ONLINE);

        if(getBridge() != null && getBridge().getHandler() != null){
            EvohomeApiClient apiClient = ((EvohomeGatewayHandler) getBridge().getHandler()).getApiClient();
            String zoneId = getThing().getProperties().get(EvohomeBindingConstants.ZONE_ID);
            String locationId = getThing().getProperties().get(EvohomeBindingConstants.LOCATION_ID);
            ZoneStatus zoneStatus = apiClient.getHeatingZone(locationId, zoneId);
            updateZoneStatus(zoneStatus);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (getBridge().getStatus() != ThingStatus.OFFLINE) {
            if(command instanceof RefreshType){
                if(getBridge() != null && getBridge().getHandler() != null){
                    EvohomeApiClient apiClient = ((EvohomeGatewayHandler) getBridge().getHandler()).getApiClient();
                    String zoneId = getThing().getProperties().get(EvohomeBindingConstants.ZONE_ID);
                    String locationId = getThing().getProperties().get(EvohomeBindingConstants.LOCATION_ID);
                    ZoneStatus zoneStatus = apiClient.getHeatingZone(locationId, zoneId);
                    updateZoneStatus(zoneStatus);

                    updateStatus(ThingStatus.ONLINE);
                }
            }
        }
    }

    @Override
    public void update(EvohomeApiClient client) {
        String locationId = getThing().getProperties().get(EvohomeBindingConstants.LOCATION_ID);
        String zoneId = getThing().getProperties().get(EvohomeBindingConstants.ZONE_ID);
        logger.debug("Updating thing[{}] locationId[{}] zoneId[{}]", getThing().getLabel(), locationId, zoneId);
        ZoneStatus zoneStatus = client.getHeatingZone(locationId, zoneId);
        updateZoneStatus(zoneStatus);
    }

    private void updateZoneStatus(ZoneStatus zoneStatus){
        if(zoneStatus == null){
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR);
            return;
        }
        if(!zoneStatus.temperature.isAvailable){
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Zone is offline");
            return;
        }

        double temperature = zoneStatus.temperature.temperature;
        double targetTemperature = zoneStatus.heatSetpoint.targetTemperature;
        String mode = zoneStatus.heatSetpoint.setpointMode;

        updateState(EvohomeBindingConstants.TEMPERATURE_CHANNEL, new DecimalType(temperature));
        updateState(EvohomeBindingConstants.CURRENT_SET_POINT_CHANNEL, new DecimalType(targetTemperature));
        updateState(EvohomeBindingConstants.SET_POINT_STATUS_CHANNEL, new StringType(mode));
    }
}
