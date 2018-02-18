/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.evohome.internal.api.models.v2.response;

import com.google.gson.annotations.SerializedName;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Response for daily schedule.
 * @author Nigel Magnay
 */
public class DailySchedule {
    @SerializedName("dayOfWeek")
    public Day dayOfWeek;

    @SerializedName("switchpoints")
    public List<Switchpoint> switchpoints;

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.valueOf(dayOfWeek.toString().toUpperCase());
    }

    public List<LocalDateTime> getUpcomingSwitchpointDates() {
        List<LocalDateTime> switches = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();

        DayOfWeek schedDay = getDayOfWeek();
        DayOfWeek today    = date.getDayOfWeek();

        int dayDiff = today.getValue() - schedDay.getValue();
        if( dayDiff < 0)
            dayDiff += 7;

        // Look at all the switch points
        for(Switchpoint sp : switchpoints) {
            LocalDateTime value = LocalDateTime.of(date, sp.getTime());
            value = value.plusDays(dayDiff);
            if( value.isBefore(now))
                value = value.plusDays(7);
            switches.add(value);

        }

        // Sort to time order
        Collections.sort(switches);

        return switches;
    }
}

