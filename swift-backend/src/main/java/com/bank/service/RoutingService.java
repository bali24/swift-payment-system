package com.bank.service;

import com.bank.model.Payment;
import com.bank.repository.PaymentRepository;
import com.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class RoutingService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void routePayment(Payment payment) {
        // 模拟路由：发送到 Kafka 主题
        kafkaTemplate.send("swift-routed-" + payment.getReceiverBIC(), payment.getTransactionId(), payment.toString());
    }
}