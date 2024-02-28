package com.example.issproject.controller;

import com.example.issproject.entity.SubscriberEntity;
import com.example.issproject.service.subscriberService.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriber")
@RequiredArgsConstructor
public class SubscriberController {

    @Autowired
    private final SubscriberService subscriberService;

    @PostMapping("/saveUser")
    public SubscriberEntity saveUser(@RequestBody SubscriberEntity user){
        return subscriberService.saveSubscriberInfo(user.getSubscriber_email(), user.getSubscription_status());
    }
}
