package com.example.issproject.controller;

import com.example.issproject.service.ISSLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iss")
public class ISSController {

    private final ISSLocationService issLocationService;

    @Autowired
    public ISSController(ISSLocationService issLocationService) {
        this.issLocationService = issLocationService;
    }


    @GetMapping("/location")
    public String getLocationOfISS(){
        return issLocationService.getLocationOfISS();
    }

    @GetMapping("/track")
    public String getTrackOfISS() throws InterruptedException {
        return issLocationService.getTrackOfISS();
    }

}
