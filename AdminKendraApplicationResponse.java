-- Clear existing data if needed (optional)
-- TRUNCATE TABLE dmz_lb_master;

-- Insert data for DMZ Load Balancer master table
INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (1, 'hap-dev-api-gw-wk.systems.uk.hsbc', 'DEV', 'UK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (2, 'hap-dev-api-gw-tk.hk.hsbc', 'DEV', 'HK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (3, 'hap-preprod-api-gw-wk.systems.uk.hsbc', 'PPD', 'UK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (4, 'hap-preprod-api-gw-tk.hk.hsbc', 'PPD', 'HK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (5, 'hap-api-gw.systems.uk.hsbc', 'PRD', 'UK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (6, 'hap-api-gw-tk.hk.hsbc,hap-api-gw-sk.hk.hsbc', 'PRD', 'HK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
