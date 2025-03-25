-- DMZ Load Balancer Master Table (without audit fields)
CREATE TABLE IF NOT EXISTS public.dmz_lb_master (
    id integer NOT NULL,
    load_balancer character varying(150) COLLATE pg_catalog."default" NOT NULL,
    environment character varying(20) COLLATE pg_catalog."default" NOT NULL,
    region character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT dmz_lb_master_pkey PRIMARY KEY (id),
    CONSTRAINT dmz_lb_unique_constraint UNIQUE (load_balancer, environment, region)
);

-- Insert the sample data (from your JSON) into dmz_lb_master
INSERT INTO public.dmz_lb_master 
(id, load_balancer, environment, region)
VALUES
(1, 'hap-dev-api-gw-wk.systems.uk.hsbc', 'DEV', 'UK'),
(2, 'hap-dev-api-gw-tk.hk.hsbc', 'DEV', 'HK'),
(3, 'hap-preprod-api-gw-wk.systems.uk.hsbc', 'PPD', 'UK'),
(4, 'hap-preprod-api-gw-tk.hk.hsbc', 'PPD', 'HK'),
(5, 'hap-api-gw.systems.uk.hsbc', 'PRD', 'UK'),
(6, 'hap-api-gw-tk.hk.hsbc', 'PRD', 'HK'),
(7, 'hap-api-gw-sk.hk.hsbc', 'PRD', 'HK');

-- Create a view that matches your original JSON structure
CREATE OR REPLACE VIEW public.dmz_lb_master_view AS
SELECT 
    id,
    load_balancer,
    environment,
    region
FROM 
    public.dmz_lb_master
ORDER BY 
    environment, region, id;
