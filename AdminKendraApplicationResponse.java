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

        // Validate query to allow only specific SQL operations
        String lowerQuery = trimmedQuery.toLowerCase();

        if (!(lowerQuery.startsWith("update") || lowerQuery.startsWith("insert") || lowerQuery.startsWith("delete"))) {
            results.add(new QueryResult(trimmedQuery, false, "Only UPDATE, INSERT, DELETE queries are allowed."));
            continue;
        }

        if (trimmedQuery.contains(";")) {
            results.add(new QueryResult(trimmedQuery, false, "Semicolons are not allowed in the query."));
            continue;
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
        } catch (Exception e) {
            results.add(new QueryResult(trimmedQuery, false, "Unexpected error occurred: " + e.getMessage()));
        }
    }

    return results;
              }
