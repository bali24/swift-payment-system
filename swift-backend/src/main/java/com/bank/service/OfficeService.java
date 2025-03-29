// src/main/java/com/example/swift/service/OfficeService.java
package com.bank.service;

import com.bank.model.Office;
import com.bank.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public Map<String, Object> addOffice(Office office) {
        Office savedOffice = officeRepository.save(office);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("officeId", savedOffice.getOfficeId());
        response.put("officeName", savedOffice.getOfficeName());
        return response;
    }

    public Map<String, Object> updateOffice(String officeId, Office office) {
        Optional<Office> existingOffice = officeRepository.findById(officeId);
        if (existingOffice.isPresent()) {
            office.setOfficeId(officeId);
            officeRepository.save(office);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("updatedId", officeId);
            return response;
        } else {
            throw new RuntimeException("Office not found with id: " + officeId);
        }
    }

    public Map<String, Object> deleteOffice(String officeId) {
        Optional<Office> existingOffice = officeRepository.findById(officeId);
        if (existingOffice.isPresent()) {
            officeRepository.deleteById(officeId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("deletedId", officeId);
            return response;
        } else {
            throw new RuntimeException("Office not found with id: " + officeId);
        }
    }
}