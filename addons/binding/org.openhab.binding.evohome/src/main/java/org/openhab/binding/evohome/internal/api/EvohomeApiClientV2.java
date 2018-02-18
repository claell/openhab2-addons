/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.openhab.binding.evohome.configuration.EvohomeGatewayConfiguration;
import org.openhab.binding.evohome.internal.api.models.ControlSystem;
import org.openhab.binding.evohome.internal.api.models.v1.DataModelResponse;
import org.openhab.binding.evohome.internal.api.models.v2.ControlSystemAndStatus;
import org.openhab.binding.evohome.internal.api.models.v2.ControlSystemV2;
import org.openhab.binding.evohome.internal.api.models.v2.request.HeatSetPoint;
import org.openhab.binding.evohome.internal.api.models.v2.request.SetpointMode;
import org.openhab.binding.evohome.internal.api.models.v2.response.Authentication;
import org.openhab.binding.evohome.internal.api.models.v2.response.Gateway;
import org.openhab.binding.evohome.internal.api.models.v2.response.GatewayStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.Location;
import org.openhab.binding.evohome.internal.api.models.v2.response.LocationStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.Locations;
import org.openhab.binding.evohome.internal.api.models.v2.response.LocationsStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.Schedule;
import org.openhab.binding.evohome.internal.api.models.v2.response.TemperatureControlSystem;
import org.openhab.binding.evohome.internal.api.models.v2.response.TemperatureControlSystemStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.UserAccount;
import org.openhab.binding.evohome.internal.api.models.v2.response.ZoneStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the evohome client V2 api
 * @author Jasper van Zuijlen
 *
 */
public class EvohomeApiClientV2 implements EvohomeApiClient {
    private final Logger logger = LoggerFactory.getLogger(EvohomeApiClientV2.class);

    private final SslContextFactory sslContextFactory = new SslContextFactory();
    private final HttpClient httpClient = new HttpClient(sslContextFactory);

    private EvohomeGatewayConfiguration         configuration;
    private ApiAccess                           apiAccess;
    private UserAccount                         useraccount        = null;
    private Locations                           locations          = null;
    private LocationsStatus                     locationsStatus    = null;
    private Map<String, ControlSystemAndStatus> controlSystemCache = null;

    public EvohomeApiClientV2(EvohomeGatewayConfiguration configuration) {
        this.configuration = configuration;
        logger.info("Creating Evohome API client.");

        try {
            httpClient.start();
        } catch (Exception e) {
            logger.error("Could not start http client.", e);
        }

        apiAccess = new ApiAccess(httpClient);
        if (configuration != null) {
            apiAccess.setApplicationId(configuration.applicationId);
        }
    }

    /**
     * Gets the status of all locations
     * @return The status as a LocationsStatus object
     */
    public LocationsStatus getLocationStatus(){
        return locationsStatus;
    }

    /**
     * Closes the current connection to the API
     */
    public void close() {
        apiAccess.setAuthentication(null);
        useraccount     = null;
        locations       = null;
        locationsStatus = null;

        if (httpClient.isStarted()) {
            try {
                httpClient.stop();
            } catch (Exception e) {
                logger.error("Could not stop http client.", e);

            }
        }
    }

    private UserAccount requestUserAccount() {
        String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_ACCOUNT;

        UserAccount userAccount =  new UserAccount();
        userAccount = apiAccess.doAuthenticatedRequest(HttpMethod.GET, url, null, null, userAccount);

        return userAccount;
    }

    private Locations requestLocations() {
        Locations locations = null;
        if (useraccount != null) {
            String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_LOCATIONS;
            url = String.format(url, useraccount.userId);

            locations = new Locations();
            locations = apiAccess.doAuthenticatedRequest(HttpMethod.GET, url, null, null, locations);
        }

        return locations;
    }

    private LocationsStatus requestLocationsStatus() {
        LocationsStatus locationsStatus = new LocationsStatus();

        if (locations != null) {
            for(Location location : locations) {
                String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_STATUS;
                url = String.format(url, location.locationInfo.locationId);
                LocationStatus status = new LocationStatus();
                status = apiAccess.doAuthenticatedRequest(HttpMethod.GET, url, null, null, status);
                locationsStatus.add(status);
            }
        }

        return locationsStatus;
    }

    @Override
    public boolean login() {
       boolean success = authenticateWithUsername();

        // If the authentication succeeded, gather the basic intel as well
        if (success) {
            useraccount        = requestUserAccount();
            locations          = requestLocations();
            controlSystemCache = populateCache();
            update();
        } else {
            apiAccess.setAuthentication(null);
            logger.error("Authorization failed");
        }

        return success;
    }

    private boolean authenticate(String credentials, String grantType) {
        Authentication authentication = new Authentication();

            String data = credentials + "&"
                        + "Host=rs.alarmnet.com%2F&"
                        + "Pragma=no-cache&"
                        + "Cache-Control=no-store+no-cache&"
                        + "scope=EMEA-V1-Basic+EMEA-V1-Anonymous+EMEA-V1-Get-Current-User-Account&"
                        + "grant_type=" + grantType+ "&"
                        + "Content-Type=application%2Fx-www-form-urlencoded%3B+charset%3Dutf-8&"
                        + "Connection=Keep-Alive";

        HashMap<String,String> headers = new HashMap<>();
        
        headers.put("Authorization", "Basic YjAxM2FhMjYtOTcyNC00ZGJkLTg4OTctMDQ4YjlhYWRhMjQ5OnRlc3Q=");
        headers.put("Accept", "application/json, application/xml, text/json, text/x-json, text/javascript, text/xml");

        authentication  = apiAccess.doRequest(
                HttpMethod.POST, EvohomeApiConstants.URL_V2_AUTH,
                headers, data, "application/x-www-form-urlencoded", authentication);

        apiAccess.setAuthentication(authentication);

        if (authentication != null) {
            authentication.systemTime = System.currentTimeMillis() / 1000;
        }

        return (authentication != null);
    }

    private boolean authenticateWithUsername() {
        boolean result = false;

        try {
            String credentials = "Username=" + URLEncoder.encode(configuration.username, "UTF-8") + "&"
                               + "Password=" + URLEncoder.encode(configuration.password, "UTF-8");
            result = authenticate(credentials, "password");
        } catch (UnsupportedEncodingException e) {
            logger.error("Credential conversion failed", e);
        }

        return result;
    }

    private boolean authenticateWithToken(String accessToken){
        String credentials = "refresh_token=" + accessToken;
        return authenticate(credentials, "refresh_token");
    }

    private void updateAuthentication() {
        Authentication authentication = apiAccess.getAuthentication();
        if (authentication == null) {
            authenticateWithUsername();
        } else {
            // Compare current time to the expiration time - four intervals for slack
            long currentTime = System.currentTimeMillis() / 1000;
            long expiration  = authentication.systemTime + authentication.expiresIn;
            expiration -= 4 * (configuration.refreshInterval / 1000);

            // Update the access token just before it expires, but fall back to username and password
            // when it fails (i.e. refresh token had been invalidated)
            if (currentTime > expiration) {
                authenticateWithToken(authentication.refreshToken);
                if (apiAccess.getAuthentication() == null) {
                    authenticateWithUsername();
                }
            }
        }
    }

    private Map<String, ControlSystemAndStatus> populateCache() throws NullPointerException {
        Map<String, ControlSystemAndStatus> map = new HashMap<>();

        // Add metadata to the map
        for (Location location : locations) {
            for (Gateway gateway : location.gateways) {
                for (TemperatureControlSystem system: gateway.temperatureControlSystems) {
                    ControlSystemAndStatus status = new ControlSystemAndStatus();
                    status.controlSystem = system;
                    map.put(system.systemId, status);
                }
            }
        }

        return map;
    }

    private void updateCache() throws NullPointerException {
        // Add all statuses to the map
        for (LocationStatus location : locationsStatus) {
            for (GatewayStatus gateway : location.gateways) {
                for (TemperatureControlSystemStatus system: gateway.temperatureControlSystems) {
                    ControlSystemAndStatus status = controlSystemCache.get(system.systemId);
                    if (status != null) {
                        status.controlSystemStatus = system;
                        controlSystemCache.put(system.systemId, status);
                    }
                }
            }
        }
    }

    @Override
    public void logout() {
        close();
    }

    @Override
    public void update() {
        updateAuthentication();
        locationsStatus = requestLocationsStatus();
        updateCache();
    }

    @Override
    public DataModelResponse[] getData() {
        return new DataModelResponse[0];
    }

    @Override
    public ControlSystem[] getControlSystems() {
        ArrayList<ControlSystem> result = new ArrayList<>();
        for (ControlSystemAndStatus item : controlSystemCache.values()) {
            result.add(new ControlSystemV2(apiAccess, item.controlSystem, item.controlSystemStatus));
        }

        return result.toArray(new ControlSystem[result.size()]);
    }

    @Override
    public ControlSystem getControlSystem(String id) {
        for (ControlSystem controlSystem : getControlSystems()) {
            if (controlSystem.getId().equals(id)) {
                return controlSystem;
            }
        }

        return null;
    }

    @Override
    public ZoneStatus getHeatingZone(String locationId, String zoneId) {
        for(GatewayStatus gatewayStatus : getGateways()){
            for(TemperatureControlSystemStatus temperatureControlSystem : gatewayStatus.temperatureControlSystems){
                if(locationId.equals(temperatureControlSystem.systemId)){
                    for(ZoneStatus zone : temperatureControlSystem.zones){
                        if(zoneId.equals(zone.zoneId)){
                            return zone;
                        }
                    }
                }
            }
        }

        return null;
    }

    public void setHeatingSetpoint(String zoneId, double temp, LocalDateTime until) {
        String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_HEAT_SETPOINT;

        url = String.format(url, zoneId);
        HeatSetPoint setPoint = new HeatSetPoint();
        setPoint.heatSetpointValue = temp;
        if( until == null )
            setPoint.setpointMode = SetpointMode.PermanentOverride;
        else {
            setPoint.setpointMode = SetpointMode.TemporaryOverride;
            setPoint.setTimeUntil(until);
        }

        apiAccess.doAuthenticatedRequest(HttpMethod.PUT, url, null, setPoint, null);

    }

    public Schedule getSchedule(String zoneId) {
        String url = EvohomeApiConstants.URL_V2_BASE + EvohomeApiConstants.URL_V2_SCHEDULE;

        url = String.format(url, "temperatureZone", zoneId);

        Schedule out = new Schedule();
        return apiAccess.doAuthenticatedRequest(HttpMethod.GET, url, null, null, out);
    }

    @Override
    public GatewayStatus[] getGateways() {
        List<GatewayStatus> gateways = new ArrayList<>();

        LocationsStatus myLocationsStatus = getLocationStatus();
        for(LocationStatus myLocationStatus : myLocationsStatus){
            gateways.addAll(myLocationStatus.gateways);
        }

        return gateways.toArray(new GatewayStatus[gateways.size()]);
    }

}
