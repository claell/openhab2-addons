/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v1;

import java.math.BigDecimal;

/**
 * Response model for the thermostat
 * @author Neil Renaud
 *
 */
public class Thermostat {

    private String units;
    private BigDecimal indoorTemperature;
    private ChangeableValues changeableValues;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "units[" + units + "] indoorTemperature[" + indoorTemperature + "] changeableValues[" + changeableValues
                + "]";
    }

    public String getUnits() {
        return units;
    }

    public BigDecimal getIndoorTemperature() {
        return indoorTemperature;
    }

    public ChangeableValues getChangeableValues() {
        return changeableValues;
    }
}
