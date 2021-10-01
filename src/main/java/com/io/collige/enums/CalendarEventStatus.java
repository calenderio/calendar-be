/*
 * @author : Oguz Kahraman
 * @since : 28.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.enums;

public enum CalendarEventStatus {

    ACCEPTED,
    TENTATIVE,
    NONE,
    CANCELLED;

    public static CalendarEventStatus getForGoogle(String status) {
        if ("confirmed".equals(status)) {
            return ACCEPTED;
        } else if ("tentative".equals(status)) {
            return TENTATIVE;
        } else if ("cancelled".equals(status)) {
            return TENTATIVE;
        }
        return NONE;
    }

    public static CalendarEventStatus getForMicrosoft(String status) {
        if ("organizer".equals(status) || "accepted".equals(status)) {
            return ACCEPTED;
        } else if ("tentativelyAccepted".equals(status)) {
            return TENTATIVE;
        } else if ("declined".equals(status)) {
            return CANCELLED;
        }
        return CANCELLED;
    }

}
