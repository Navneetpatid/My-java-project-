-- Corrected SELECT test query (from first part of your image)
SELECT 'HK', 'GCP', 'PRD', 'https://kcphk-prod.hsbc-11383538-kongcphk-prod.gcp.cloud.hk.hsbc'  
WHERE NOT EXISTS (  
    SELECT 1 FROM public.cp_master  
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PRD'  
);

-- Corrected INSERT statement for UK region
INSERT INTO public.cp_master (region, platform, environment, cp_sdmdn_api_url)  
SELECT 'UK', 'GCP', 'PRD', 'https://kcpuk-prod.hsbc-11382986-kongcpuk-prod.prod.gcp.cloud.uk.hsbc'  
WHERE NOT EXISTS (  
    SELECT 1 FROM public.cp_master  
    WHERE region = 'UK' AND platform = 'GCP' AND environment = 'PRD'  
);

-- For HK region (if you need to insert this too)
INSERT INTO public.cp_master (region, platform, environment, cp_sdmdn_api_url)  
SELECT 'HK', 'GCP', 'PRD', 'https://kcphk-prod.hsbc-11383538-kongcphk-prod.gcp.cloud.hk.hsbc'  
WHERE NOT EXISTS (  
    SELECT 1 FROM public.cp_master  
    WHERE region = 'HK' AND platform = 'GCP' AND environment = 'PRD'  
);
