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

import java.time.LocalDateTime;


/**
 * The {@link EvohomeHeatingZoneHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jasper van Zuijlen - Initial contribution
 * @author Neil Renaud - Working implementation
 */
public class EvohomeHeatingZoneHandler extends BaseEvohomeHandler {

    private final Logger logger = LoggerFactory.getLogger(EvohomeHeatingZoneHandler.class);
    
    private long setpointLastChanged;

    public EvohomeHeatingZoneHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);

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
            } else {
                if (channelUID.getId().equals(EvohomeBindingConstants.SET_POINT_CHANNEL)) {
                    if( command instanceof DecimalType ) {
                        setZoneSetpoint( ((DecimalType)command).doubleValue()) ;
                    }
                }
            }

        }
    }

    private void setZoneSetpoint(double temp) {
        EvohomeApiClient apiClient = ((EvohomeGatewayHandler) getBridge().getHandler()).getApiClient();
        String zoneId = getThing().getProperties().get(EvohomeBindingConstants.ZONE_ID);

        // On EvoHome, when you set the setpoint, it needs to know when you want to switch back
        // to the schedule. This can be either "permanent override", or until a particular date/time.
        //
        // What the phone app does as a default, which seems sensible, is to find out when then next
        // scheduled change is, and override the setppoint only until then. So to do this we need to
        // find the next scheduled change, and use that date for the changeover.

        LocalDateTime nextSwitchPoint = apiClient.getSchedule(zoneId).getUpcomingSwitchpointDates().get(0);

        apiClient.setHeatingSetpoint(zoneId, temp, nextSwitchPoint);

        updateState(EvohomeBindingConstants.SET_POINT_CHANNEL, new DecimalType(temp));

        setpointLastChanged = System.currentTimeMillis();
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

        // We want the user setpoint control to sit at what the system believes the setpoint is
        // for most of the time (I.E: so you can nudge the temperature up and down without clicking
        // forever). However - we don't want to have a race condition where we keep hitting the up button,
        // the system then fetches the zone status for some other reason, and we blindly reset the "desired
        // setpoint". So only change this value if it's over a minute since we last tried to manually change
        // the setpoint.

        if(setpointLastChanged + 60000 < System.currentTimeMillis() ) {
            updateState(EvohomeBindingConstants.SET_POINT_CHANNEL, new DecimalType(targetTemperature));
        }
    }
}
