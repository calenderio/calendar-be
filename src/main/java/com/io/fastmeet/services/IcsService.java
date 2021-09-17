package com.io.fastmeet.services;

import com.io.fastmeet.models.requests.meet.MeetingRequest;

import java.io.IOException;

public interface IcsService {

    byte[] writeIcsFileToByteArray(MeetingRequest request , String filePath) throws IOException;
}
