package com.bank;

import com.bank.model.Account;
import com.bank.model.Payment;
import com.bank.model.Office;
import com.bank.service.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class SwiftController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private ComplianceService complianceService;

    @Autowired
    private RoutingService routingService;

    @Autowired
    private SwiftMessageParser swiftMessageParser;


    @GetMapping("/health")
    public String healthCheck() {
        return "System is up!";
    }

    @PostMapping("/process-pacs008")
    public Payment processPacs008(@RequestBody Map<String, String> pacs008Data) {
        // 使用现有逻辑生成 XML 并解析
        Payment payment = paymentService.processAndSave(pacs008Data, "pacs.008");
        return processPayment(payment);
            
        // String xml = pacs008.message();
        // return processPayment(
        //         pacs008Data.get("msgId"),
        //         pacs008Data.get("dbtrNm"),
        //         pacs008Data.get("cdtrNm"),
        //         instdAmt,
        //         instdCcy,
        //         pacs008Data.get("creDtTm"),
        //         pacs008Data.get("dbtrAgtBIC"),
        //         pacs008Data.get("cdtrAgtBIC"),
        //         pacs008Data.get("dbtrAcctId"),
        //         pacs008Data.get("cdtrAcctId"),
        //         xml
        // );
    }

    @PostMapping("/process-pacs009")
    public Payment processPacs009(@RequestBody Map<String, String> pacs009Data) {
        Payment payment = paymentService.processAndSave(pacs009Data, "pacs.009");
        return processPayment(payment);
        
        // String xml = pacs009.message();
        // return paymentService.processAndSave(xml); // 使用 parser 处理
        // return processPayment(
        //         pacs009Data.get("msgId"),
        //         pacs009Data.get("dbtrNm"),
        //         pacs009Data.get("cdtrNm"),
        //         intrBkAmt,
        //         intrBkCcy,
        //         pacs009Data.get("creDtTm"),
        //         pacs009Data.get("dbtrAgtBIC"),
        //         pacs009Data.get("cdtrAgtBIC"),
        //         pacs009Data.get("dbtrAgtBIC") + "_001",
        //         pacs009Data.get("cdtrAgtBIC") + "_001",
        //         xml
        // );
    }

 private Payment processPayment(Payment payment) {
        payment.setStatus("PENDING");
        paymentService.save(payment);

        // 检查合规性
        if (!complianceService.isCompliant(payment)) {
            payment.setStatus("FAILED - Non-compliant");
            paymentService.save(payment);
            return payment;
        }

        // 确定账户并检查余额
        Account debitAcct = accountService.findById(payment.getDebitAccount());
        Account creditAcct = accountService.findById(payment.getCreditAccount());
        if (debitAcct == null || creditAcct == null) {
            payment.setStatus("FAILED - Invalid account");
            paymentService.save(payment);
            return payment;
        }

        if (debitAcct.getBalance() < payment.getAmount()) {
            payment.setStatus("FAILED - Insufficient balance");
            paymentService.save(payment);
            return payment;
        }

        // 更新账户余额
        debitAcct.setBalance(debitAcct.getBalance() - payment.getAmount());
        creditAcct.setBalance(creditAcct.getBalance() + payment.getAmount());
        accountService.save(debitAcct);
        accountService.save(creditAcct);

        // 路由支付
        routingService.routePayment(payment);

        payment.setStatus("SUCCESS");
        paymentService.save(payment);
        return payment;
    }

    // Office 相关端点
    @GetMapping("/offices")
    public ResponseEntity<List<Office>> getAllOffices() {
        List<Office> offices = officeService.getAllOffices();
        return ResponseEntity.ok(offices);
    }

    @PostMapping("/offices")
    public ResponseEntity<Map<String, Object>> addOffice(@RequestBody Office office) {
        Map<String, Object> response = officeService.addOffice(office);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/offices/{officeId}")
    public ResponseEntity<Map<String, Object>> updateOffice(@PathVariable String officeId, @RequestBody Office office) {
        Map<String, Object> response = officeService.updateOffice(officeId, office);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/offices/{officeId}")
    public ResponseEntity<Map<String, Object>> deleteOffice(@PathVariable String officeId) {
        Map<String, Object> response = officeService.deleteOffice(officeId);
        return ResponseEntity.ok(response);
    }
}