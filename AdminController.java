-- Update Hong Kong DEV URL
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' AND platform = 'GCP' AND environment = 'DEV';

-- Update UK DEV URL
UPDATE public.cp_master
SET cp_admin_api_url = 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'GCP' AND environment = 'DEV';

-- Similar UPDATE statements for other environments...
