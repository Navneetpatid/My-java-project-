-- 1. Update HK IKP Dev record (currently has NULL URL)
UPDATE public.cp_master
SET 
    platform = 'GCP',  -- Changing from IKP to GCP
    environment = 'DEV',  -- Ensuring proper case
    cp_admin_api_url = 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' AND platform = 'IKP' AND environment = 'Dev';

-- 2. Update UK IKP Dev record (currently has NULL URL)
UPDATE public.cp_master
SET 
    platform = 'GCP',  -- Changing from IKP to GCP
    environment = 'DEV',  -- Ensuring proper case
    cp_admin_api_url = 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'IKP' AND environment = 'Dev';

-- 3. Update UK GKE Dev record (has existing URL but needs standardization)
UPDATE public.cp_master
SET 
    platform = 'GCP',  -- Changing from GKE to GCP
    cp_admin_api_url = 'https://kcpuk-dev.hsbc-11382986-kongcpuk10-dev.dev.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'GKE' AND environment = 'Dev';

-- 4. UK GCP PRD record is correct - no update needed

-- Additional records for PPD environments (not shown but mentioned in previous requests)
UPDATE public.cp_master
SET 
    cp_admin_api_url = 'https://kcphk-ppd.hsbc-11383538-kongcphk90-ppd.ppd.gcp.cloud.hk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PPD';

UPDATE public.cp_master
SET 
    cp_admin_api_url = 'https://kcpuk-ppd.hsbc-11382986-kongcpuk90-ppd.ppd.gcp.cloud.uk.hsbc',
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PPD';
