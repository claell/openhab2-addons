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
import java.util.Date;

/**
 * Response model for the heat set point
 * @author Neil Renaud
 *
 */
public class HeatSetPoint {
    private BigDecimal value;
    private String status;
    private Date nextTime;

    @Override
    public String toString() {
        return "value[" + value + "] status[" + status + "] nextTime[" + nextTime + "]";
    }

    public Date getNextTime() {
        return nextTime;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getValue() {
        return value;
    }
}
