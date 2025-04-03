-- Clear existing data if needed (optional)
-- TRUNCATE TABLE dmz_lb_master;

-- Insert data for DMZ Load Balancer master table
INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (1, 'hap-dev-api-gw-wk.systems.uk.hsbc', 'DEV', 'UK', SYSDATE, SYSDATE);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (2, 'hap-dev-api-gw-tk.hk.hsbc', 'DEV', 'HK', SYSDATE, SYSDATE);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (3, 'hap-preprod-api-gw-wk.systems.uk.hsbc', 'PPD', 'UK', SYSDATE, SYSDATE);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (4, 'hap-preprod-api-gw-tk.hk.hsbc', 'PPD', 'HK', SYSDATE, SYSDATE);

INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (5, 'hap-api-gw.systems.uk.hsbc', 'PRD', 'UK', SYSDATE, SYSDATE);

-- Note: For ID 6, there appears to be multiple load balancers in the original data
-- You can choose to insert them as a single string or split into multiple records
INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
VALUES (6, 'hap-api-gw-tk.hk.hsbc,hap-api-gw-sk.hk.hsbc', 'PRD', 'HK', SYSDATE, SYSDATE);

-- Alternative for ID 6 if you want to split into separate records:
-- INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
-- VALUES (6, 'hap-api-gw-tk.hk.hsbc', 'PRD', 'HK', SYSDATE, SYSDATE);
--
-- INSERT INTO dmz_lb_master (id, load_balancer, environment, region, created_date, updated_date)
-- VALUES (7, 'hap-api-gw-sk.hk.hsbc', 'PRD', 'HK', SYSDATE, SYSD
