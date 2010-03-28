drop table SOA_APP_KEY_new;

drop table app_user_new;

CREATE TABLE app_user_new (
  id number NOT NULL ,
  account_expired char(1) NOT NULL,
  account_locked char(1) NOT NULL,
  address varchar(150) DEFAULT NULL,
  city varchar(50) NOT NULL,
  country varchar(100) DEFAULT NULL,
  postal_code varchar(15) NOT NULL,
  province varchar(100) DEFAULT NULL,
  credentials_expired char(1) NOT NULL,
  email varchar(255) NOT NULL,
  account_enabled char(1) DEFAULT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  password varchar(255) NOT NULL,
  password_hint varchar(255) DEFAULT NULL,
  phone_number varchar(255) DEFAULT NULL,
  username varchar(50) NOT NULL,
  version number DEFAULT NULL,
  website varchar(255) DEFAULT NULL,
  PRIMARY KEY  (id),
  UNIQUE   (email),
  UNIQUE   (username)
) ;

drop table role_new;

CREATE TABLE role_new (
  id number NOT NULL ,
  description varchar(64) DEFAULT NULL,
  name varchar(20) DEFAULT NULL,
  PRIMARY KEY  (id)
) ;


drop table user_role_new;

CREATE TABLE user_role_new (
  user_id number NOT NULL,
  role_id number NOT NULL,
  PRIMARY KEY  (user_id,role_id)
) ;

drop table SOA_GROUP;
create table SOA_GROUP (
  SOA_GROUP_ID NUMBER(22, 0) not null,
  SOA_GROUP_NAME VARCHAR2(100) not null,
  primary key (SOA_GROUP_ID)
);

drop table SOA_APP_KEY;
create table SOA_APP_KEY (
  APP_ID NUMBER(22, 0) not null,
  APP_USER_ID NUMBER(22, 0) not null,
  NAME VARCHAR2(255) not null,
  STATUS VARCHAR2(50),
  SVN_KEY BLOB not null,
  CREATE_DATE DATE not null,
  MODIFY_DATE DATE,
  SOA_GROUP_ID NUMBER(22, 0),
  primary key (APP_ID),
  foreign key (SOA_GROUP_ID) references SOA_GROUP(SOA_GROUP_ID),
  foreign key (APP_USER_ID) references APP_USER(ID)
);


drop sequence hibernate_sequence;
create sequence hibernate_sequence;


