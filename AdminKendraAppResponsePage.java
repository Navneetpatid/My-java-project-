package com.hsbc.hap.cer.service.impl;

import com.hsbc.hap.cer.model.QueryResult;
import com.hsbc.hap.cer.service.HapCERService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class HapCERServiceImpl implements HapCERService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<QueryResult> executeQueries(List<String> queries) {
        List<QueryResult> results = new ArrayList<>();

        if (queries == null || queries.isEmpty()) {
            results.add(new QueryResult(null, false, "Query list is empty or null"));
            return results;
        }

        for (String query : queries) {
            String trimmedQuery = query.trim();
            if (trimmedQuery.isEmpty()) continue;

            try {
                entityManager.createNativeQuery(trimmedQuery).executeUpdate();
                results.add(new QueryResult(trimmedQuery, true, null));
            } catch (IllegalArgumentException e) {
                results.add(new QueryResult(trimmedQuery, false, "Invalid SQL query syntax"));
            } catch (javax.persistence.PersistenceException e) {
                results.add(new QueryResult(trimmedQuery, false, "Database execution error"));
            } catch (Exception e) {
                results.add(new QueryResult(trimmedQuery, false, "Unexpected error occurred"));
            }
        }

        return results;
    }
}
