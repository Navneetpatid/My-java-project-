-- Update single row (example for HK DEV environment)
UPDATE public.cp_master
SET 
    platform = 'GCP',
    environment = 'DEV',
    cp_admin_api_url = 'https://kcphk-dev.hsbc-11383538-kongcphk10-dev.dev.gcp.cloud.hk.hsbc',
    created_date = COALESCE(created_date, CURRENT_TIMESTAMP),
    updated_date = CURRENT_TIMESTAMP
WHERE region = 'HK' 
  AND platform = 'IKP'  -- Assuming we're updating this specific record
  AND environment = 'Dev'
RETURNING *;  -- This will show you the updated record
