-- ********************************************************************
-- *** AUTH
-- ********************************************************************

CREATE TABLE IF NOT EXISTS public.user (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR,
  organization VARCHAR,
  country VARCHAR,
  email VARCHAR UNIQUE,
  username VARCHAR UNIQUE,
  password VARCHAR,
  created_at TIMESTAMP
);

-- ********************************************************************
-- *** DAMAGED PRODUCT
-- ********************************************************************

 CREATE TABLE IF NOT EXISTS public.washing_machine_details (
   id BIGSERIAL PRIMARY KEY,

   -- PACKAGE
   applicable_package_damage BOOLEAN,
   package_damaged BOOLEAN,
   package_dirty BOOLEAN,
   package_material_available BOOLEAN,

   -- VISIBLE SURFACES
   applicable_visible_surfaces_damage BOOLEAN,
   visible_surfaces_has_scratches BOOLEAN,
   visible_surfaces_scratches_length DOUBLE PRECISION,

   visible_surfaces_has_dents BOOLEAN,
   visible_surfaces_dents_depth DOUBLE PRECISION,

   visible_surfaces_has_minor_damage BOOLEAN,
   visible_surfaces_minor_damage VARCHAR,

   visible_surfaces_has_major_damage BOOLEAN,
   visible_surfaces_major_damage VARCHAR,

   -- HIDDEN SURFACES
   applicable_hidden_surfaces_damage BOOLEAN,
   hidden_surfaces_has_scratches BOOLEAN,
   hidden_surfaces_scratches_length DOUBLE PRECISION,

   hidden_surfaces_has_dents BOOLEAN,
   hidden_surfaces_dents_depth DOUBLE PRECISION,

   hidden_surfaces_has_minor_damage BOOLEAN,
   hidden_surfaces_minor_damage VARCHAR,

   hidden_surfaces_has_major_damage BOOLEAN,
   hidden_surfaces_major_damage VARCHAR,

   price INTEGER,
   repair_price INTEGER
);

CREATE TABLE IF NOT EXISTS public.washing_machine (
  id BIGSERIAL PRIMARY KEY,
  category VARCHAR,
  manufacturer VARCHAR,

  damage_type VARCHAR,
  return_type VARCHAR,
  identification_mode VARCHAR,

  serial_number VARCHAR UNIQUE,
  model VARCHAR,
  type VARCHAR,

  recommendation VARCHAR,

  created_at TIMESTAMP,

  washing_machine_details_id BIGINT REFERENCES public.washing_machine_details(id)
);

CREATE TABLE IF NOT EXISTS public.washing_machine_image (
  id BIGSERIAL PRIMARY KEY,
  image_prefix VARCHAR,
  image BYTEA,

  washing_machine_id BIGINT REFERENCES public.washing_machine(id)
);