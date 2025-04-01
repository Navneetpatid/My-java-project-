-- Update only NULL values in existing records
UPDATE public.cp_master 
SET cp_admin_api_url = 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc'
WHERE id = 1 AND cp_admin_api_url IS NULL;

UPDATE public.cp_master 
SET cp_admin_api_url = 'kdp02uk-dev.ikp10lr.cloud.uk.hsbc'
WHERE id = 2 AND cp_admin_api_url IS NULL;

-- For records that already have values, these will be skipped
-- because we're only targeting NULL fields

-- Insert additional records that don't exist yet
-- (only if you want to add these new records)
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'DEV', 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'DEV'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'PPD', 'https://kcphk-ppd.hsbc-11383538-kongcphk90-dev.dev.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PPD'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'PPD', 'https://kcpuk-ppd.hsbc-11382986-kongcpuk90-dev.dev.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PPD'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'PRD', 'https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PRD'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'PRD', 'https://kcpuk-prod.hsbc-11382986-kongcpuk-prod.prod.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PRD'
);
