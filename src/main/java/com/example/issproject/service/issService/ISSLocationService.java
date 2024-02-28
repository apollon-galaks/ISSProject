package com.example.issproject.service.issService;


public interface ISSLocationService {
    String getLocationOfISS();
    String getTrackOfISS() throws InterruptedException;
}
