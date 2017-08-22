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
 * Response model for the weather
 * @author Neil Renaud
 *
 */
public class Weather {

    private String condition;
    private BigDecimal temperature;
    private String units;
    private BigDecimal humidity;
    private String phrase;

    public String getCondition() {
        return condition;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public String getPhrase() {
        return phrase;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public String getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return "Condition[" + condition + "] Temperature[" + temperature + "] units[" + units + "] humidity[" + humidity
                + "] phrase[" + phrase + "]";
    }
}
