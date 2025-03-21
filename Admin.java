@RestController
@RequestMapping("/api/engagement")
public class EngagementController {

    @Autowired
    private EngagementService engagementService;

    @GetMapping("/validate")
    public ResponseEntity<List<Map<String, Object>>> validateEngagement(
            @RequestParam String engagementId,
            @RequestParam String workspace) {
        return ResponseEntity.ok(engagementService.validateWorkspaceForEngagement(engagementId, workspace));
    }
}
