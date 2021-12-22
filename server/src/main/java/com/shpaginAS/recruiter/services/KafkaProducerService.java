package com.shpaginAS.recruiter.services;

import com.shpaginAS.recruiter.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> emailTemplate;

    public void produce(Message message) {
        System.out.println("Producing the message: " + message);
        kafkaTemplate.send("messages", message);
    }

    public void sendEmailForAcceptRegistration(String email) {
        System.out.println("Отправка email для регистрации ---> " + email);
        emailTemplate.send("emailRegistration", email);
    }

}
