-- First create a table for the load balancer data
CREATE TABLE IF NOT EXISTS public.load_balancers (
    id integer NOT NULL,
    load_balancer character varying(150) COLLATE pg_catalog."default" NOT NULL,
    environment character varying(20) COLLATE pg_catalog."default" NOT NULL,
    region character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT load_balancers_pkey PRIMARY KEY (id)
);

-- Then you could query it with joins to your other tables
SELECT 
    et.engagement_id,
    et.region as engagement_region,
    lb.region as lb_region,
    lb.load_balancer,
    lb.environment,
    wt.workspace
FROM 
    public.engagement_target et
JOIN 
    public.workspace_target wt ON et.engagement_id = wt.engagement_id
JOIN 
    public.load_balancers lb ON wt.environment = lb.environment AND et.region = lb.region
WHERE 
    wt.environment = 'DEV'  -- Example filter
ORDER BY 
    et.engagement_id
NOT NULL et.region, lb.load_balancer, wt.workspace;
