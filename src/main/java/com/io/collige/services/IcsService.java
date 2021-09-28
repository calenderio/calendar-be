package com.io.collige.services;

import com.io.collige.models.requests.meet.MeetingRequest;

import java.io.IOException;

public interface IcsService {

    byte[] writeIcsFileToByteArray(MeetingRequest request) throws IOException;

}
