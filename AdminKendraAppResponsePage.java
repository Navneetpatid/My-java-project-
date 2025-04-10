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
            int updatedCount = entityManager.createNativeQuery(trimmedQuery).executeUpdate();

            if (updatedCount > 0) {
                results.add(new QueryResult(trimmedQuery, true, null));
            } else {
                results.add(new QueryResult(trimmedQuery, false, "Query executed but no rows updated"));
            }

        } catch (IllegalArgumentException e) {
            results.add(new QueryResult(trimmedQuery, false, "Invalid query syntax: " + e.getMessage()));
        } catch (RuntimeException e) {
            results.add(new QueryResult(trimmedQuery, false, "Runtime exception: " + e.getMessage()));
        } catch (Exception e) {
            results.add(new QueryResult(trimmedQuery, false, "Unexpected error: " + e.getMessage()));
        }
    }

    return results;
}
