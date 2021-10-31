/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.services;

import com.io.collige.entitites.Meeting;

public interface MeetingLocationService {
    String getLocationLink(Meeting meeting);
}
