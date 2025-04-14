public List<QueryResult> executeQueries(List<String> queries) {
    List<QueryResult> results = new ArrayList<>();
    if (queries == null || queries.isEmpty()) {
        results.add(new QueryResult(null, false, "Query list is empty or null"));
        return results;
    }

    for (String query : queries) {
        String trimmedQuery = query.trim();
        if (trimmedQuery.isEmpty()) continue;

        String lowerQuery = trimmedQuery.toLowerCase();

        // Validate only UPDATE, INSERT, DELETE
        if (!(lowerQuery.startsWith("update") || lowerQuery.startsWith("insert") || lowerQuery.startsWith("delete"))) {
            results.add(new QueryResult(trimmedQuery, false, "Only UPDATE, INSERT, DELETE queries are allowed"));
            continue;
        }

        if (trimmedQuery.contains(";")) {
            results.add(new QueryResult(trimmedQuery, false, "Semicolons are not allowed in the query."));
            continue;
        }

        // Inject updated_date = now() if update query
        if (lowerQuery.startsWith("update") && lowerQuery.contains("set")) {
            trimmedQuery = trimmedQuery.replaceFirst("(?i)set", "SET updated_date = now(),");
        }

        try {
            int updatedRows = entityManager.createNativeQuery(trimmedQuery).executeUpdate();
            if (updatedRows > 0) {
                results.add(new QueryResult(trimmedQuery, true, null));
            } else {
                results.add(new QueryResult(trimmedQuery, false, "Query executed but no rows affected."));
            }
        } catch (PersistenceException e) {
            results.add(new QueryResult(trimmedQuery, false, "Invalid SQL syntax or unknown column/table."));
        }
    }

    return results;
}
