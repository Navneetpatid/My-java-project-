-- 1. Update HK DEV environment
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'DEV',
    cp_admin_api_url = 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE UPPER(region) = 'HK' 
  AND UPPER(environment) = 'DEV';

-- 2. Update HK PPD environment
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'PPD',
    cp_admin_api_url = 'https://kcphk-ppd.hsbc-11383538-kongcphk90-ppd.ppd.gcp.cloud.hk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE UPPER(region) = 'HK' 
  AND UPPER(environment) = 'PPD';

-- 3. Update HK PRD environment
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'PRD',
    cp_admin_api_url = 'https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE UPPER(region) = 'HK' 
  AND UPPER(environment) = 'PRD';

-- 4. Update UK DEV environment
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'DEV',
    cp_admin_api_url = 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE UPPER(region) = 'UK' 
  AND UPPER(environment) = 'DEV';

-- 5. Update UK PPD environment
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'PPD',
    cp_admin_api_url = 'https://kcpuk-ppd.hsbc-11382986-kongcpuk90-ppd.ppd.gcp.cloud.uk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE UPPER(region) = 'UK' 
  AND UPPER(environment) = 'PPD';

-- 6. Update UK PRD environment
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'PRD',
    cp_admin_api_url = 'https://kcpuk-prod.hsbc-11382986-kongcpuk-prod.prod.gcp.cloud.uk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE UPPER(region) = 'UK' 
  AND UPPER(environment) = 'PRD';
