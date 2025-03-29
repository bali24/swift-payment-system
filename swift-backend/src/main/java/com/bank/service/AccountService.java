package com.bank.service;

import com.bank.model.Payment;
import com.bank.model.Account;
import com.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account findById(String accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public boolean validateBalance(Payment payment) {
        Optional<Account> debitAccount = accountRepository.findById(payment.getSenderBIC() + "_001"); // 假设账户ID格式
        if (debitAccount.isPresent() && debitAccount.get().getBalance() >= payment.getAmount()) {
            payment.setDebitAccount(debitAccount.get().getAccountId());
            payment.setCreditAccount(payment.getReceiverBIC() + "_001"); // 假设收款账户
            return true;
        }
        return false;
    }

    public void updateBalance(Payment payment) {
        Account debit = accountRepository.findById(payment.getDebitAccount()).get();
        Account credit = accountRepository.findById(payment.getCreditAccount()).get();
        debit.setBalance(debit.getBalance() - payment.getAmount());
        credit.setBalance(credit.getBalance() + payment.getAmount());
        accountRepository.save(debit);
        accountRepository.save(credit);
    }
}