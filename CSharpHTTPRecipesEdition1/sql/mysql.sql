SET NAMES latin1;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `spider_workload` (
  `workload_id` int(10) unsigned NOT NULL auto_increment,
  `host` int(10) unsigned NOT NULL,
  `url` varchar(2083) NOT NULL default '',
  `status` varchar(1) NOT NULL default '',
  `depth` int(10) unsigned NOT NULL,
  `url_hash` int(11) NOT NULL,
  `source_id` int(11) NOT NULL,
  PRIMARY KEY  (`workload_id`),
  KEY `status` (`status`),
  KEY `url_hash` (`url_hash`)
) ENGINE=MyISAM AUTO_INCREMENT=189 DEFAULT CHARSET=latin1;

CREATE TABLE `spider_host` (
  `host_id` int(10) unsigned NOT NULL auto_increment,
  `host` varchar(255) NOT NULL default '',
  `status` varchar(1) NOT NULL default '',
  `urls_done` int(11) NOT NULL,
  `urls_error` int(11) NOT NULL,
  PRIMARY KEY  (`host_id`)
) ENGINE=MyISAM AUTO_INCREMENT=19796 DEFAULT CHARSET=latin1;


SET FOREIGN_KEY_CHECKS = 1;
