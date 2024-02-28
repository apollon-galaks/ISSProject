package com.example.issproject.service.subscriberService;

import com.example.issproject.entity.SubscriberEntity;
import com.example.issproject.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SubscriberServiceImpl implements SubscriberService{

    @Autowired
    private SubscriberRepository subscriberRepository;
    @Override
    public SubscriberEntity saveSubscriberInfo(String userEmail, String status) {
        SubscriberEntity user = new SubscriberEntity();
        user.setInsert_date(LocalDateTime.now());
        user.setSubscriber_email(userEmail);
        user.setSubscription_status(status);

        return subscriberRepository.save(user);
    }
}
