package com.bank.service;

import com.bank.model.Payment;
import com.bank.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplianceService {
    public boolean isCompliant(Payment payment) {
        // 检查必填字段
        if (payment.getTransactionId() == null || payment.getSenderBIC() == null ||
            payment.getReceiverBIC() == null || payment.getAmount() <= 0) {
            return false;
        }
        // 检查 BIC 格式（8 或 11 位）
        return payment.getSenderBIC().matches("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$") &&
               payment.getReceiverBIC().matches("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");
    }
}