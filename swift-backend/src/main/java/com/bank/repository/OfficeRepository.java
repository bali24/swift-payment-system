package com.bank.repository;

import com.bank.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, String> {
}