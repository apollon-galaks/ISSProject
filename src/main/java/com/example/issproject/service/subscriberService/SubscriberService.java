package com.example.issproject.service.subscriberService;

import com.example.issproject.entity.SubscriberEntity;

public interface SubscriberService {
    SubscriberEntity saveSubscriberInfo(String userEmail, String status);
}
