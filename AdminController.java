-- Update existing records (assuming the IDs from first image correspond to these)
UPDATE public.cp_master 
SET region = 'HK', platform = 'IKP', environment = 'Dev', cp_admin_api_url = NULL
WHERE id = 1;

UPDATE public.cp_master 
SET region = 'UK', platform = 'IKP', environment = 'Dev', cp_admin_api_url = NULL
WHERE id = 2;

UPDATE public.cp_master 
SET region = 'UK', platform = 'GKE', environment = 'Dev', 
    cp_admin_api_url = 'kdp02uk-dev.ikp10lr.cloud.uk.hsbc'
WHERE id = 3;

UPDATE public.cp_master 
SET region = 'UK', platform = 'GCP', environment = 'PRD', 
    cp_admin_api_url = 'https://kcpuk-prod.hsbc-11382986-kongcpuk-prod.prod.gcp.cloud.uk.hsbc'
WHERE id = 4;

-- Insert new records that don't exist yet (from second image)
-- First check if they exist to avoid duplicates
INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'DEV', 'https://kcplk-dev.hsbc-11383538-kongcplk10-dev.dev.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'DEV'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'DEV', 'https://kcplk-dev.hsbc-11382986-kongcplk10-dev.dev.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'DEV'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'PPD', 'https://kcplk-ppd.hsbc-11383538-kongcplk90-dev.dev.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PPD'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'PPD', 'https://kcplk-ppd.hsbc-11382986-kongcplk90-dev.dev.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PPD'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'HK', 'GCP', 'PRD', 'https://kcplk-prod.hsbc-11383538-kongcplk-prod.gcp.cloud.hk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PRD'
);

INSERT INTO public.cp_master (region, platform, environment, cp_admin_api_url)
SELECT 'UK', 'GCP', 'PRD', 'https://kcplk-prod.hsbc-11382986-kongcplk-prod.prod.gcp.cloud.uk.hsbc'
WHERE NOT EXISTS (
    SELECT 1 FROM public.cp_master 
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PRD'
);
