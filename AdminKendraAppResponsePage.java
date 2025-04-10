public class HapCERServiceImpl implements HapCERService {
    
    @PersistenceContext
    private EntityManager entityManager;

    public List<QueryResult> executeQueries(List<String> queries) {
        List<QueryResult> results = new ArrayList<>();
        
        if (queries == null || queries.isEmpty()) {
            results.add(new QueryResult(null, false, "Query list is empty or null"));
            return results;
        }

        for (String query : queries) {
            String trimmedQuery = query.trim();
            if (trimmedQuery.isEmpty()) {
                results.add(new QueryResult("", false, "Empty query string"));
                continue;
            }

            try {
                int updatedCount = entityManager.createNativeQuery(trimmedQuery).executeUpdate();
                
                if (updatedCount > 0) {
                    results.add(new QueryResult(trimmedQuery, true, "Successfully updated " + updatedCount + " rows"));
                } else {
                    // Distinguish between no matching rows vs valid query with zero effect
                    results.add(new QueryResult(trimmedQuery, false, 
                        "Query executed successfully but no matching rows found"));
                }
                
            } catch (IllegalArgumentException e) {
                results.add(new QueryResult(trimmedQuery, false, 
                    "SQL Syntax Error: " + extractSyntaxErrorMessage(e)));
            } catch (PersistenceException e) {
                results.add(new QueryResult(trimmedQuery, false, 
                    "Database Error: " + extractDatabaseErrorMessage(e)));
            } catch (Exception e) {
                results.add(new QueryResult(trimmedQuery, false, 
                    "Execution Error: " + e.getMessage()));
            }
        }
        return results;
    }

    private String extractSyntaxErrorMessage(Exception e) {
        String msg = e.getMessage();
        // Handle specific syntax error patterns from your logs
        if (msg.contains("at or near")) {
            return "Invalid syntax near: " + msg.substring(msg.indexOf("at or near"));
        }
        return "Invalid SQL syntax - " + msg;
    }

    private String extractDatabaseErrorMessage(PersistenceException e) {
        Throwable cause = e.getCause();
        if (cause instanceof SQLException) {
            SQLException sqlEx = (SQLException) cause;
            // Handle specific error codes
            if (sqlEx.getSQLState().equals("42703")) { // Invalid column
                return "Invalid column name in query";
            } else if (sqlEx.getSQLState().equals("42P01")) { // Table not found
                return "Table does not exist";
            }
            return "Database error: " + sqlEx.getMessage();
        }
        return "Database operation failed";
    }
}
