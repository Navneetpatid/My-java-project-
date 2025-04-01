-- Update Hong Kong DEV environment
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' AND platform = 'GCP' AND environment = 'DEV';

-- Update Hong Kong PPD environment
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcphk-ppd.hsbc-11383538-kongcphk90-dev.dev.gcp.cloud.hk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PPD';

-- Update Hong Kong PRD environment
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PRD';

-- Update UK DEV environment
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'GCP' AND environment = 'DEV';

-- Update UK PPD environment
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcpuk-ppd.hsbc-11382986-kongcpuk90-dev.dev.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PPD';

-- Update UK PRD environment
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcpuk-prod.hsbc-11382986-kongcpuk-prod.prod.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PRD';
