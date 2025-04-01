-- HK DEV
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'DEV', 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'DEV'
);

-- UK DEV
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'DEV', 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'DEV'
);

-- HK PPD
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'PPD', 'https://kcphk-ppd.hsbc-11383538-kongcphk90-dev.dev.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PPD'
);

-- UK PPD
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'PPD', 'https://kcpuk-ppd.hsbc-11382986-kongcpuk90-dev.dev.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PPD'
);

-- HK PRD (if not already exists)
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'PRD', 'https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PRD'
);

-- UK PRD (if not already exists)
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'PRD', 'https://kcpuk-prod.hsbc-11382986-kongcpuk-prod.prod.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PRD'
);
