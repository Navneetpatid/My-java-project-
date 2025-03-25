CREATE TABLE IF NOT EXISTS public.dmz_lb_master (
    id SERIAL NOT NULL,
    load_balancer character varying(150) COLLATE pg_catalog."default" NOT NULL,
    environment character varying(20) COLLATE pg_catalog."default" NOT NULL,
    region character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT dmz_lb_master_pkey PRIMARY KEY (id)
);
