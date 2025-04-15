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

        QueryResult validationResult = validateAndReconstructQuery(trimmedQuery);
        if (!validationResult.isSuccess()) {
            results.add(validationResult);
            continue;
        }

        String safeQuery = validationResult.getQuery(); // already validated

        try {
            int updatedRows = entityManager.createNativeQuery(safeQuery).executeUpdate();
            if (updatedRows > 0) {
                results.add(new QueryResult(safeQuery, true, null));
            } else {
                results.add(new QueryResult(safeQuery, false, "Query executed but no rows affected."));
            }
        } catch (PersistenceException e) {
            results.add(new QueryResult(safeQuery, false, "Invalid SQL syntax or unknown column/table."));
        }
    }

    return results;
}
private QueryResult validateAndReconstructQuery(String query) {
    String lowerQuery = query.toLowerCase();

    if (!(lowerQuery.startsWith("update") || lowerQuery.startsWith("insert") || lowerQuery.startsWith("delete"))) {
        return new QueryResult(query, false, "Only UPDATE, INSERT, DELETE queries are allowed");
    }

    if (query.contains(";") || query.contains("--") || lowerQuery.contains("drop") || lowerQuery.contains("alter")) {
        return new QueryResult(query, false, "Query contains disallowed keywords or special characters.");
    }

    // Basic table whitelist (enhance this list)
    String[] allowedTables = {"employee", "orders", "product"}; // Add valid table names
    boolean tableAllowed = false;

    for (String table : allowedTables) {
        if (lowerQuery.contains(table)) {
            tableAllowed = true;
            break;
        }
    }

    if (!tableAllowed) {
        return new QueryResult(query, false, "Query contains a table not allowed.");
    }

    // Inject 'updated_date = now()' if it's an UPDATE query
    String safeQuery = injectUpdatedDateIfUpdate(query);
    return new QueryResult(safeQuery, true, null);
}
private String injectUpdatedDateIfUpdate(String query) {
    String lowerQuery = query.toLowerCase();
    if (lowerQuery.startsWith("update") && lowerQuery.contains("set")) {
        return query.replaceFirst("(?i)set", "SET updated_date = now(),");
    }
    return query;
    }
