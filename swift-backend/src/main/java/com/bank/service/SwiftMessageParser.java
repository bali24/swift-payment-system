package com.bank.service;

import com.bank.model.Payment;
import com.prowidesoftware.swift.model.mx.MxPacs00800110;
import com.prowidesoftware.swift.model.mx.MxPacs00900110;
import com.prowidesoftware.swift.model.mx.dic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigDecimal;

/**
 * SwiftMessageParser 用于解析 SWIFT 消息（pacs.008 和 pacs.009）的 XML 格式，
 * 并将解析结果映射到 Payment 对象。
 */
@Service
public class SwiftMessageParser {

    /**
     * 从 XML 字符串解析 SWIFT 消息并返回 Payment 对象。
     * @param xml SWIFT 消息的 XML 字符串
     * @return 解析后的 Payment 对象
     * @throws JAXBException 如果 XML 解析失败
     */
    public Payment parse(String xml) throws JAXBException {
        if (xml == null || xml.trim().isEmpty()) {
            throw new IllegalArgumentException("XML message cannot be null or empty");
        }

        // 尝试解析为 pacs.008 或 pacs.009
        Payment payment = tryParsePacs008(xml);
        if (payment == null) {
            payment = tryParsePacs009(xml);
        }

        if (payment == null) {
            throw new IllegalArgumentException("Unsupported SWIFT message format");
        }

        return payment;
    }

    /**
     * 尝试解析 pacs.008 消息。
     */
    private Payment tryParsePacs008(String xml) throws JAXBException {
        try {
            MxPacs00800110 pacs008 = MxPacs00800110.parse(xml);
            FIToFICustomerCreditTransferV10 fiToFICstmrCdtTrf = pacs008.getFIToFICstmrCdtTrf();
            if (fiToFICstmrCdtTrf == null) {
                return null;
            }

            Payment payment = new Payment();
            GroupHeader96 grpHdr = fiToFICstmrCdtTrf.getGrpHdr();
            if (grpHdr != null) {
                payment.setTransactionId(grpHdr.getMsgId());
                if (grpHdr.getCreDtTm() != null) {
                    payment.setTransactionDate(grpHdr.getCreDtTm().toString());
                }
                ActiveCurrencyAndAmount ttlAmt = grpHdr.getTtlIntrBkSttlmAmt();
                if (ttlAmt != null) {
                    payment.setAmount(ttlAmt.getValue().doubleValue());
                    payment.setCurrency(ttlAmt.getCcy());
                }
            }

            // 假设处理第一笔交易
            if (!fiToFICstmrCdtTrf.getCdtTrfTxInf().isEmpty()) {
                CreditTransferTransaction50 txInf = fiToFICstmrCdtTrf.getCdtTrfTxInf().get(0);
                populatePaymentFromTxInf(txInf, payment);
            }

            payment.setXmlMessage(xml);
            return payment;
        } catch (Exception e) {
            return null; // 如果不是 pacs.008，返回 null
        }
    }

    /**
     * 尝试解析 pacs.009 消息。
     */
    private Payment tryParsePacs009(String xml) throws JAXBException {
        try {
            MxPacs00900110 pacs009 = MxPacs00900110.parse(xml);
            FinancialInstitutionCreditTransferV10 fiCdtTrf = pacs009.getFICdtTrf();
            if (fiCdtTrf == null) {
                return null;
            }

            Payment payment = new Payment();
            GroupHeader96 grpHdr = fiCdtTrf.getGrpHdr();
            if (grpHdr != null) {
                payment.setTransactionId(grpHdr.getMsgId());
                if (grpHdr.getCreDtTm() != null) {
                    payment.setTransactionDate(grpHdr.getCreDtTm().toString());
                }
                ActiveCurrencyAndAmount ttlAmt = grpHdr.getTtlIntrBkSttlmAmt();
                if (ttlAmt != null) {
                    payment.setAmount(ttlAmt.getValue().doubleValue());
                    payment.setCurrency(ttlAmt.getCcy());
                }
            }

            // 假设处理第一笔交易
            if (!fiCdtTrf.getCdtTrfTxInf().isEmpty()) {
                CreditTransferTransaction56 txInf = fiCdtTrf.getCdtTrfTxInf().get(0);
                populatePaymentFromTxInf(txInf, payment);
            }

            payment.setXmlMessage(xml);
            return payment;
        } catch (Exception e) {
            return null; // 如果不是 pacs.009，返回 null
        }
    }

    /**
     * 从 CreditTransferTransaction50 填充 Payment 对象字段。
     */
    private void populatePaymentFromTxInf(CreditTransferTransaction50 txInf, Payment payment) {
        if (txInf.getPmtId() != null) {
            payment.setTransactionId(txInf.getPmtId().getEndToEndId()); // 使用 EndToEndId 作为交易 ID
        }

        if (txInf.getInstdAmt() != null) {
            payment.setAmount(txInf.getInstdAmt().getValue().doubleValue());
            payment.setCurrency(txInf.getInstdAmt().getCcy());
        }

        if (txInf.getDbtr() != null) {
            payment.setPayer(txInf.getDbtr().getNm());
        }

        if (txInf.getDbtrAcct() != null && txInf.getDbtrAcct().getId() != null &&
                txInf.getDbtrAcct().getId().getOthr() != null) {
            payment.setDebitAccount(txInf.getDbtrAcct().getId().getOthr().getId());
        }

        if (txInf.getDbtrAgt() != null && txInf.getDbtrAgt().getFinInstnId() != null) {
            payment.setSenderBIC(txInf.getDbtrAgt().getFinInstnId().getBICFI());
        }

        if (txInf.getCdtr() != null) {
            payment.setPayee(txInf.getCdtr().getNm());
        }

        if (txInf.getCdtrAcct() != null && txInf.getCdtrAcct().getId() != null &&
                txInf.getCdtrAcct().getId().getOthr() != null) {
            payment.setCreditAccount(txInf.getCdtrAcct().getId().getOthr().getId());
        }

        if (txInf.getCdtrAgt() != null && txInf.getCdtrAgt().getFinInstnId() != null) {
            payment.setReceiverBIC(txInf.getCdtrAgt().getFinInstnId().getBICFI());
        }
    }

    /**
     * 从 CreditTransferTransaction56 填充 Payment 对象字段。
     */
    private void populatePaymentFromTxInf(CreditTransferTransaction56 txInf, Payment payment) {
        if (txInf.getPmtId() != null) {
            payment.setTransactionId(txInf.getPmtId().getEndToEndId());
        }

        if (txInf.getIntrBkSttlmAmt() != null) {
            payment.setAmount(txInf.getIntrBkSttlmAmt().getValue().doubleValue());
            payment.setCurrency(txInf.getIntrBkSttlmAmt().getCcy());
        }

        if (txInf.getDbtr() != null && txInf.getDbtr().getFinInstnId() != null) {
            payment.setPayer(txInf.getDbtr().getFinInstnId().getNm());
            payment.setSenderBIC(txInf.getDbtr().getFinInstnId().getBICFI());
        }

        if (txInf.getDbtrAgt() != null && txInf.getDbtrAgt().getFinInstnId() != null) {
            payment.setSenderBIC(txInf.getDbtrAgt().getFinInstnId().getBICFI());
        }

        if (txInf.getCdtr() != null && txInf.getCdtr().getFinInstnId() != null) {
            payment.setPayee(txInf.getCdtr().getFinInstnId().getNm());
            payment.setReceiverBIC(txInf.getCdtr().getFinInstnId().getBICFI());
        }

        if (txInf.getCdtrAgt() != null && txInf.getCdtrAgt().getFinInstnId() != null) {
            payment.setReceiverBIC(txInf.getCdtrAgt().getFinInstnId().getBICFI());
        }

        // pacs.009 不直接提供账户信息，假设使用 BIC 派生账户 ID
        payment.setDebitAccount(payment.getSenderBIC() + "_001");
        payment.setCreditAccount(payment.getReceiverBIC() + "_001");
    }
}