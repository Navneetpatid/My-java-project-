public List<QueryResult> executeQueries(List<String> queries) {
    List<QueryResult> results = new ArrayList<>();

    if (queries == null || queries.isEmpty()) {
        results.add(new QueryResult(null, false, "Query list is empty or null"));
        return results;
    }

    for (String query : queries) {
        String trimmedQuery = query.trim();
        if (trimmedQuery.isEmpty()) continue;

        QueryResult validationResult = validateQuery(trimmedQuery);
        if (validationResult != null) {
            results.add(validationResult);
            continue;
        }

        // Inject updated_date for update statements
        String executableQuery = injectUpdatedDateIfUpdate(trimmedQuery);

        try {
            int updatedRows = entityManager.createNativeQuery(executableQuery).executeUpdate();
            if (updatedRows > 0) {
                results.add(new QueryResult(executableQuery, true, null));
            } else {
                results.add(new QueryResult(executableQuery, false, "Query executed but no rows affected."));
            }
        } catch (PersistenceException e) {
            results.add(new QueryResult(executableQuery, false, "Invalid SQL syntax or unknown column/table."));
        }
    }

    return results;
}

private QueryResult validateQuery(String query) {
    String lowerQuery = query.toLowerCase();

    if (!(lowerQuery.startsWith("update") || lowerQuery.startsWith("insert") || lowerQuery.startsWith("delete"))) {
        return new QueryResult(query, false, "Only UPDATE, INSERT, DELETE queries are allowed");
    }

    if (query.contains(";")) {
        return new QueryResult(query, false, "Semicolons are not allowed in the query.");
    }

    return null; // valid query
}

private String injectUpdatedDateIfUpdate(String query) {
    String lowerQuery = query.toLowerCase();
    if (lowerQuery.startsWith("update") && lowerQuery.contains("set")) {
        return query.replaceFirst("(?i)set", "SET updated_date = now(),");
    }
    return query;
        }
