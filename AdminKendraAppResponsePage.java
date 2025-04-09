package com.example.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BulkUpdateService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void executeQueries(String rawQuery) {
        if (rawQuery == null || rawQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Query is empty or null");
        }

        String[] queries = rawQuery.split(";");
        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                entityManager.createNativeQuery(query.trim()).executeUpdate();
            }
        }
    }
}
