@Service
public class CoreService {

    @PersistenceContext
    private EntityManager entityManager;

    public ResponseEntity<?> bulkUpdate(QueryRequest queryRequest) {
        try {
            if (queryRequest == null || queryRequest.getQuery() == null || queryRequest.getQuery().isBlank()) {
                return ResponseEntity.badRequest().body("Query is empty or null");
            }

            String allQueries = queryRequest.getQuery();
            List<String> queries = Arrays.stream(allQueries.split("\n"))
                    .map(String::trim)
                    .filter(q -> !q.isEmpty())
                    .collect(Collectors.toList());

            for (String query : queries) {
                entityManager.createNativeQuery(query).executeUpdate();
            }

            return ResponseEntity.ok("All queries executed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during bulk update: " + e.getMessage());
        }
    }
                          }
