--liquibase formatted sql

--changeset thainguyen:0
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_uuid` varchar(50) NOT NULL,
  `name` varchar(200) NOT NULL,
  `config` json DEFAULT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'ACTIVE',
  `created_time` timestamp DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



