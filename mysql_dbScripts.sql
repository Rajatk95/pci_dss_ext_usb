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
  PRIMARY KEY  (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_id`,`role_id`),
  KEY `FK143BF46A8682A5AE` (`user_id`),
  KEY `FK143BF46AE157E1CE` (`role_id`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;


CREATE TABLE SOA_APP_KEY(
APP_ID bigint unsigned primary key auto_increment,
app_user_id bigint(20) NOT NULL,
name varchar(255) UNIQUE KEY not null,
status enum('Active', 'Inactive', 'Other') DEFAULT 'Active' not null,
SVN_KEY BLOB NOT NULL, 
create_date datetime not null,
modify_date datetime,
FOREIGN KEY (app_user_id) REFERENCES app_user(id)
)engine=INNODB; 