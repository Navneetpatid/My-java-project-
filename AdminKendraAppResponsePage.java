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
            int updatedRows = entityManager.createNativeQuery(trimmedQuery).executeUpdate();

            if (updatedRows > 0) {
                results.add(new QueryResult(trimmedQuery, true, null));
            } else {
                results.add(new QueryResult(trimmedQuery, false, "Query executed but no rows affected"));
            }
        } catch (javax.persistence.PersistenceException e) {
            results.add(new QueryResult(trimmedQuery, false, "Invalid SQL syntax or unknown column/table: " + e.getMessage()));
        } catch (Exception e) {
            results.add(new QueryResult(trimmedQuery, false, "Unexpected error occurred: " + e.getMessage()));
        }
    }

    return results;
}
