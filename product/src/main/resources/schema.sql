CREATE TABLE IF NOT EXISTS public.product(
  id BIGSERIAL PRIMARY KEY,
  category VARCHAR,
  manufacturer VARCHAR,
  model VARCHAR UNIQUE,
  type VARCHAR UNIQUE
);