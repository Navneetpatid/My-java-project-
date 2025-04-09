@RestController
@RequestMapping("/api")
public class BulkUpdateController {

    @Autowired
    private CoreService coreService;

    @PostMapping("/bulk/update")
    public ResponseEntity<?> bulkUpdate(@RequestBody QueryRequest request) {
        return coreService.bulkUpdate(request);
    }
}
