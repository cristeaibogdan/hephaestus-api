-- ********************************************************************
-- *** AUTH
-- ********************************************************************

CREATE TABLE public.user (
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

 CREATE TABLE public.washing_machine_details (
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

   visible_surfaces_has_small_damage BOOLEAN,
   visible_surfaces_small_damage VARCHAR,

   visible_surfaces_has_big_damage BOOLEAN,
   visible_surfaces_big_damage VARCHAR,

   -- HIDDEN SURFACES
   applicable_hidden_surfaces_damage BOOLEAN,
   hidden_surfaces_has_scratches BOOLEAN,
   hidden_surfaces_scratches_length DOUBLE PRECISION,

   hidden_surfaces_has_dents BOOLEAN,
   hidden_surfaces_dents_depth DOUBLE PRECISION,

   hidden_surfaces_has_small_damage BOOLEAN,
   hidden_surfaces_small_damage VARCHAR,

   hidden_surfaces_has_big_damage BOOLEAN,
   hidden_surfaces_big_damage VARCHAR,

   price INTEGER,
   repair_price INTEGER
);

CREATE TABLE public.washing_machine (
  id BIGSERIAL PRIMARY KEY,
  category VARCHAR,
  manufacturer VARCHAR,

  damage_type VARCHAR,
  return_type VARCHAR,
  identification_mode VARCHAR,

  serial_number VARCHAR UNIQUE,
  model VARCHAR,
  type VARCHAR,

  damage_level INTEGER,
  recommendation VARCHAR,

  created_at TIMESTAMP,

  washing_machine_details_id BIGINT REFERENCES public.washing_machine_details(id)
);

CREATE TABLE public.washing_machine_image (
  id BIGSERIAL PRIMARY KEY,
  image_prefix VARCHAR,
  image BYTEA,

  washing_machine_id BIGINT REFERENCES public.washing_machine(id)
);