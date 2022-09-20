    create table if not exists public.t_poi
    (
      id serial not null		constraint t_area_pkey	primary key,
      name varchar(100) not null,
      type integer not null
    );
    select addGeometryColumn('public','t_poi','shape',4326,'Point',2);

    create table if not exists public.t_river
    (
      id serial not null		constraint t_river_pkey	primary key,
      name varchar(100) not null,
      type integer not null
    );
    select addGeometryColumn('public','t_river','shape',4326,'LineString',2);

  create table if not exists public.t_build
  (
    id serial not null		constraint t_build_pkey	primary key,
    name varchar(100) not null,
    type integer not null
  );
  select addGeometryColumn('public','t_build','shape',4326,'Polygon',2);
