package com.example.issproject.service;


public interface ISSLocationService {
    String getLocationOfISS();
    String getTrackOfISS() throws InterruptedException;
}
