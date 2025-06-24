@Service
public class ServiceNowClient {

    @Value("${servicenow.username}")
    private String username;

    @Value("${servicenow.password}")
    private String password;

    @Value("${servicenow.host}")
    private String snowHost;

    @Value("${servicenow.customHeader}")
    private String customHeader;

    private final RestTemplate restTemplate;

    public ServiceNowClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Map<String, Object> getChangeDetails(String changeNumber) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();

        String url = String.format("%s/cto-ea-sn-change/api/v1/hsbcc/itsm/change?number=%s", snowHost, changeNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.set("xCustomHeaderChg", customHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ChangeDetailsResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, ChangeDetailsResponse.class);

        ChangeDetailsResponse snowResponse = response.getBody();

        if (snowResponse == null || !"200".equals(snowResponse.getResult().getResponseSummary().getCode())
            || !"1".equals(snowResponse.getResult().getResponseSummary().getTotalRecords())) {
            throw new Exception("Error retrieving change details");
        }

        ChangeRequest cr = snowResponse.getResult().getChangeRequests();

        returnMap.put("approval", cr.getApproval());
        returnMap.put("onHold", cr.getOnHold());
        returnMap.put("refNumber", cr.getNumber());
        returnMap.put("changeOrderType", cr.getType());
        returnMap.put("changeOrderSubType", cr.getSubType());
        returnMap.put("status", cr.getStateDescription());
        returnMap.put("category", cr.getCategory());
        returnMap.put("implementingGroup", cr.getAssignmentGroup());
        returnMap.put("scheduledStartDate", cr.getStartDate());
        returnMap.put("scheduledEndDate", cr.getEndDate());
        returnMap.put("chgModel", cr.getChangeModel());
        returnMap.put("businessService", cr.getBusinessService());

        List<String> CIs = new ArrayList<>();
        if (cr.getAffectedCIs() != null) {
            for (String ci : cr.getAffectedCIs()) {
                CIs.add(ci.toLowerCase());
            }
        }
        returnMap.put("CIs", CIs);

        return returnMap;
    }
            
