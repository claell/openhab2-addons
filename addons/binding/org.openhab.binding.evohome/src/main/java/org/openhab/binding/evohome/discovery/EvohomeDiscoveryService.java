/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.discovery;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.evohome.EvohomeBindingConstants;
import org.openhab.binding.evohome.handler.EvohomeGatewayHandler;
import org.openhab.binding.evohome.internal.api.EvohomeApiClient;
import org.openhab.binding.evohome.internal.api.models.ControlSystem;
import org.openhab.binding.evohome.internal.api.models.v2.response.TemperatureControlSystem;
import org.openhab.binding.evohome.internal.api.models.v2.response.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link EvohomeDiscoveryService} class is capable of discovering the available data from Evohome
 *
 * @author Neil Renaud - Initial contribution
 */
public class EvohomeDiscoveryService extends AbstractDiscoveryService {
    private static final Logger logger = LoggerFactory.getLogger(EvohomeDiscoveryService.class);
    private static final int SEARCH_TIME_SECONDS = 2;

    private final EvohomeGatewayHandler evohomeGatewayHandler;

    public EvohomeDiscoveryService(EvohomeGatewayHandler evohomeBridgeHandler) {
        super(EvohomeBindingConstants.SUPPORTED_THING_TYPES_UIDS, SEARCH_TIME_SECONDS);
        this.evohomeGatewayHandler = evohomeBridgeHandler;
    }

    @Override
    public void startScan() {
        logger.debug("Evohome start scan");

        EvohomeApiClient client = evohomeGatewayHandler.getApiClient();
        if (client != null) {
            for (ControlSystem controlSystem : client.getControlSystems()) {
                discoverDisplay(controlSystem);
                discoverHeatingZones(controlSystem.getId(), controlSystem.getHeatingZones());
            }
        }

        stopScan();
    }

    private void discoverHeatingZones(String locationId, TemperatureControlSystem heatingZones) {
        for(Zone zone : heatingZones.zones){
            ThingUID thingUID = findThingUID(EvohomeBindingConstants.THING_TYPE_EVOHOME_HEATING_ZONE, zone.name);
            Map<String, Object> properties = new HashMap<>();
            properties.put(EvohomeBindingConstants.LOCATION_ID, locationId);
            properties.put(EvohomeBindingConstants.ZONE_ID, zone.zoneId);
            properties.put(EvohomeBindingConstants.ZONE_NAME, zone.name);
            properties.put(EvohomeBindingConstants.ZONE_TYPE, zone.zoneType);
            properties.put(EvohomeBindingConstants.ZONE_MODEL_TYPE, zone.modelType);
            addDiscoveredThing(thingUID, properties, zone.name);
        }
    }

    private void discoverDisplay(ControlSystem controlSystem) {
        String name = controlSystem.getName();
        ThingUID thingUID = findThingUID(EvohomeBindingConstants.THING_TYPE_EVOHOME_DISPLAY, name);
        Map<String, Object> properties = new HashMap<>();

        //TODO NR: Doesn't the gateway also have a location? I don't have a dual location system but from the EvoHome UI
        //         it looks to me like you can have two houses in one account and then you could have 2 displays...
        //     JZ: Yes it does, I even think that a single location can have multiple displays. Currently that info is
        //         not stored. Something to implement and test for the future as I don't think that there are a lot of
        //         users with more than one gateway and/or display. I raised an issue accordingly.

        properties.put(EvohomeBindingConstants.DEVICE_NAME, name);
        properties.put(EvohomeBindingConstants.DEVICE_ID, controlSystem.getId());
        addDiscoveredThing(thingUID, properties, name);
    }

    private void addDiscoveredThing(ThingUID thingUID, Map<String, Object> properties, String displayLabel) {
        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                .withBridge(evohomeGatewayHandler.getThing().getUID()).withLabel(displayLabel).build();

        thingDiscovered(discoveryResult);
    }

    private ThingUID findThingUID(ThingTypeUID thingType, String thingId) {
        return new ThingUID(thingType, evohomeGatewayHandler.getThing().getUID(),
                thingId.replaceAll("[^a-zA-Z0-9_]", ""));
    }

    private Optional<ThingTypeUID> getTypeUIDById(String thingType) {
        return getSupportedThingTypes().stream().filter( tt -> tt.getId().equalsIgnoreCase(thingType)).findFirst();
    }
}
