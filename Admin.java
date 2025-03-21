@Repository
public class EngagementRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Fetch Engagement Data
    public Map<String, Object> getEngagementData(String engagementId) {
        String query = "SELECT engagement_id, gbgf FROM engagement_target WHERE engagement_id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, engagementId);
        return result.isEmpty() ? null : result.get(0);
    }

    // Fetch Workspace Data
    public Map<String, Object> getWorkspaceData(String engagementId, String workspace) {
        String query = "SELECT workspace, dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, engagementId, workspace);
        return result.isEmpty() ? null : result.get(0);
    }

    // Fetch Mandatory Plugins
    public List<String> getMandatoryPlugins(String engagementId) {
        String query = "SELECT mandatory_plugin FROM engagement_plugin WHERE engagement_id = ?";
        return jdbcTemplate.queryForList(query, String.class, engagementId);
    }

    // Fetch DP Host URL
    public String getDpHostUrl(String engagementId, String workspace) {
        String query = "SELECT dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{engagementId, workspace}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Fetch CP Admin API URL
    public String getCpAdminApiUrl(String engagementId, String workspace) {
        String query = """
            SELECT cm.cp_admin_api_url
            FROM cp_master cm
            WHERE EXISTS (
                SELECT 1
                FROM engagement_target et
                JOIN workspace_target wt ON et.engagement_id = wt.engagement_id
                WHERE et.engagement_id = ?
                AND wt.workspace = ?
                AND et.region = cm.region
                AND wt.environment = cm.environment
            )
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(query, new Object[]{engagementId, workspace}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
