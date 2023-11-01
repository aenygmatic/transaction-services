package com.virtualbank.transaction.analytics.service.model;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@UtilityClass
public class IdGenerator {

    public static String idForNow(UUID userId) {
        LocalDate now = LocalDate.now();
        return idFor(userId, now.getYear(), now.getMonthValue());
    }

    public static String idFor(UUID userId, int year, int month) {
        return userId.toString() + ":" + year + "-" + month;
    }

    public static String idFor(UUID userId, ZonedDateTime time) {
        return idFor(userId, time.getYear(), time.getMonthValue());
    }
}
