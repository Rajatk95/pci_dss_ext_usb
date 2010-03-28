CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `address` varchar(150) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `country` varchar(100) DEFAULT NULL,
  `postal_code` varchar(15) NOT NULL,
  `province` varchar(100) DEFAULT NULL,
  `credentials_expired` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `account_enabled` bit(1) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_hint` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `soa_group` (
  `SOA_GROUP_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `SOA_GROUP_NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`SOA_GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `soa_app_key` (
  `APP_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `CERTIFICATEID` varchar(100) NOT NULL,
  `status` enum('Active','Inactive','Other') NOT NULL DEFAULT 'Active',
  `SVN_KEY` blob NOT NULL,
  `create_date` datetime NOT NULL,
  `modify_date` datetime DEFAULT NULL,
  `SOA_GROUP_ID` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`APP_ID`),
  UNIQUE KEY `name` (`name`),
  KEY `soa_group_fk` (`SOA_GROUP_ID`),
  CONSTRAINT `soa_group_fk` FOREIGN KEY (`SOA_GROUP_ID`) REFERENCES `soa_group` (`SOA_GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `soa_app_prv_key` (
  `soa_app_prv_key_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `soa_app_key_id` bigint(20) unsigned NOT NULL,
  `soa_app_prv_key` blob NOT NULL,
  PRIMARY KEY (`soa_app_prv_key_id`),
  KEY `soa_app_key_fk` (`soa_app_key_id`),
  CONSTRAINT `soa_app_key_fk` FOREIGN KEY (`soa_app_key_id`) REFERENCES `soa_app_key` (`APP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK143BF46A8682A5AE` (`user_id`),
  KEY `FK143BF46AE157E1CE` (`role_id`),
  CONSTRAINT `FK143BF46A8682A5AE` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK143BF46AE157E1CE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

