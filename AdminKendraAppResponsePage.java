@RestController
@RequestMapping("/api/update")
public class BulkUpdateController {

    @Autowired
    private BulkUpdateService bulkUpdateService;

    @PostMapping("/execute")
    public ResponseEntity<Object> bulkUpdate(@RequestBody QueryRequest queryRequest) {
        try {
            bulkUpdateService.executeQueries(queryRequest.getQuery());
            return ResponseEntity.ok("Update successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error occurred: " + e.getMessage());
        }
    }
}
