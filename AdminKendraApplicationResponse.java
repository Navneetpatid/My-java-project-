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

        // Validate query structure
        QueryResult validationResult = validateQuery(trimmedQuery);
        if (validationResult != null) {
            results.add(validationResult);
            continue;
        }

        // Process query with security measures
        try {
            String executableQuery = injectUpdatedDateIfUpdate(trimmedQuery);
            int updatedRows = executeSecureQuery(executableQuery);
            
            if (updatedRows > 0) {
                results.add(new QueryResult(executableQuery, true, null));
            } else {
                results.add(new QueryResult(executableQuery, false, "Query executed but no rows affected."));
            }
        } catch (SecurityException e) {
            results.add(new QueryResult(trimmedQuery, false, "Query blocked by security policy: " + e.getMessage()));
        } catch (PersistenceException e) {
            results.add(new QueryResult(trimmedQuery, false, "Invalid SQL syntax or unknown column/table."));
        }
    }
    return results;
}

private int executeSecureQuery(String query) {
    // Parse query to extract table and columns
    QueryParts parts = parseQueryParts(query);
    
    // Verify against whitelist
    if (!isTableAllowed(parts.getTable())) {
        throw new SecurityException("Table not permitted: " + parts.getTable());
    }
    
    // Execute with parameterized approach if possible
    if (canBeParameterized(parts)) {
        return executeParameterized(parts);
    }
    
    // Fallback to strict validation for dynamic queries
    if (!isQuerySafe(query)) {
        throw new SecurityException("Query contains potentially dangerous patterns");
    }
    
    // Final execution
    return entityManager.createNativeQuery(query).executeUpdate();
}

private QueryResult validateQuery(String query) {
    String lowerQuery = query.toLowerCase();
    
    // Only allow UPDATE, INSERT, DELETE
    if (!(lowerQuery.startsWith(Constant.UPDATE) || 
         lowerQuery.startsWith(Constant.INSERT) || 
         lowerQuery.startsWith(Constant.DELETE))) {
        return new QueryResult(query, false, "Only UPDATE, INSERT, DELETE queries are allowed");
    }

    // Block query chaining
    if (query.contains(";")) {
        return new QueryResult(query, false, "Semicolons are not allowed in the query.");
    }

    // Block comments that could bypass validation
    if (query.contains("--") || query.contains("/*")) {
        return new QueryResult(query, false, "SQL comments are not allowed.");
    }

    return null; // valid query
}

private String injectUpdatedDateIfUpdate(String query) {
    String lowerQuery = query.toLowerCase();
    if (lowerQuery.startsWith(Constant.UPDATE) && lowerQuery.contains("set") && 
        !lowerQuery.contains("updated_date")) {
        return query.replaceFirst("(?i)set", "SET updated_date = now(), ");
    }
    return query;
}

// Additional security methods
private boolean isTableAllowed(String tableName) {
    // Implement whitelist of allowed tables
    Set<String> allowedTables = Set.of("customers", "orders", "products");
    return allowedTables.contains(tableName.toLowerCase());
}

private boolean isQuerySafe(String query) {
    // Block common SQL injection patterns
    String lowerQuery = query.toLowerCase();
    return !lowerQuery.matches(".*(drop|alter|truncate|create|exec|xp_cmdshell|--|/\\*).*");
          }
