CREATE TABLE [spider_host] (
  [host_id] counter  NOT NULL,
  [host] varchar(255) NOT NULL,
  [status] varchar(1) NOT NULL,
  [urls_done] int NOT NULL,
  [urls_error] int NOT NULL,
  PRIMARY KEY  ([host_id]),
  CONSTRAINT  `host` UNIQUE  (`host`)
);

CREATE TABLE [spider_workload] (
  [workload_id] counter NOT NULL,
  [host] integer  NOT NULL,
  [url] varchar(255) NOT NULL,
  [status] varchar(1) NOT NULL,
  [depth] integer NOT NULL,
  [url_hash] integer NOT NULL,
  [source_id] integer NOT NULL,
  PRIMARY KEY  ([workload_id])
);

create index idx_status on spider_workload (status);
create index idx_url_hash on spider_workload (url_hash);