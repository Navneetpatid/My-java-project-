public class HapCERServiceImpl implements HapCERService {
    
    @PersistenceContext
    private EntityManager entityManager;

    public List<QueryResult> executeQueries(List<String> queries) {
        List<QueryResult> results = new ArrayList<>();
        Date currentDate = new Date();
        
        if (queries == null || queries.isEmpty()) {
            results.add(new QueryResult(null, false, "Query list is empty or null", currentDate, currentDate));
            return results;
        }

        for (String query : queries) {
            String trimmedQuery = query.trim();
            if (trimmedQuery.isEmpty()) {
                results.add(new QueryResult("", false, "Empty query string", currentDate, currentDate));
                continue;
            }

            try {
                int updatedCount = entityManager.createNativeQuery(trimmedQuery).executeUpdate();
                
                if (updatedCount > 0) {
                    results.add(new QueryResult(trimmedQuery, true, 
                        "Successfully updated " + updatedCount + " rows", 
                        currentDate, // createdDate
                        currentDate  // updatedDate
                    ));
                } else {
                    results.add(new QueryResult(trimmedQuery, false, 
                        "Query executed but no rows affected", 
                        currentDate, // createdDate
                        currentDate  // updatedDate
                    ));
                }
                
            } catch (Exception e) {
                results.add(new QueryResult(trimmedQuery, false, 
                    "Error: " + getSimplifiedErrorMessage(e), 
                    currentDate, // createdDate
                    currentDate  // updatedDate
                ));
            }
        }
        return results;
    }

    private String getSimplifiedErrorMessage(Exception e) {
        // Handle common error cases
        if (e instanceof IllegalArgumentException) {
            return "Invalid query syntax";
        } else if (e instanceof PersistenceException) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLException) {
                SQLException sqlEx = (SQLException) cause;
                if ("42703".equals(sqlEx.getSQLState())) {
                    return "Invalid column name";
                } else if ("42P01".equals(sqlEx.getSQLState())) {
                    return "Table not found";
                }
            }
            return "Database operation failed";
        }
        return "Execution error";
    }
}
