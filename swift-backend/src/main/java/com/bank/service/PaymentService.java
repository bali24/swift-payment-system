package com.bank.service;

import com.bank.model.Payment;
import com.bank.repository.*;
import com.prowidesoftware.swift.model.mx.MxPacs00800110;
import com.prowidesoftware.swift.model.mx.MxPacs00900110;
import com.prowidesoftware.swift.model.mx.dic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private SwiftMessageParser parser;

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    public Payment processAndSave(Map<String, String> data, String messageType) {
            // 生成 SWIFT 消息 XML
            String xml = generateSwiftMessage(data, messageType);
            try {
                Payment payment = parser.parse(xml);
                payment.setStatus("PENDING");
                save(payment);
                return payment;
            } catch (Exception e) {
                Payment payment = new Payment();
                payment.setXmlMessage(xml);
                payment.setStatus("FAILED - Parsing error: " + e.getMessage());
                save(payment);
                return payment;
            }
    }

    private String generateSwiftMessage(Map<String, String> data, String messageType) {
        if ("pacs.008".equals(messageType)) {
            MxPacs00800110 pacs008 = new MxPacs00800110();
            // 使用 SwiftController 中的逻辑填充 pacs008
            
            FIToFICustomerCreditTransferV10 fiToFICstmrCdtTrf = pacs008.getFIToFICstmrCdtTrf();
            if (fiToFICstmrCdtTrf == null) {
                fiToFICstmrCdtTrf = new FIToFICustomerCreditTransferV10();
                pacs008.setFIToFICstmrCdtTrf(fiToFICstmrCdtTrf);
            }
            GroupHeader96 grpHdr = new GroupHeader96();
            fiToFICstmrCdtTrf.setGrpHdr(grpHdr);

            // 设置 GrpHdr
            grpHdr.setMsgId(data.get("msgId"));
            LocalDateTime localDateTime = LocalDateTime.parse(data.get("creDtTm"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
            grpHdr.setCreDtTm(offsetDateTime);
            grpHdr.setNbOfTxs(data.get("nbOfTxs"));

            String ttlAmt = data.get("ttlIntrBkSttlmAmt");
            String ttlCcy = data.get("ttlIntrBkSttlmCcy");
            if (ttlAmt != null && !ttlAmt.isEmpty() && ttlCcy != null && !ttlCcy.isEmpty()) {
                ActiveCurrencyAndAmount ttlAmount = new ActiveCurrencyAndAmount();
                ttlAmount.setValue(new BigDecimal(ttlAmt));
                ttlAmount.setCcy(ttlCcy);
                grpHdr.setTtlIntrBkSttlmAmt(ttlAmount);
            }

            String sttlmMtd = data.get("sttlmMtd");
            if (sttlmMtd != null && !sttlmMtd.isEmpty()) {
                SettlementInstruction11 sttlmInf = new SettlementInstruction11();
                sttlmInf.setSttlmMtd(SettlementMethod1Code.valueOf(sttlmMtd.toUpperCase()));
                grpHdr.setSttlmInf(sttlmInf);
            }

            // 添加 CdtTrfTxInf
            CreditTransferTransaction50 cdtTrfTxInf = new CreditTransferTransaction50();
            fiToFICstmrCdtTrf.getCdtTrfTxInf().add(cdtTrfTxInf);

            PaymentIdentification13 pmtId = new PaymentIdentification13();
            pmtId.setInstrId(data.get("pmtIdInstrId"));
            pmtId.setEndToEndId(data.get("pmtIdEndToEndId"));
            cdtTrfTxInf.setPmtId(pmtId);

            String svcLvlCd = data.get("pmtTpInfSvcLvlCd");
            if (svcLvlCd != null && !svcLvlCd.isEmpty()) {
                PaymentTypeInformation28 pmtTpInf = new PaymentTypeInformation28();
                ServiceLevel8Choice svcLvl = new ServiceLevel8Choice();
                svcLvl.setCd(svcLvlCd);
                pmtTpInf.getSvcLvl().add(svcLvl); // 使用 addSvcLvl
                cdtTrfTxInf.setPmtTpInf(pmtTpInf);
            }

            String instdAmt = data.get("instdAmt");
            String instdCcy = data.get("instdAmtCcy");
            if (instdAmt != null && !instdAmt.isEmpty() && instdCcy != null && !instdCcy.isEmpty()) {
                ActiveOrHistoricCurrencyAndAmount amount = new ActiveOrHistoricCurrencyAndAmount();
                amount.setValue(new BigDecimal(instdAmt));
                amount.setCcy(instdCcy);
                cdtTrfTxInf.setInstdAmt(amount);
            }

            String chrgBr = data.get("chrgBr");
            if (chrgBr != null && !chrgBr.isEmpty()) {
                cdtTrfTxInf.setChrgBr(ChargeBearerType1Code.valueOf(chrgBr.toUpperCase()));
            }

            // 设置 Dbtr
            PartyIdentification135 dbtr = new PartyIdentification135();
            dbtr.setNm(data.get("dbtrNm"));
            cdtTrfTxInf.setDbtr(dbtr);

            CashAccount40 dbtrAcct = new CashAccount40();
            GenericAccountIdentification1 dbtrAcctId = new GenericAccountIdentification1();
            dbtrAcctId.setId(data.get("dbtrAcctId"));
            dbtrAcct.getId().setOthr(dbtrAcctId);
            cdtTrfTxInf.setDbtrAcct(dbtrAcct);

            BranchAndFinancialInstitutionIdentification6 dbtrAgt = new BranchAndFinancialInstitutionIdentification6();
            FinancialInstitutionIdentification18 finInstnId = new FinancialInstitutionIdentification18();
            finInstnId.setBICFI(data.get("dbtrAgtBIC"));
            dbtrAgt.setFinInstnId(finInstnId);
            cdtTrfTxInf.setDbtrAgt(dbtrAgt);

            // 设置 Cdtr
            PartyIdentification135 cdtr = new PartyIdentification135();
            cdtr.setNm(data.get("cdtrNm"));
            cdtTrfTxInf.setCdtr(cdtr);

            CashAccount40 cdtrAcct = new CashAccount40();
            GenericAccountIdentification1 cdtrAcctId = new GenericAccountIdentification1();
            cdtrAcctId.setId(data.get("cdtrAcctId"));
            cdtrAcct.getId().setOthr(cdtrAcctId);
            cdtTrfTxInf.setCdtrAcct(cdtrAcct);

            BranchAndFinancialInstitutionIdentification6 cdtrAgt = new BranchAndFinancialInstitutionIdentification6();
            FinancialInstitutionIdentification18 cdtrFinInstnId = new FinancialInstitutionIdentification18();
            cdtrFinInstnId.setBICFI(data.get("cdtrAgtBIC"));
            cdtrAgt.setFinInstnId(cdtrFinInstnId);
            cdtTrfTxInf.setCdtrAgt(cdtrAgt);

            String rmtInfUstrd = data.get("rmtInfUstrd");
            if (rmtInfUstrd != null && !rmtInfUstrd.isEmpty()) {
                RemittanceInformation21 rmtInf = new RemittanceInformation21();
                rmtInf.getUstrd().add(rmtInfUstrd);
                cdtTrfTxInf.setRmtInf(rmtInf);
            }
            return pacs008.message();
        } else {
            MxPacs00900110 pacs009 = new MxPacs00900110();
            // 使用 SwiftController 中的逻辑填充 pacs009
            FinancialInstitutionCreditTransferV10 fiCdtTrf = pacs009.getFICdtTrf();
            GroupHeader96 grpHdr = new GroupHeader96();
            fiCdtTrf.setGrpHdr(grpHdr);

            // 设置 GrpHdr
            grpHdr.setMsgId(data.get("msgId"));
            LocalDateTime localDateTime = LocalDateTime.parse(data.get("creDtTm"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
            grpHdr.setCreDtTm(offsetDateTime);
            grpHdr.setNbOfTxs(data.get("nbOfTxs"));

            String ttlAmt = data.get("ttlIntrBkSttlmAmt");
            String ttlCcy = data.get("ttlIntrBkSttlmCcy");
            if (ttlAmt != null && !ttlAmt.isEmpty() && ttlCcy != null && !ttlCcy.isEmpty()) {
                ActiveCurrencyAndAmount ttlAmount = new ActiveCurrencyAndAmount();
                ttlAmount.setValue(new BigDecimal(ttlAmt));
                ttlAmount.setCcy(ttlCcy);
                grpHdr.setTtlIntrBkSttlmAmt(ttlAmount);
            }

            String sttlmMtd = data.get("sttlmMtd");
            if (sttlmMtd != null && !sttlmMtd.isEmpty()) {
                SettlementInstruction11 sttlmInf = new SettlementInstruction11();
                sttlmInf.setSttlmMtd(SettlementMethod1Code.valueOf(sttlmMtd.toUpperCase()));
                grpHdr.setSttlmInf(sttlmInf);
            }

            String intrBkSttlmDt = data.get("intrBkSttlmDt");
            if (intrBkSttlmDt != null && !intrBkSttlmDt.isEmpty()) {
                grpHdr.setIntrBkSttlmDt(LocalDate.parse(intrBkSttlmDt, DateTimeFormatter.ISO_LOCAL_DATE));
            }

            // 添加 CdtTrfTxInf
            CreditTransferTransaction56 cdtTrfTxInf = new CreditTransferTransaction56();
            fiCdtTrf.getCdtTrfTxInf().add(cdtTrfTxInf);

            PaymentIdentification13 pmtId = new PaymentIdentification13();
            pmtId.setInstrId(data.get("pmtIdInstrId"));
            pmtId.setEndToEndId(data.get("pmtIdEndToEndId"));
            cdtTrfTxInf.setPmtId(pmtId);

            String intrBkAmt = data.get("intrBkSttlmAmt");
            String intrBkCcy = data.get("intrBkSttlmCcy");
            if (intrBkAmt != null && !intrBkAmt.isEmpty() && intrBkCcy != null && !intrBkCcy.isEmpty()) {
                ActiveCurrencyAndAmount amount = new ActiveCurrencyAndAmount();
                amount.setValue(new BigDecimal(intrBkAmt));
                amount.setCcy(intrBkCcy);
                cdtTrfTxInf.setIntrBkSttlmAmt(amount);
            }

            String chrgBr = data.get("chrgBr");
            if (chrgBr != null && !chrgBr.isEmpty()) {
                // 假设 setChargeBearer 存在，若不存在可注释
                // cdtTrfTxInf.setChargeBearer(ChargeBearerType1Code.valueOf(chrgBr.toUpperCase()));
            }

            // 设置 Dbtr
            BranchAndFinancialInstitutionIdentification6 dbtr = new BranchAndFinancialInstitutionIdentification6();
            FinancialInstitutionIdentification18 dbtrFinInstnId = new FinancialInstitutionIdentification18();
            dbtrFinInstnId.setBICFI(data.get("dbtrAgtBIC"));
            dbtr.setFinInstnId(dbtrFinInstnId);
            cdtTrfTxInf.setDbtr(dbtr);

            BranchAndFinancialInstitutionIdentification6 dbtrAgt = new BranchAndFinancialInstitutionIdentification6();
            FinancialInstitutionIdentification18 finInstnId = new FinancialInstitutionIdentification18();
            finInstnId.setBICFI(data.get("dbtrAgtBIC"));
            dbtrAgt.setFinInstnId(finInstnId);
            cdtTrfTxInf.setDbtrAgt(dbtrAgt);

            // 设置 Cdtr
            BranchAndFinancialInstitutionIdentification6 cdtr = new BranchAndFinancialInstitutionIdentification6();
            FinancialInstitutionIdentification18 cdtrFinInstnId = new FinancialInstitutionIdentification18();
            cdtrFinInstnId.setBICFI(data.get("cdtrAgtBIC"));
            cdtr.setFinInstnId(cdtrFinInstnId);
            cdtTrfTxInf.setCdtr(cdtr);

            BranchAndFinancialInstitutionIdentification6 cdtrAgt = new BranchAndFinancialInstitutionIdentification6();
            FinancialInstitutionIdentification18 cdtrAgtFinInstnId = new FinancialInstitutionIdentification18();
            cdtrAgtFinInstnId.setBICFI(data.get("cdtrAgtBIC"));
            cdtrAgt.setFinInstnId(cdtrAgtFinInstnId);
            cdtTrfTxInf.setCdtrAgt(cdtrAgt);

            String rmtInfUstrd = data.get("rmtInfUstrd");
            if (rmtInfUstrd != null && !rmtInfUstrd.isEmpty()) {
                RemittanceInformation2 rmtInf = new RemittanceInformation2();
                rmtInf.getUstrd().add(rmtInfUstrd);
                cdtTrfTxInf.setRmtInf(rmtInf);
            }

            return pacs009.message();
        }
    }
}