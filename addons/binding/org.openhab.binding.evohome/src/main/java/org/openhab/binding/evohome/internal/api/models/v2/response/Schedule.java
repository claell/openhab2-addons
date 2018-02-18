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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Response for schedule.
 * @author Nigel Magnay
 */
public class Schedule {
    @SerializedName("dailySchedules")
    public List<DailySchedule> dailySchedules;

    public List<LocalDateTime> getUpcomingSwitchpointDates() {
        List<LocalDateTime> switches = new ArrayList<>();
        dailySchedules.forEach(s -> switches.addAll(s.getUpcomingSwitchpointDates()));

        Collections.sort(switches);
        return switches;
    }

}