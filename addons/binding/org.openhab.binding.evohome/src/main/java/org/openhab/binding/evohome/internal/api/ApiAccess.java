/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.openhab.binding.evohome.internal.api.models.v2.response.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Provides access to (an optionally OAUTH based) API. Makes sure that all the necessary headers are set.
 * @author Jasper van Zuijlen
 *
 */
public class ApiAccess {
    private final Logger logger = LoggerFactory.getLogger(ApiAccess.class);
    private final HttpClient httpClient;

    private Authentication authenticationData;
    private String applicationId;

    public ApiAccess(HttpClient httpClient){
        this.httpClient = httpClient;
    }

    /**
     * Sets the authentication details on the type
     * @param authentication The authentication details to apply
     */
    public void setAuthentication(Authentication authentication) {
        authenticationData = authentication;
    }

    /**
     * Gets the current authentication details of the type
     * @return The current authentication details
     */
    public Authentication getAuthentication() {
        return authenticationData;
    }

    /**
     * Sets the application id on the type
     * @param applicationId The application id to apply
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Issues an HTTP request on the API's URL. Makes sure that the request is correctly formatted.
     * @param method The HTTP method to use (POST, GET, ...)
     * @param url The URL to query
     * @param headers The optional additional headers to apply, can be null
     * @param requestData The optional request data to use, can be null
     * @param contentType The content type to use with the request data. Required when using requestData
     * @param out An instance of the return type, ignored when value is null
     * @return The result of the request or null
     */
    @SuppressWarnings("unchecked")
    public <TOut> TOut doRequest(HttpMethod method, String url, Map<String, String> headers, String requestData,
            String contentType, TOut out) {

        logger.debug("Requesting: [{}]", url);

        try {
            Request request = httpClient.newRequest(url).method(method);

            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.header(header.getKey(), header.getValue());
                }
            }

            if (requestData != null) {
                request.content(new StringContentProvider(requestData), contentType);
            }

            ContentResponse response = request.send();

            logger.debug("Response: {}\n{}\n{}", response, response.getHeaders(),
                    response.getContentAsString());

            if ((response.getStatus() == HttpStatus.OK_200) || (response.getStatus() == HttpStatus.ACCEPTED_202)) {
                String reply = response.getContentAsString();

                if (out != null) {
                    out = (TOut) new Gson().fromJson(reply, out.getClass());
                }
            }
            else
            {
                out = null;
            }
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            logger.error("Error in handling request", e);
        } catch (Exception e) {
            logger.error("Generic error in handling request", e);
        }

        return out;
    }

    /**
     * Issues an HTTP request on the API's URL, using an object that is serialized to JSON as input.
     * Makes sure that the request is correctly formatted.*
     * @param method The HTTP method to use (POST, GET, ...)
     * @param url The URL to query
     * @param headers The optional additional headers to apply, can be null
     * @param requestContainer The object to use as JSON data for the request
     * @param out An instance of the return type, ignored when value is null
     * @return The result of the request or null
     */
    @SuppressWarnings("unchecked")
    public <TIn, TOut> TOut doRequest(HttpMethod method, String url, Map<String, String> headers, TIn requestContainer,
            TOut out) {

        String json = null;
        if (requestContainer != null) {
            Gson gson = new GsonBuilder().create();
            json = gson.toJson(requestContainer);
        }

        return doRequest(method, url, headers, json, "application/json", out);
    }

    /**
     * Issues an HTTP request on the API's URL, using an object that is serialized to JSON as input and
     * using the authentication applied to the type.
     * Makes sure that the request is correctly formatted.*
     * @param method The HTTP method to use (POST, GET, ...)
     * @param url The URL to query
     * @param headers The optional additional headers to apply, can be null
     * @param requestContainer The object to use as JSON data for the request
     * @param out An instance of the return type, ignored when value is null
     * @return The result of the request or null
     */
    public <TIn, TOut> TOut doAuthenticatedRequest(HttpMethod method, String url, Map<String, String> headers,
            TIn requestContainer, TOut out) {

        if (authenticationData != null) {
            if (headers == null) {
                headers = new HashMap<>();
            }

            headers.put("Authorization", "bearer " + authenticationData.accessToken);
            headers.put("applicationId", applicationId);
            headers.put("Accept", "application/json, application/xml, text/json, text/x-json, text/javascript, text/xml");
        }

        return doRequest(method, url, headers, requestContainer, out);
    }
}
