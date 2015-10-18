USE website;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id`           VARCHAR(50)   NOT NULL,
  `create_time`  DATETIME    DEFAULT NULL,
  `creator`      VARCHAR(20) DEFAULT NULL,
  `modifier`     VARCHAR(20) DEFAULT NULL,
  `modify_time`  DATETIME    DEFAULT NULL,
  `display_name` VARCHAR(3000) NOT NULL,
  `layer`        INT(11)       NOT NULL,
  `name`         VARCHAR(255)  NOT NULL,
  `path`         VARCHAR(3000) NOT NULL,
  `sort`         INT(11)     DEFAULT NULL,
  `p_id`         VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_AREA_PARENT` (`p_id`),
  CONSTRAINT `FK_AREA_PARENT` FOREIGN KEY (`p_id`) REFERENCES `area` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_attribute`;
CREATE TABLE `attr_attribute` (
  `id`                BIGINT(20) NOT NULL,
  `create_time`       DATETIME      DEFAULT NULL,
  `creator`           VARCHAR(20)   DEFAULT NULL,
  `modifier`          VARCHAR(20)   DEFAULT NULL,
  `modify_time`       DATETIME      DEFAULT NULL,
  `code`              VARCHAR(50)   DEFAULT NULL,
  `description`       VARCHAR(2000) DEFAULT NULL,
  `name`              VARCHAR(50)   DEFAULT NULL,
  `non_null`          BIT(1)        DEFAULT NULL,
  `attribute_type_id` BIGINT(20)    DEFAULT NULL,
  `not_temporary`     BIT(1)        DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ATTR_ATTRIBUTE_TYPE` (`attribute_type_id`),
  CONSTRAINT `FK_ATTR_ATTRIBUTE_TYPE` FOREIGN KEY (`attribute_type_id`) REFERENCES `attr_attribute_type` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_attribute_type`;
CREATE TABLE `attr_attribute_type` (
  `id`           BIGINT(20) NOT NULL,
  `create_time`  DATETIME      DEFAULT NULL,
  `creator`      VARCHAR(20)   DEFAULT NULL,
  `modifier`     VARCHAR(20)   DEFAULT NULL,
  `modify_time`  DATETIME      DEFAULT NULL,
  `data_type`    VARCHAR(200)  DEFAULT NULL,
  `description`  VARCHAR(2000) DEFAULT NULL,
  `name`         VARCHAR(200)  DEFAULT NULL,
  `converter_id` BIGINT(20)    DEFAULT NULL,
  `foreign_key`  VARCHAR(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `data_type` (`data_type`),
  KEY `FK_ATTRIBUTE_TYPE_CONVERTER` (`converter_id`),
  CONSTRAINT `FK_ATTRIBUTE_TYPE_CONVERTER` FOREIGN KEY (`converter_id`) REFERENCES `attr_converter` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_attribute_value`;
CREATE TABLE `attr_attribute_value` (
  `id`           BIGINT(20) NOT NULL,
  `create_time`  DATETIME    DEFAULT NULL,
  `creator`      VARCHAR(20) DEFAULT NULL,
  `modifier`     VARCHAR(20) DEFAULT NULL,
  `modify_time`  DATETIME    DEFAULT NULL,
  `target_id`    BIGINT(20)  DEFAULT NULL,
  `value`        LONGTEXT,
  `attribute_id` BIGINT(20)  DEFAULT NULL,
  `version_id`   BIGINT(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `attribute_id` (`attribute_id`, `target_id`),
  UNIQUE KEY `UK_9em04vtemlv55xj7g5imcdhbp` (`attribute_id`, `target_id`),
  UNIQUE KEY `UK_ptrpx979g4l09mtpd9eeghgp6` (`version_id`, `attribute_id`, `target_id`),
  KEY `FK_ATTRIBUTE_VALUE_ATTRIBUTE` (`attribute_id`),
  CONSTRAINT `FK_ATTRIBUTE_VALUE_ATTRIBUTE` FOREIGN KEY (`attribute_id`) REFERENCES `attr_attribute` (`id`),
  CONSTRAINT `FK_ATTRIBUTE_VALUE_VERSION` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_converter`;
CREATE TABLE `attr_converter` (
  `id`             BIGINT(20) NOT NULL,
  `create_time`    DATETIME      DEFAULT NULL,
  `creator`        VARCHAR(20)   DEFAULT NULL,
  `modifier`       VARCHAR(20)   DEFAULT NULL,
  `modify_time`    DATETIME      DEFAULT NULL,
  `description`    VARCHAR(2000) DEFAULT NULL,
  `name`           VARCHAR(200)  DEFAULT NULL,
  `type_converter` VARCHAR(200)  DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_custom_form`;
CREATE TABLE `attr_custom_form` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `version_id`  BIGINT(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dsh93ea6wa8vcremdf0f7yc1k` (`id`, `version_id`),
  KEY `FK_CMS_ARTICLE_VERSION` (`version_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_renderer`;
CREATE TABLE `attr_renderer` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `name`        VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_screen`;
CREATE TABLE `attr_screen` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `name`        VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_version`;
CREATE TABLE `attr_version` (
  `id`                BIGINT(20) NOT NULL,
  `create_time`       DATETIME     DEFAULT NULL,
  `creator`           VARCHAR(20)  DEFAULT NULL,
  `modifier`          VARCHAR(20)  DEFAULT NULL,
  `modify_time`       DATETIME     DEFAULT NULL,
  `attribute_sort`    VARCHAR(100) DEFAULT NULL,
  `not_temporary`     VARCHAR(150) DEFAULT NULL,
  `number`            VARCHAR(50)  DEFAULT NULL,
  `class_name`        VARCHAR(150) DEFAULT NULL,
  `target_class_name` VARCHAR(150) DEFAULT NULL,
  `type`              VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_327x6mdq5oe24bmpmhpnfmfva` (`class_name`, `number`),
  UNIQUE KEY `UK_8y7qe8pxko4k4u89gfro4xyao` (`target_class_name`, `number`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `attr_version_attribute`;
CREATE TABLE `attr_version_attribute` (
  `version_id`   BIGINT(20) NOT NULL,
  `attribute_id` BIGINT(20) NOT NULL,
  KEY `FK_VERSION_ATTRIBUTE` (`version_id`),
  KEY `FK9E941D276A23E76D` (`attribute_id`),
  KEY `FK9E941D279DD6B344` (`attribute_id`),
  CONSTRAINT `FK_3e5t0trmf4o2d57hf4dxx555t` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`),
  CONSTRAINT `FK_7miqgvgen00609i8eb67bc09` FOREIGN KEY (`attribute_id`) REFERENCES `attr_attribute` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auction_deal`;
CREATE TABLE `auction_deal` (
  `id`                BIGINT(20) NOT NULL,
  `create_time`       DATETIME      DEFAULT NULL,
  `creator`           VARCHAR(20)   DEFAULT NULL,
  `modifier`          VARCHAR(20)   DEFAULT NULL,
  `modify_time`       DATETIME      DEFAULT NULL,
  `approval_remark`   VARCHAR(1000) DEFAULT NULL,
  `deal_price`        VARCHAR(20)   DEFAULT NULL,
  `deal_state`        BIT(1)        DEFAULT NULL,
  `entrust_brokerage` VARCHAR(20)   DEFAULT NULL,
  `gain_sign`         VARCHAR(20)   DEFAULT NULL,
  `remark`            VARCHAR(1000) DEFAULT NULL,
  `service_charge`    VARCHAR(20)   DEFAULT NULL,
  `state`             BIT(1)        DEFAULT NULL,
  `deal_member_id`    BIGINT(20)    DEFAULT NULL,
  `entrust_member_id` BIGINT(20)    DEFAULT NULL,
  `deal_time`         DATETIME      DEFAULT NULL,
  `sn`                VARCHAR(255)  DEFAULT NULL,
  `auction_goods_id`  BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  KEY `FK_AUCTION_ORDER_ENTRUST_MEMBER` (`entrust_member_id`),
  KEY `FK_AUCTION_ORDER_DEAL_MEMBER` (`deal_member_id`),
  KEY `FK_AUCTION_GOODS_DEAL` (`auction_goods_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auction_goods`;
CREATE TABLE `auction_goods` (
  `id`                BIGINT(20)     NOT NULL,
  `create_time`       DATETIME      DEFAULT NULL,
  `creator`           VARCHAR(20)   DEFAULT NULL,
  `modifier`          VARCHAR(20)   DEFAULT NULL,
  `modify_time`       DATETIME      DEFAULT NULL,
  `author`            VARCHAR(50)   DEFAULT NULL,
  `decade`            VARCHAR(20)   DEFAULT NULL,
  `floor_price`       DECIMAL(19, 2) NOT NULL,
  `image_store`       VARCHAR(3000) DEFAULT NULL,
  `introduce`         VARCHAR(300)  DEFAULT NULL,
  `introduce_remark`  VARCHAR(1000) DEFAULT NULL,
  `material`          VARCHAR(50)   DEFAULT NULL,
  `name`              VARCHAR(50)   DEFAULT NULL,
  `signature`         VARCHAR(20)   DEFAULT NULL,
  `size`              VARCHAR(50)   DEFAULT NULL,
  `versions_num`      INT(11)       DEFAULT NULL,
  `goods_category_id` BIGINT(20)     NOT NULL,
  `approval_remark`   VARCHAR(20)   DEFAULT NULL,
  `state`             INT(11)       DEFAULT NULL,
  `sn`                VARCHAR(100)  DEFAULT NULL,
  `auction_time`      DATETIME      DEFAULT NULL,
  `auction_member_id` BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_AUCTION_GOODS_CATEGORY` (`goods_category_id`),
  KEY `FK_AUCTION_GOODS_AUCTION_MEMBER` (`auction_member_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_job`;
CREATE TABLE `auth_job` (
  `code`                  VARCHAR(255) NOT NULL,
  `create_time`           DATETIME     DEFAULT NULL,
  `creator`               VARCHAR(20)  DEFAULT NULL,
  `modifier`              VARCHAR(20)  DEFAULT NULL,
  `modify_time`           DATETIME     DEFAULT NULL,
  `description`           VARCHAR(255) DEFAULT NULL,
  `name`                  VARCHAR(255) DEFAULT NULL,
  `id`                    BIGINT(20)   NOT NULL,
  `organization_id`       VARCHAR(255) NOT NULL,
  `attribute_value_store` LONGTEXT,
  `version_id`            BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`code`),
  UNIQUE KEY `UK_lorppf2s7empqyh35y4yx43um` (`id`, `version_id`),
  KEY `FK_JOB_ORGANIZATION` (`organization_id`),
  KEY `FK_ky1u5rmo8pnsobepudhm797uc` (`version_id`),
  CONSTRAINT `FK_JOB_ORGANIZATION` FOREIGN KEY (`organization_id`) REFERENCES `auth_organization` (`code`),
  CONSTRAINT `FK_ky1u5rmo8pnsobepudhm797uc` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `icon`        VARCHAR(50)   DEFAULT NULL,
  `layer`       INT(11)    NOT NULL,
  `name`        VARCHAR(200)  DEFAULT NULL,
  `sort`        INT(11)       DEFAULT NULL,
  `type`        VARCHAR(20)   DEFAULT NULL,
  `value`       VARCHAR(200)  DEFAULT NULL,
  `pid`         BIGINT(20)    DEFAULT NULL,
  `path`        VARCHAR(200)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK57103996ECB485AF` (`pid`),
  CONSTRAINT `FK57103996ECB485AF` FOREIGN KEY (`pid`) REFERENCES `auth_menu` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_org_children`;
CREATE TABLE `auth_org_children` (
  `id`     BIGINT(20)   NOT NULL,
  `org_id` VARCHAR(255) NOT NULL,
  UNIQUE KEY `UK_o5kqf9y3ypla40kgeaifjljmk` (`org_id`),
  KEY `FK_fh3px31w7ty9whjsyx7f2qlr4` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_org_dimension`;
CREATE TABLE `auth_org_dimension` (
  `code`        VARCHAR(255) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `website_id`  BIGINT(20)   DEFAULT NULL,
  `website_key` VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK_AUTH_ORG_RELATION_WEBSITE` (`website_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_org_relation`;
CREATE TABLE `auth_org_relation` (
  `id`               BIGINT(20)    NOT NULL,
  `create_time`      DATETIME     DEFAULT NULL,
  `creator`          VARCHAR(20)  DEFAULT NULL,
  `modifier`         VARCHAR(20)  DEFAULT NULL,
  `modify_time`      DATETIME     DEFAULT NULL,
  `org_dimension`    TINYBLOB,
  `type`             INT(11)      DEFAULT NULL,
  `org_id`           VARCHAR(255) DEFAULT NULL,
  `org_dimension_id` VARCHAR(255) DEFAULT NULL,
  `subsidiary_id`    VARCHAR(255) DEFAULT NULL,
  `layer`            INT(11)       NOT NULL,
  `sort`             INT(11)      DEFAULT NULL,
  `pid`              BIGINT(20)   DEFAULT NULL,
  `path`             VARCHAR(3000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1hlr2wdqke2ms9ogukhn33nqt` (`org_dimension_id`, `org_id`),
  KEY `FK_AUTH_ORG_RELATION` (`org_id`),
  KEY `FK_AUTH_ORG_DIMENSION` (`org_dimension_id`),
  KEY `FK_AUTH_SUB_ORG_RELATION` (`subsidiary_id`),
  KEY `FK_AUTH_ORG_PID` (`pid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_organization`;
CREATE TABLE `auth_organization` (
  `code`        VARCHAR(255) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `type`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_permission`;
CREATE TABLE `auth_permission` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `enabled`     BIT(1)       DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `type`        VARCHAR(20)  DEFAULT NULL,
  `value`       VARCHAR(255) DEFAULT NULL,
  `resource_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_AUTH_PERMISSION_RESOURCE_PID` (`resource_id`),
  CONSTRAINT `FK_AUTH_PERMISSION_RESOURCE_PID` FOREIGN KEY (`resource_id`) REFERENCES `auth_resource` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_relation_resource`;
CREATE TABLE `auth_relation_resource` (
  `resource_id`     BIGINT(20) NOT NULL,
  `sub_resource_id` BIGINT(20) NOT NULL,
  KEY `FK4A14241AB8CDFC0` (`sub_resource_id`),
  KEY `FK4A14241A4A8A233F` (`resource_id`),
  CONSTRAINT `FK4A14241A4A8A233F` FOREIGN KEY (`resource_id`) REFERENCES `auth_resource` (`id`),
  CONSTRAINT `FK4A14241AB8CDFC0` FOREIGN KEY (`sub_resource_id`) REFERENCES `auth_resource` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_resource`;
CREATE TABLE `auth_resource` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `enabled`     BIT(1)       DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `type`        VARCHAR(20)  DEFAULT NULL,
  `value`       VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `code`        VARCHAR(255) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `enabled`     BIT(1)       DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `type`        VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_role_member`;
CREATE TABLE `auth_role_member` (
  `member_id` BIGINT(20)   NOT NULL,
  `role_code` VARCHAR(255) NOT NULL,
  KEY `FK_ROLE_MEMBER_MEM` (`member_id`),
  KEY `FKC5CAB66CB4FF51` (`role_code`),
  CONSTRAINT `FKC5CAB66CB4FF51` FOREIGN KEY (`role_code`) REFERENCES `auth_role` (`code`),
  CONSTRAINT `FK_ROLE_MEMBER_MEM` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_role_menu`;
CREATE TABLE `auth_role_menu` (
  `role_code` VARCHAR(255) NOT NULL,
  `menu_id`   BIGINT(20)   NOT NULL,
  KEY `FK4B980DB12558401F` (`menu_id`),
  KEY `FK4B980DB1B4FF51` (`role_code`),
  CONSTRAINT `FK4B980DB12558401F` FOREIGN KEY (`menu_id`) REFERENCES `auth_menu` (`id`),
  CONSTRAINT `FK4B980DB1B4FF51` FOREIGN KEY (`role_code`) REFERENCES `auth_role` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_role_permission`;
CREATE TABLE `auth_role_permission` (
  `role_code`     VARCHAR(255) NOT NULL,
  `permission_id` BIGINT(20)   NOT NULL,
  KEY `FK_neoq53ueau6irxawx52x0erht` (`permission_id`),
  KEY `FK_qrksfh1thqyjftmi6mgpvl3t1` (`role_code`),
  CONSTRAINT `FK_qrksfh1thqyjftmi6mgpvl3t1` FOREIGN KEY (`role_code`) REFERENCES `auth_role` (`code`),
  CONSTRAINT `FK_neoq53ueau6irxawx52x0erht` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_role_resource`;
CREATE TABLE `auth_role_resource` (
  `role_code`   VARCHAR(255) NOT NULL,
  `resource_id` BIGINT(20)   NOT NULL,
  KEY `FK90584360B4FF51` (`role_code`),
  KEY `FK905843604A8A233F` (`resource_id`),
  CONSTRAINT `FK_7fcjy3v2fo54vqf3dirlswkqe` FOREIGN KEY (`resource_id`) REFERENCES `auth_permission` (`id`),
  CONSTRAINT `FK905843604A8A233F` FOREIGN KEY (`resource_id`) REFERENCES `auth_resource` (`id`),
  CONSTRAINT `FK90584360B4FF51` FOREIGN KEY (`role_code`) REFERENCES `auth_role` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_role_user`;
CREATE TABLE `auth_role_user` (
  `user_id`   BIGINT(20)   NOT NULL,
  `role_code` VARCHAR(255) NOT NULL,
  KEY `FK4B9BE41DB4FF51` (`role_code`),
  KEY `FK_ROLE_USER_US` (`user_id`),
  CONSTRAINT `FK4B9BE41DB4FF51` FOREIGN KEY (`role_code`) REFERENCES `auth_role` (`code`),
  CONSTRAINT `FK_ROLE_USER_US` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
  `id`                      BIGINT(20)  NOT NULL,
  `create_time`             DATETIME     DEFAULT NULL,
  `creator`                 VARCHAR(20)  DEFAULT NULL,
  `modifier`                VARCHAR(20)  DEFAULT NULL,
  `modify_time`             DATETIME     DEFAULT NULL,
  `non_expired`             BIT(1)       DEFAULT NULL,
  `non_locked`              BIT(1)       DEFAULT NULL,
  `credentials_non_expired` BIT(1)       DEFAULT NULL,
  `enabled`                 BIT(1)       DEFAULT NULL,
  `last_login_time`         DATETIME     DEFAULT NULL,
  `lock_time`               DATETIME     DEFAULT NULL,
  `nick_name`               VARCHAR(50)  DEFAULT NULL,
  `password`                VARCHAR(20) NOT NULL,
  `username`                VARCHAR(20) NOT NULL,
  `website_key`             VARCHAR(20)  DEFAULT NULL,
  `logo_image_store`        VARCHAR(500) DEFAULT NULL,
  `organization_id`         VARCHAR(255) DEFAULT NULL,
  `user_type`               VARCHAR(20) NOT NULL,
  `website_id`              BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK_WEBSITE_USER` (`website_key`),
  KEY `FK_ORGANIZATION_USER` (`organization_id`),
  CONSTRAINT `FK_ORGANIZATION_USER` FOREIGN KEY (`organization_id`) REFERENCES `auth_organization` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_user_details`;
CREATE TABLE `auth_user_details` (
  `user_id`     BIGINT(20) NOT NULL,
  `birthday`    DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `email`       VARCHAR(50)  DEFAULT NULL,
  `mobile`      VARCHAR(20)  DEFAULT NULL,
  `name`        VARCHAR(20)  DEFAULT NULL,
  `sex`         VARCHAR(20)  DEFAULT NULL,
  `tel`         VARCHAR(20)  DEFAULT NULL,
  `website`     VARCHAR(50)  DEFAULT NULL,
  `avatar`      VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_usergroup`;
CREATE TABLE `auth_usergroup` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `code`        VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `enabled`     BIT(1)       DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_usergroup_member`;
CREATE TABLE `auth_usergroup_member` (
  `member_id`    BIGINT(20) NOT NULL,
  `usergroup_id` BIGINT(20) NOT NULL,
  KEY `FK_USERGROUP_MEMBER_MEM` (`member_id`),
  KEY `FK4FA8315C9349DD75` (`usergroup_id`),
  CONSTRAINT `FK4FA8315C9349DD75` FOREIGN KEY (`usergroup_id`) REFERENCES `auth_usergroup` (`id`),
  CONSTRAINT `FK_USERGROUP_MEMBER_MEM` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_usergroup_menu`;
CREATE TABLE `auth_usergroup_menu` (
  `usergroup_id` BIGINT(20) NOT NULL,
  `menu_id`      BIGINT(20) NOT NULL,
  KEY `FK4F7784A12558401F` (`menu_id`),
  KEY `FK_USERGROUP_MENU_UG` (`usergroup_id`),
  CONSTRAINT `FK4F7784A12558401F` FOREIGN KEY (`menu_id`) REFERENCES `auth_menu` (`id`),
  CONSTRAINT `FK_USERGROUP_MENU_UG` FOREIGN KEY (`usergroup_id`) REFERENCES `auth_usergroup` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_usergroup_permission`;
CREATE TABLE `auth_usergroup_permission` (
  `usergroup_id`  BIGINT(20) NOT NULL,
  `permission_id` BIGINT(20) NOT NULL,
  KEY `FK_sdhw9nnw2ee2m3fcbkl0twf95` (`permission_id`),
  KEY `FK_8b3si0ftpfclqv14vffdf4i8j` (`usergroup_id`),
  CONSTRAINT `FK_8b3si0ftpfclqv14vffdf4i8j` FOREIGN KEY (`usergroup_id`) REFERENCES `auth_usergroup` (`id`),
  CONSTRAINT `FK_sdhw9nnw2ee2m3fcbkl0twf95` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_usergroup_resource`;
CREATE TABLE `auth_usergroup_resource` (
  `usergroup_id` BIGINT(20) NOT NULL,
  `resource_id`  BIGINT(20) NOT NULL,
  KEY `FK_USERGROUP_RESOURCE_UG` (`usergroup_id`),
  KEY `FK18C2C2504A8A233F` (`resource_id`),
  CONSTRAINT `FK_gbec8034t0lvoa69ogq6k71ly` FOREIGN KEY (`resource_id`) REFERENCES `auth_permission` (`id`),
  CONSTRAINT `FK18C2C2504A8A233F` FOREIGN KEY (`resource_id`) REFERENCES `auth_resource` (`id`),
  CONSTRAINT `FK_USERGROUP_RESOURCE_UG` FOREIGN KEY (`usergroup_id`) REFERENCES `auth_usergroup` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `auth_usergroup_user`;
CREATE TABLE `auth_usergroup_user` (
  `user_id`      BIGINT(20) NOT NULL,
  `usergroup_id` BIGINT(20) NOT NULL,
  KEY `FK4F7B5B0D9349DD75` (`usergroup_id`),
  KEY `FK_USERGROUP_USER_US` (`user_id`),
  CONSTRAINT `FK4F7B5B0D9349DD75` FOREIGN KEY (`usergroup_id`) REFERENCES `auth_usergroup` (`id`),
  CONSTRAINT `FK_USERGROUP_USER_US` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `axis_message`;
CREATE TABLE `axis_message` (
  `id`          VARCHAR(255) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `in_message`  LONGTEXT,
  `out_message` LONGTEXT,
  `remote_addr` VARCHAR(20) DEFAULT NULL,
  `result`      VARCHAR(20) DEFAULT NULL,
  `type`        VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_article`;
CREATE TABLE `cms_article` (
  `id`                    BIGINT(20) NOT NULL,
  `create_time`           DATETIME     DEFAULT NULL,
  `creator`               VARCHAR(20)  DEFAULT NULL,
  `modifier`              VARCHAR(20)  DEFAULT NULL,
  `modify_time`           DATETIME     DEFAULT NULL,
  `author`                VARCHAR(255) DEFAULT NULL,
  `issue`                 BIT(1)       DEFAULT NULL,
  `keywords`              VARCHAR(255) DEFAULT NULL,
  `logo_image_store`      VARCHAR(500) DEFAULT NULL,
  `release_date`          VARCHAR(255) DEFAULT NULL,
  `summary`               VARCHAR(255) DEFAULT NULL,
  `title`                 VARCHAR(255) DEFAULT NULL,
  `category_code`         VARCHAR(50)  DEFAULT NULL,
  `content_id`            BIGINT(20)   DEFAULT NULL,
  `article_image_store`   LONGTEXT,
  `hqdate`                VARCHAR(255) DEFAULT NULL,
  `url`                   VARCHAR(500) DEFAULT NULL,
  `recommend`             BIT(1)       DEFAULT NULL,
  `version_id`            BIGINT(20)   DEFAULT NULL,
  `attribute_value_store` LONGTEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bjkx3mn4i2vmdl9ys3pys37fk` (`id`, `version_id`),
  KEY `FK_CMS_ARTICLE_CONTENT` (`content_id`),
  KEY `FK_CMS_ARTICLE_CATEGORY` (`category_code`),
  KEY `FK_CMS_ARTICLE_VERSION` (`version_id`),
  CONSTRAINT `FK_CMS_ARTICLE_CATEGORY` FOREIGN KEY (`category_code`) REFERENCES `cms_article_category` (`code`),
  CONSTRAINT `FK_CMS_ARTICLE_CONTENT` FOREIGN KEY (`content_id`) REFERENCES `cms_content` (`id`),
  CONSTRAINT `FK_CMS_ARTICLE_VERSION` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_article_attribute`;
CREATE TABLE `cms_article_attribute` (
  `category_id`  VARCHAR(50) NOT NULL,
  `attribute_id` BIGINT(20)  NOT NULL,
  KEY `FK1552411D602549CC` (`category_id`),
  KEY `FK1552411D9DD6B344` (`attribute_id`),
  CONSTRAINT `FK1552411D602549CC` FOREIGN KEY (`category_id`) REFERENCES `cms_article_category` (`code`),
  CONSTRAINT `FK1552411D9DD6B344` FOREIGN KEY (`attribute_id`) REFERENCES `attr_attribute` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_article_attribute_values`;
CREATE TABLE `cms_article_attribute_values` (
  `cms_article`      BIGINT(20) NOT NULL,
  `attribute_values` BIGINT(20) NOT NULL,
  UNIQUE KEY `UK_cluba5cx1e3mvvw241w4eja77` (`attribute_values`),
  KEY `FK_htykwg86alchwmmxhxptxrgfg` (`cms_article`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_article_category`;
CREATE TABLE `cms_article_category` (
  `code`                  VARCHAR(50)   NOT NULL,
  `create_time`           DATETIME      DEFAULT NULL,
  `creator`               VARCHAR(20)   DEFAULT NULL,
  `modifier`              VARCHAR(20)   DEFAULT NULL,
  `modify_time`           DATETIME      DEFAULT NULL,
  `description`           VARCHAR(2000) DEFAULT NULL,
  `layer`                 INT(11)       NOT NULL,
  `name`                  VARCHAR(200)  DEFAULT NULL,
  `path`                  VARCHAR(3000) NOT NULL,
  `sort`                  INT(11)       DEFAULT NULL,
  `pcode`                 VARCHAR(50)   DEFAULT NULL,
  `article_template_path` VARCHAR(255)  DEFAULT NULL,
  `egname`                VARCHAR(255)  DEFAULT NULL,
  `template_path`         VARCHAR(255)  DEFAULT NULL,
  `article_version_id`    BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK_CMS_ARTICLE_CATEGORY_VERSION` (`article_version_id`),
  KEY `FK_CMS_CATEGORY_PARENT` (`pcode`),
  CONSTRAINT `FK_CMS_ARTICLE_CATEGORY_VERSION` FOREIGN KEY (`article_version_id`) REFERENCES `attr_version` (`id`),
  CONSTRAINT `FK_CMS_CATEGORY_PARENT` FOREIGN KEY (`pcode`) REFERENCES `cms_article_category` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_banner`;
CREATE TABLE `cms_banner` (
  `code`        VARCHAR(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(500) DEFAULT NULL,
  `name`        VARCHAR(50)  DEFAULT NULL,
  `size`        VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_banner_item`;
CREATE TABLE `cms_banner_item` (
  `id`                 BIGINT(20) NOT NULL,
  `create_time`        DATETIME     DEFAULT NULL,
  `creator`            VARCHAR(20)  DEFAULT NULL,
  `modifier`           VARCHAR(20)  DEFAULT NULL,
  `modify_time`        DATETIME     DEFAULT NULL,
  `banner_image_store` VARCHAR(500) DEFAULT NULL,
  `summary`            VARCHAR(500) DEFAULT NULL,
  `title`              VARCHAR(200) DEFAULT NULL,
  `url`                VARCHAR(100) DEFAULT NULL,
  `sort`               INT(11)      DEFAULT NULL,
  `banner_code`        VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5vneejmcl6jkybilrepil6f7s` (`banner_code`),
  CONSTRAINT `FK_5vneejmcl6jkybilrepil6f7s` FOREIGN KEY (`banner_code`) REFERENCES `cms_banner` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `code`        VARCHAR(50)
                COLLATE utf8_bin NOT NULL,
  `create_time` DATETIME         DEFAULT NULL,
  `creator`     VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modifier`    VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modify_time` DATETIME         DEFAULT NULL,
  `clazz`       VARCHAR(100)
                COLLATE utf8_bin DEFAULT NULL,
  `description` VARCHAR(2000)
                COLLATE utf8_bin DEFAULT NULL,
  `layer`       INT(11)          NOT NULL,
  `name`        VARCHAR(200)
                COLLATE utf8_bin DEFAULT NULL,
  `path`        VARCHAR(3000)
                COLLATE utf8_bin NOT NULL,
  `sort`        INT(11)          DEFAULT NULL,
  `pcode`       VARCHAR(50)
                COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`code`),
  UNIQUE KEY `clazz` (`clazz`),
  KEY `FK_CMS_CATEGORY_PARENT` (`pcode`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `content`     LONGTEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_rcmdpos`;
CREATE TABLE `cms_rcmdpos` (
  `id`          BIGINT(20)       NOT NULL,
  `create_time` DATETIME         DEFAULT NULL,
  `creator`     VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modifier`    VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modify_time` DATETIME         DEFAULT NULL,
  `description` VARCHAR(500)
                COLLATE utf8_bin DEFAULT NULL,
  `code`        VARCHAR(20)
                COLLATE utf8_bin NOT NULL,
  `name`        VARCHAR(50)
                COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `cms_rcmdpos_target`;
CREATE TABLE `cms_rcmdpos_target` (
  `id`           BIGINT(20) NOT NULL,
  `create_time`  DATETIME         DEFAULT NULL,
  `creator`      VARCHAR(255)
                 COLLATE utf8_bin DEFAULT NULL,
  `modifier`     VARCHAR(255)
                 COLLATE utf8_bin DEFAULT NULL,
  `modify_time`  DATETIME         DEFAULT NULL,
  `display_date` DATETIME         DEFAULT NULL,
  `summary`      VARCHAR(500)
                 COLLATE utf8_bin DEFAULT NULL,
  `title`        VARCHAR(200)
                 COLLATE utf8_bin DEFAULT NULL,
  `url`          VARCHAR(100)
                 COLLATE utf8_bin DEFAULT NULL,
  `rcmdpos_id`   BIGINT(20)       DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8BFCD11AF6EF97D6` (`rcmdpos_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `cms_special`;
CREATE TABLE `cms_special` (
  `code`         VARCHAR(50) NOT NULL,
  `create_time`  DATETIME     DEFAULT NULL,
  `creator`      VARCHAR(20)  DEFAULT NULL,
  `modifier`     VARCHAR(20)  DEFAULT NULL,
  `modify_time`  DATETIME     DEFAULT NULL,
  `category`     TINYBLOB,
  `description`  VARCHAR(255) DEFAULT NULL,
  `issn`         VARCHAR(255) DEFAULT NULL,
  `name`         VARCHAR(255) DEFAULT NULL,
  `release_date` DATETIME     DEFAULT NULL,
  `url`          VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `cms_topic`;
CREATE TABLE `cms_topic` (
  `code`         VARCHAR(50) NOT NULL,
  `create_time`  DATETIME     DEFAULT NULL,
  `creator`      VARCHAR(20)  DEFAULT NULL,
  `modifier`     VARCHAR(20)  DEFAULT NULL,
  `modify_time`  DATETIME     DEFAULT NULL,
  `category`     TINYBLOB,
  `description`  VARCHAR(255) DEFAULT NULL,
  `issn`         VARCHAR(255) DEFAULT NULL,
  `name`         VARCHAR(255) DEFAULT NULL,
  `release_date` DATETIME     DEFAULT NULL,
  `url`          VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `contacts_address`;
CREATE TABLE `contacts_address` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `city`        VARCHAR(20)  DEFAULT NULL,
  `country`     VARCHAR(20)  DEFAULT NULL,
  `province`    VARCHAR(20)  DEFAULT NULL,
  `street`      VARCHAR(200) DEFAULT NULL,
  `type`        VARCHAR(10)  DEFAULT NULL,
  `zip_code`    VARCHAR(200) DEFAULT NULL,
  `linkman_id`  BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6524A0087298DAA2` (`linkman_id`),
  CONSTRAINT `FK6524A0087298DAA2` FOREIGN KEY (`linkman_id`) REFERENCES `contacts_linkman` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `contacts_book`;
CREATE TABLE `contacts_book` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `name`        VARCHAR(20) DEFAULT NULL,
  `owner`       VARCHAR(20) DEFAULT NULL,
  `owner_type`  VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `contacts_group`;
CREATE TABLE `contacts_group` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `name`        VARCHAR(20)   DEFAULT NULL,
  `book_id`     BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKED47EF93114DC932` (`book_id`),
  CONSTRAINT `FKED47EF93114DC932` FOREIGN KEY (`book_id`) REFERENCES `contacts_book` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `contacts_group_linkman`;
CREATE TABLE `contacts_group_linkman` (
  `linkman_id` BIGINT(20) NOT NULL,
  `group_id`   BIGINT(20) NOT NULL,
  KEY `FK54528E747298DAA2` (`linkman_id`),
  KEY `FK54528E74BF604442` (`group_id`),
  CONSTRAINT `FK54528E747298DAA2` FOREIGN KEY (`linkman_id`) REFERENCES `contacts_linkman` (`id`),
  CONSTRAINT `FK54528E74BF604442` FOREIGN KEY (`group_id`) REFERENCES `contacts_group` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `contacts_linkman`;
CREATE TABLE `contacts_linkman` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `avatar`      VARCHAR(200)  DEFAULT NULL,
  `company`     VARCHAR(200)  DEFAULT NULL,
  `department`  VARCHAR(200)  DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `email`       VARCHAR(50)   DEFAULT NULL,
  `job`         VARCHAR(200)  DEFAULT NULL,
  `mobile`      VARCHAR(20)   DEFAULT NULL,
  `name`        VARCHAR(20)   DEFAULT NULL,
  `sex`         VARCHAR(20)   DEFAULT NULL,
  `website`     VARCHAR(50)   DEFAULT NULL,
  `book_id`     BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB41B3F34114DC932` (`book_id`),
  CONSTRAINT `FKB41B3F34114DC932` FOREIGN KEY (`book_id`) REFERENCES `contacts_book` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `custom_bean`;
CREATE TABLE `custom_bean` (
  `id`            BIGINT(20) NOT NULL,
  `create_time`   DATETIME     DEFAULT NULL,
  `creator`       VARCHAR(20)  DEFAULT NULL,
  `modifier`      VARCHAR(20)  DEFAULT NULL,
  `modify_time`   DATETIME     DEFAULT NULL,
  `version_id`    BIGINT(20)   DEFAULT NULL,
  `class_name`    VARCHAR(255) DEFAULT NULL,
  `name`          VARCHAR(255) DEFAULT NULL,
  `definition`    TINYBLOB,
  `definition_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6krw69eiq2w7xgsxgdyas3vxg` (`id`, `version_id`),
  KEY `FK_CMS_ARTICLE_VERSION` (`version_id`),
  KEY `FK_lslq7xnw0onndm2jbb2pabnu0` (`definition_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `custom_bean_attribute`;
CREATE TABLE `custom_bean_attribute` (
  `version_id`   BIGINT(20) NOT NULL,
  `attribute_id` BIGINT(20) NOT NULL,
  KEY `FK_q9xlk015l1bms2s5d53nw5gu7` (`attribute_id`),
  KEY `FK_kby1ugit0cvnu5c3mfj3rssrn` (`version_id`),
  CONSTRAINT `FK_kby1ugit0cvnu5c3mfj3rssrn` FOREIGN KEY (`version_id`) REFERENCES `custom_bean_definition` (`id`),
  CONSTRAINT `FK_q9xlk015l1bms2s5d53nw5gu7` FOREIGN KEY (`attribute_id`) REFERENCES `attr_attribute` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `custom_bean_definition`;
CREATE TABLE `custom_bean_definition` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `class_name`  VARCHAR(255) DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `version_id`  BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cq35117r369i1xhelrwuyrt9` (`class_name`),
  KEY `FK_CUSTOM_BEAN_DEFINITION_VERSION` (`version_id`),
  CONSTRAINT `FK_CUSTOM_BEAN_DEFINITION_VERSION` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `file_directory`;
CREATE TABLE `file_directory` (
  `dir_key`                VARCHAR(50) NOT NULL,
  `create_time`            DATETIME     DEFAULT NULL,
  `creator`                VARCHAR(20)  DEFAULT NULL,
  `modifier`               VARCHAR(20)  DEFAULT NULL,
  `modify_time`            DATETIME     DEFAULT NULL,
  `dir_path`               VARCHAR(250) DEFAULT NULL,
  `dir_name`               VARCHAR(250) DEFAULT NULL,
  `file_manager_config_id` VARCHAR(50)  DEFAULT NULL,
  PRIMARY KEY (`dir_key`),
  UNIQUE KEY `dir_key` (`dir_key`),
  KEY `FK_FILE_DIRECTORY_MANAGER` (`file_manager_config_id`),
  CONSTRAINT `FK_FILE_DIRECTORY_MANAGER` FOREIGN KEY (`file_manager_config_id`) REFERENCES `file_manager_config` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `file_filedetail`;
CREATE TABLE `file_filedetail` (
  `absolute_path`            VARCHAR(250) NOT NULL,
  `file_manager_config_id`   VARCHAR(50)  NOT NULL,
  `create_time`              DATETIME     DEFAULT NULL,
  `creator`                  VARCHAR(20)  DEFAULT NULL,
  `modifier`                 VARCHAR(20)  DEFAULT NULL,
  `modify_time`              DATETIME     DEFAULT NULL,
  `content_type`             VARCHAR(50)  DEFAULT NULL,
  `description`              VARCHAR(250) DEFAULT NULL,
  `file_name`                VARCHAR(150) DEFAULT NULL,
  `md5`                      VARCHAR(50)  DEFAULT NULL,
  `real_path`                VARCHAR(250) DEFAULT NULL,
  `length`                   BIGINT(20)   DEFAULT NULL,
  `folder_path`              VARCHAR(250) DEFAULT NULL,
  `folder_manager_config_id` VARCHAR(50)  DEFAULT NULL,
  `ext`                      VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`absolute_path`, `file_manager_config_id`),
  KEY `FKE2325B70FBE76F75` (`file_manager_config_id`),
  KEY `FKE2325B70C5F5D59A` (`folder_path`, `folder_manager_config_id`),
  CONSTRAINT `FKE2325B70C5F5D59A` FOREIGN KEY (`folder_path`, `folder_manager_config_id`) REFERENCES `file_folder` (`absolute_path`, `file_manager_config_id`),
  CONSTRAINT `FKE2325B70FBE76F75` FOREIGN KEY (`file_manager_config_id`) REFERENCES `file_manager_config` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `file_filepart`;
CREATE TABLE `file_filepart` (
  `absolute_path`          VARCHAR(250) NOT NULL,
  `file_manager_config_id` VARCHAR(50)  NOT NULL,
  `create_time`            DATETIME     DEFAULT NULL,
  `creator`                VARCHAR(20)  DEFAULT NULL,
  `modifier`               VARCHAR(20)  DEFAULT NULL,
  `modify_time`            DATETIME     DEFAULT NULL,
  `entire_file_hash`       VARCHAR(255) DEFAULT NULL,
  `paer_index`             INT(11)      DEFAULT NULL,
  `part_file_hash`         VARCHAR(255) DEFAULT NULL,
  `paer_total`             INT(11)      DEFAULT NULL,
  PRIMARY KEY (`absolute_path`, `file_manager_config_id`),
  UNIQUE KEY `entire_file_hash` (`entire_file_hash`, `part_file_hash`),
  UNIQUE KEY `UK_2t3mliklysrygjpubav1jdi0k` (`entire_file_hash`, `part_file_hash`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder` (
  `absolute_path`          VARCHAR(250) NOT NULL,
  `file_manager_config_id` VARCHAR(50)  NOT NULL,
  `create_time`            DATETIME     DEFAULT NULL,
  `creator`                VARCHAR(20)  DEFAULT NULL,
  `modifier`               VARCHAR(20)  DEFAULT NULL,
  `modify_time`            DATETIME     DEFAULT NULL,
  `exts`                   VARCHAR(255) DEFAULT NULL,
  `last`                   BIT(1)       DEFAULT NULL,
  `name`                   VARCHAR(255) DEFAULT NULL,
  `length`                 BIGINT(20)   DEFAULT NULL,
  `type`                   VARCHAR(255) DEFAULT NULL,
  `type_value`             VARCHAR(255) DEFAULT NULL,
  `parent_path`            VARCHAR(250) DEFAULT NULL,
  `p_manager_config_id`    VARCHAR(50)  DEFAULT NULL,
  PRIMARY KEY (`absolute_path`, `file_manager_config_id`),
  KEY `FK63C14091FBE76F75` (`file_manager_config_id`),
  KEY `FK63C14091BD64D660` (`parent_path`, `p_manager_config_id`),
  CONSTRAINT `FK63C14091BD64D660` FOREIGN KEY (`parent_path`, `p_manager_config_id`) REFERENCES `file_folder` (`absolute_path`, `file_manager_config_id`),
  CONSTRAINT `FK63C14091FBE76F75` FOREIGN KEY (`file_manager_config_id`) REFERENCES `file_manager_config` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `file_manager_config`;
CREATE TABLE `file_manager_config` (
  `id`                 VARCHAR(50)  NOT NULL,
  `create_time`        DATETIME     DEFAULT NULL,
  `creator`            VARCHAR(20)  DEFAULT NULL,
  `modifier`           VARCHAR(20)  DEFAULT NULL,
  `modify_time`        DATETIME     DEFAULT NULL,
  `description`        VARCHAR(250) DEFAULT NULL,
  `name`               VARCHAR(150) NOT NULL,
  `type`               VARCHAR(20)  NOT NULL,
  `config_param_store` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `ftl_template`;
CREATE TABLE `ftl_template` (
  `name`        VARCHAR(255)
                COLLATE utf8_bin NOT NULL,
  `create_time` DATETIME         DEFAULT NULL,
  `creator`     VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modifier`    VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modify_time` DATETIME         DEFAULT NULL,
  `content`     LONGTEXT
                COLLATE utf8_bin,
  PRIMARY KEY (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `ftp_config`;
CREATE TABLE `ftp_config` (
  `id`               BIGINT(20) NOT NULL,
  `create_time`      DATETIME     DEFAULT NULL,
  `creator`          VARCHAR(20)  DEFAULT NULL,
  `modifier`         VARCHAR(20)  DEFAULT NULL,
  `modify_time`      DATETIME     DEFAULT NULL,
  `control_encoding` VARCHAR(20)  DEFAULT NULL,
  `default_dir`      VARCHAR(150) DEFAULT NULL,
  `description`      VARCHAR(250) DEFAULT NULL,
  `host_name`        VARCHAR(50)  DEFAULT NULL,
  `name`             VARCHAR(150) DEFAULT NULL,
  `password`         VARCHAR(20)  DEFAULT NULL,
  `port`             INT(11)      DEFAULT NULL,
  `username`         VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `hot_keywords`;
CREATE TABLE `hot_keywords` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `hit_count`   INT(11)      NOT NULL,
  `target_key`  VARCHAR(50)  NOT NULL,
  `keywords`    VARCHAR(150) NOT NULL,
  `time`        VARCHAR(8)  DEFAULT NULL,
  `time_unit`   VARCHAR(8)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `target_key` (`target_key`, `keywords`, `time_unit`, `time`),
  UNIQUE KEY `UK_r05e6njwfbbkkbe9317nw3foq` (`target_key`, `keywords`, `time_unit`, `time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `jdbc_config`;
CREATE TABLE `jdbc_config` (
  `id`              VARCHAR(50) NOT NULL,
  `create_time`     DATETIME     DEFAULT NULL,
  `creator`         VARCHAR(20)  DEFAULT NULL,
  `modifier`        VARCHAR(20)  DEFAULT NULL,
  `modify_time`     DATETIME     DEFAULT NULL,
  `datasource_name` VARCHAR(20)  DEFAULT NULL,
  `password`        VARCHAR(20)  DEFAULT NULL,
  `username`        VARCHAR(20)  DEFAULT NULL,
  `name`            VARCHAR(150) DEFAULT NULL,
  `host_name`       VARCHAR(50)  DEFAULT NULL,
  `port`            INT(11)      DEFAULT NULL,
  `type`            VARCHAR(255) DEFAULT NULL,
  `database_name`   VARCHAR(20)  DEFAULT NULL,
  `jndi_name`       VARCHAR(20)  DEFAULT NULL,
  `driver`          VARCHAR(150) DEFAULT NULL,
  `driver_url`      VARCHAR(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `keywords`;
CREATE TABLE `keywords` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `type`        INT(11)      DEFAULT NULL,
  `words`       VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_brand`;
CREATE TABLE `mall_brand` (
  `id`               BIGINT(20)   NOT NULL,
  `create_time`      DATETIME      DEFAULT NULL,
  `creator`          VARCHAR(20)   DEFAULT NULL,
  `modifier`         VARCHAR(20)   DEFAULT NULL,
  `modify_time`      DATETIME      DEFAULT NULL,
  `eng_name`         VARCHAR(100)  DEFAULT NULL,
  `introduction`     VARCHAR(3000) DEFAULT NULL,
  `logo_image_store` VARCHAR(500)  DEFAULT NULL,
  `name`             VARCHAR(255) NOT NULL,
  `nation`           VARCHAR(20)   DEFAULT NULL,
  `url`              VARCHAR(150)  DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_cart`;
CREATE TABLE `mall_cart` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `owner`       VARCHAR(50) DEFAULT NULL,
  `owner_type`  INT(11)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `owner` (`owner`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_cart_item`;
CREATE TABLE `mall_cart_item` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME       DEFAULT NULL,
  `creator`     VARCHAR(20)    DEFAULT NULL,
  `modifier`    VARCHAR(20)    DEFAULT NULL,
  `modify_time` DATETIME       DEFAULT NULL,
  `price`       DECIMAL(19, 2) DEFAULT NULL,
  `quantity`    INT(11)        DEFAULT NULL,
  `cart_id`     BIGINT(20)     DEFAULT NULL,
  `product_id`  BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ITEM_CART` (`cart_id`),
  KEY `FK_CART_ITEM_PRODUCT` (`product_id`),
  CONSTRAINT `FK_CART_ITEM_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `mall_product` (`id`),
  CONSTRAINT `FK_ITEM_CART` FOREIGN KEY (`cart_id`) REFERENCES `mall_cart` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_delivery_corp`;
CREATE TABLE `mall_delivery_corp` (
  `id`          BIGINT(20)  NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `name`        VARCHAR(50) NOT NULL,
  `sort`        INT(11)       DEFAULT NULL,
  `url`         VARCHAR(50)   DEFAULT NULL,
  `description` VARCHAR(3000) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_delivery_item`;
CREATE TABLE `mall_delivery_item` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `name`        VARCHAR(255) NOT NULL,
  `quantity`    INT(11)      NOT NULL,
  `sn`          VARCHAR(255) NOT NULL,
  `product_id`  BIGINT(20)  DEFAULT NULL,
  `reship_id`   BIGINT(20)  DEFAULT NULL,
  `shipping_id` BIGINT(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_DELIVERY_ITEM_SHIPPING` (`shipping_id`),
  KEY `FK_DELIVERY_ITEM_RESHIP` (`reship_id`),
  KEY `FK_DELIVERY_ITEM_PRODUCT` (`product_id`),
  CONSTRAINT `FK_DELIVERY_ITEM_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `mall_product` (`id`),
  CONSTRAINT `FK_DELIVERY_ITEM_RESHIP` FOREIGN KEY (`reship_id`) REFERENCES `mall_delivery_reship` (`id`),
  CONSTRAINT `FK_DELIVERY_ITEM_SHIPPING` FOREIGN KEY (`shipping_id`) REFERENCES `mall_delivery_shipping` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_delivery_reship`;
CREATE TABLE `mall_delivery_reship` (
  `id`                 BIGINT(20)     NOT NULL,
  `create_time`        DATETIME     DEFAULT NULL,
  `creator`            VARCHAR(20)  DEFAULT NULL,
  `modifier`           VARCHAR(20)  DEFAULT NULL,
  `modify_time`        DATETIME     DEFAULT NULL,
  `delivery_corp_name` VARCHAR(50)    NOT NULL,
  `delivery_fee`       DECIMAL(10, 2) NOT NULL,
  `delivery_sn`        VARCHAR(50)  DEFAULT NULL,
  `delivery_type_name` VARCHAR(50)  DEFAULT NULL,
  `memo`               VARCHAR(150) DEFAULT NULL,
  `reship_address`     VARCHAR(150)   NOT NULL,
  `reship_area_store`  VARCHAR(300)   NOT NULL,
  `reship_mobile`      VARCHAR(12)  DEFAULT NULL,
  `reship_name`        VARCHAR(50)    NOT NULL,
  `reship_phone`       VARCHAR(12)  DEFAULT NULL,
  `reship_zip_code`    VARCHAR(10)    NOT NULL,
  `sn`                 VARCHAR(255)   NOT NULL,
  `delivery_type_id`   BIGINT(20)   DEFAULT NULL,
  `order_id`           BIGINT(20)   DEFAULT NULL,
  `order_sn`           VARCHAR(255) DEFAULT NULL,
  `order_type`         VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  KEY `FK_RESHIP_DELIVERY_TYPE` (`delivery_type_id`),
  KEY `FK_RESHIP_ORDER` (`order_id`),
  CONSTRAINT `FK_RESHIP_DELIVERY_TYPE` FOREIGN KEY (`delivery_type_id`) REFERENCES `mall_delivery_type` (`id`),
  CONSTRAINT `FK_RESHIP_ORDER` FOREIGN KEY (`order_id`) REFERENCES `mall_order` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_delivery_shipping`;
CREATE TABLE `mall_delivery_shipping` (
  `id`                 BIGINT(20)   NOT NULL,
  `create_time`        DATETIME       DEFAULT NULL,
  `creator`            VARCHAR(20)    DEFAULT NULL,
  `modifier`           VARCHAR(20)    DEFAULT NULL,
  `modify_time`        DATETIME       DEFAULT NULL,
  `delivery_corp_name` VARCHAR(50)    DEFAULT NULL,
  `delivery_corp_url`  VARCHAR(50)    DEFAULT NULL,
  `delivery_fee`       DECIMAL(10, 2) DEFAULT NULL,
  `delivery_sn`        VARCHAR(50)    DEFAULT NULL,
  `delivery_type_name` VARCHAR(50)    DEFAULT NULL,
  `memo`               VARCHAR(150)   DEFAULT NULL,
  `ship_address`       VARCHAR(150)   DEFAULT NULL,
  `ship_area_store`    VARCHAR(300)   DEFAULT NULL,
  `ship_mobile`        VARCHAR(12)    DEFAULT NULL,
  `ship_name`          VARCHAR(50)    DEFAULT NULL,
  `ship_zip_code`      VARCHAR(10)    DEFAULT NULL,
  `sn`                 VARCHAR(255) NOT NULL,
  `delivery_type_id`   BIGINT(20)     DEFAULT NULL,
  `order_sn`           VARCHAR(255)   DEFAULT NULL,
  `order_type`         VARCHAR(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  KEY `FK_SHIPPING_DELIVERY_TYPE` (`delivery_type_id`),
  CONSTRAINT `FK_SHIPPING_DELIVERY_TYPE` FOREIGN KEY (`delivery_type_id`) REFERENCES `mall_delivery_type` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_delivery_type`;
CREATE TABLE `mall_delivery_type` (
  `id`                    BIGINT(20)     NOT NULL,
  `create_time`           DATETIME      DEFAULT NULL,
  `creator`               VARCHAR(20)   DEFAULT NULL,
  `modifier`              VARCHAR(20)   DEFAULT NULL,
  `modify_time`           DATETIME      DEFAULT NULL,
  `continue_weight`       INT(11)        NOT NULL,
  `continue_weight_price` DECIMAL(15, 5) NOT NULL,
  `description`           VARCHAR(3000) DEFAULT NULL,
  `first_weight`          INT(11)        NOT NULL,
  `first_weight_price`    DECIMAL(15, 5) NOT NULL,
  `delivery_method`       VARCHAR(50)    NOT NULL,
  `name`                  VARCHAR(255)   NOT NULL,
  `delivery_corp_id`      BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MALL_DELIVERY_TYPE_CORP` (`delivery_corp_id`),
  CONSTRAINT `MALL_DELIVERY_TYPE_CORP` FOREIGN KEY (`delivery_corp_id`) REFERENCES `mall_delivery_corp` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_famouswine`;
CREATE TABLE `mall_famouswine` (
  `id`                BIGINT(20) NOT NULL,
  `create_time`       DATETIME         DEFAULT NULL,
  `creator`           VARCHAR(255)
                      COLLATE utf8_bin DEFAULT NULL,
  `modifier`          VARCHAR(255)
                      COLLATE utf8_bin DEFAULT NULL,
  `modify_time`       DATETIME         DEFAULT NULL,
  `goods_image_store` VARCHAR(3000)
                      COLLATE utf8_bin DEFAULT NULL,
  `name`              VARCHAR(1000)
                      COLLATE utf8_bin DEFAULT NULL,
  `remark`            VARCHAR(1000)
                      COLLATE utf8_bin DEFAULT NULL,
  `sort`              BIGINT(20)       DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `mall_favorite_member`;
CREATE TABLE `mall_favorite_member` (
  `member_id` BIGINT(20) NOT NULL,
  `goods_id`  BIGINT(20) NOT NULL,
  KEY `FK_MEMBER_FAVORITE_GOODS` (`goods_id`),
  KEY `FK_GOODS_FAVORITE_MEMBER` (`member_id`),
  CONSTRAINT `FK_GOODS_FAVORITE_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`),
  CONSTRAINT `FK_MEMBER_FAVORITE_GOODS` FOREIGN KEY (`goods_id`) REFERENCES `mall_goods` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_goods`;
CREATE TABLE `mall_goods` (
  `id`                      BIGINT(20)     NOT NULL,
  `create_time`             DATETIME       DEFAULT NULL,
  `creator`                 VARCHAR(20)    DEFAULT NULL,
  `modifier`                VARCHAR(20)    DEFAULT NULL,
  `modify_time`             DATETIME       DEFAULT NULL,
  `cost`                    DECIMAL(15, 5) DEFAULT NULL,
  `eng_name`                VARCHAR(100)   DEFAULT NULL,
  `freeze_store`            INT(11)        DEFAULT NULL,
  `goods_image_store`       VARCHAR(3000)  DEFAULT NULL,
  `goods_param_value_store` VARCHAR(3000)  DEFAULT NULL,
  `introduction`            LONGTEXT,
  `market_price`            DECIMAL(19, 2) NOT NULL,
  `marketable`              BIT(1)         NOT NULL,
  `meta_description`        VARCHAR(1000)  DEFAULT NULL,
  `meta_keywords`           VARCHAR(1000)  DEFAULT NULL,
  `month_sale_count`        INT(11)        NOT NULL,
  `name`                    VARCHAR(50)    DEFAULT NULL,
  `price`                   DECIMAL(19, 2) NOT NULL,
  `sale_count`              INT(11)        NOT NULL,
  `score`                   INT(11)        DEFAULT NULL,
  `sn`                      VARCHAR(255)   NOT NULL,
  `specification_enabled`   BIT(1)         NOT NULL,
  `store`                   INT(11)        DEFAULT NULL,
  `weight`                  INT(11)        DEFAULT NULL,
  `brand_id`                BIGINT(20)     DEFAULT NULL,
  `goods_category_id`       BIGINT(20)     NOT NULL,
  `version_id`              BIGINT(20)     DEFAULT NULL,
  `shop_id`                 BIGINT(20)     DEFAULT NULL,
  `attribute_value_store`   LONGTEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  UNIQUE KEY `UK_h7921o349mqipuhr1nv56fghn` (`id`, `version_id`),
  KEY `FK_GOODS_CATEGORY` (`goods_category_id`),
  KEY `FK_GOODS_BRAND` (`brand_id`),
  KEY `FK_GOODS_SHOP` (`shop_id`),
  KEY `FK_MALL_GOODS_VERSION` (`version_id`),
  CONSTRAINT `FK_GOODS_BRAND` FOREIGN KEY (`brand_id`) REFERENCES `mall_brand` (`id`),
  CONSTRAINT `FK_GOODS_CATEGORY` FOREIGN KEY (`goods_category_id`) REFERENCES `mall_goods_category` (`id`),
  CONSTRAINT `FK_GOODS_SHOP` FOREIGN KEY (`shop_id`) REFERENCES `mall_shop` (`id`),
  CONSTRAINT `FK_MALL_GOODS_VERSION` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_goods_attribute`;
CREATE TABLE `mall_goods_attribute` (
  `category_id`  BIGINT(20) NOT NULL,
  `attribute_id` BIGINT(20) NOT NULL,
  KEY `FK45393CC86DB5714F` (`category_id`),
  KEY `FK45393CC89DD6B344` (`attribute_id`),
  CONSTRAINT `FK45393CC86DB5714F` FOREIGN KEY (`category_id`) REFERENCES `mall_goods_category` (`id`),
  CONSTRAINT `FK45393CC89DD6B344` FOREIGN KEY (`attribute_id`) REFERENCES `attr_attribute` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_goods_category`;
CREATE TABLE `mall_goods_category` (
  `id`                    BIGINT(20)   NOT NULL,
  `create_time`           DATETIME      DEFAULT NULL,
  `creator`               VARCHAR(20)   DEFAULT NULL,
  `modifier`              VARCHAR(20)   DEFAULT NULL,
  `modify_time`           DATETIME      DEFAULT NULL,
  `brand_custom_sort`     VARCHAR(100)  DEFAULT NULL,
  `countries`             VARCHAR(2000) DEFAULT NULL,
  `goods_attribute_store` VARCHAR(3000) DEFAULT NULL,
  `goods_parameter_store` VARCHAR(3000) DEFAULT NULL,
  `layer`                 INT(11)      NOT NULL,
  `meta_description`      VARCHAR(3000) DEFAULT NULL,
  `meta_keywords`         VARCHAR(3000) DEFAULT NULL,
  `name`                  VARCHAR(255) NOT NULL,
  `path`                  VARCHAR(200) NOT NULL,
  `sign`                  VARCHAR(255) NOT NULL,
  `sort`                  INT(11)       DEFAULT NULL,
  `p_id`                  BIGINT(20)    DEFAULT NULL,
  `article_version_id`    BIGINT(20)    DEFAULT NULL,
  `version_id`            BIGINT(20)    DEFAULT NULL,
  `attribute_value_store` LONGTEXT,
  `goods_version_id`      BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sign` (`sign`),
  UNIQUE KEY `UK_shdg5thncxrm6luqs5ew914c7` (`id`, `version_id`),
  KEY `FK_GOODS_CATEGORY_PARENT` (`p_id`),
  KEY `FK_MALL_CATEGORY_VERSION` (`version_id`),
  KEY `FK_MALL_GOODS_CATEGORY_VERSION` (`goods_version_id`),
  CONSTRAINT `FK_MALL_GOODS_CATEGORY_VERSION` FOREIGN KEY (`goods_version_id`) REFERENCES `attr_version` (`id`),
  CONSTRAINT `FK_GOODS_CATEGORY_PARENT` FOREIGN KEY (`p_id`) REFERENCES `mall_goods_category` (`id`),
  CONSTRAINT `FK_MALL_CATEGORY_VERSION` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_goods_category_brand`;
CREATE TABLE `mall_goods_category_brand` (
  `goods_category_id` BIGINT(20) DEFAULT NULL,
  `brand_id`          BIGINT(20) DEFAULT NULL,
  KEY `FK_GOODS_TYPE_BRANDS` (`goods_category_id`),
  KEY `FK_BRAND_GOODS_CATEGORIE` (`brand_id`),
  CONSTRAINT `FK_BRAND_GOODS_CATEGORIE` FOREIGN KEY (`brand_id`) REFERENCES `mall_brand` (`id`),
  CONSTRAINT `FK_GOODS_TYPE_BRANDS` FOREIGN KEY (`goods_category_id`) REFERENCES `mall_goods_category` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_goods_flash_sale`;
CREATE TABLE `mall_goods_flash_sale` (
  `id`          BIGINT(20)       NOT NULL,
  `create_time` DATETIME         DEFAULT NULL,
  `creator`     VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modifier`    VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modify_time` DATETIME         DEFAULT NULL,
  `end_time`    DATETIME         NOT NULL,
  `name`        VARCHAR(200)
                COLLATE utf8_bin NOT NULL,
  `pre_time`    DATETIME         NOT NULL,
  `start_time`  DATETIME         NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `mall_goods_flash_sale_item`;
CREATE TABLE `mall_goods_flash_sale_item` (
  `id`                BIGINT(20) NOT NULL,
  `create_time`       DATETIME         DEFAULT NULL,
  `creator`           VARCHAR(255)
                      COLLATE utf8_bin DEFAULT NULL,
  `modifier`          VARCHAR(255)
                      COLLATE utf8_bin DEFAULT NULL,
  `modify_time`       DATETIME         DEFAULT NULL,
  `goods_image_store` VARCHAR(3000)
                      COLLATE utf8_bin DEFAULT NULL,
  `sort`              BIGINT(20)       DEFAULT NULL,
  `flash_sale_id`     BIGINT(20) NOT NULL,
  `goods_id`          BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SALE_ITEM_GOODS` (`goods_id`),
  KEY `FK_SALE_ITEM_SALE` (`flash_sale_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `mall_goods_notify`;
CREATE TABLE `mall_goods_notify` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `email`       VARCHAR(255) NOT NULL,
  `send_date`   DATETIME    DEFAULT NULL,
  `sent`        BIT(1)       NOT NULL,
  `member_id`   BIGINT(20)  DEFAULT NULL,
  `product_id`  BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_GOODS_NOTIFY_MEMBER` (`member_id`),
  KEY `FK_GOODS_NOTIFY_PRODUCT` (`product_id`),
  CONSTRAINT `FK_GOODS_NOTIFY_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`),
  CONSTRAINT `FK_GOODS_NOTIFY_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `mall_product` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_goods_specification`;
CREATE TABLE `mall_goods_specification` (
  `specification_id` BIGINT(20) NOT NULL,
  `goods_id`         BIGINT(20) NOT NULL,
  KEY `FK_GOODS_SPECIFICATION` (`goods_id`),
  KEY `FK_SPECIFICATION_GOODS` (`specification_id`),
  CONSTRAINT `FK_GOODS_SPECIFICATION` FOREIGN KEY (`goods_id`) REFERENCES `mall_goods` (`id`),
  CONSTRAINT `FK_SPECIFICATION_GOODS` FOREIGN KEY (`specification_id`) REFERENCES `mall_specification` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_mem_comment`;
CREATE TABLE `mall_mem_comment` (
  `id`             BIGINT(20)   NOT NULL,
  `create_time`    DATETIME     DEFAULT NULL,
  `creator`        VARCHAR(20)  DEFAULT NULL,
  `modifier`       VARCHAR(20)  DEFAULT NULL,
  `modify_time`    DATETIME     DEFAULT NULL,
  `admin_reply`    BIT(1)       NOT NULL,
  `contact`        VARCHAR(255) NOT NULL,
  `content`        LONGTEXT     NOT NULL,
  `ip`             VARCHAR(15)  NOT NULL,
  `is_show`        BIT(1)       NOT NULL,
  `username`       VARCHAR(255) DEFAULT NULL,
  `for_comment_id` BIGINT(20)   DEFAULT NULL,
  `goods_id`       BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COMMENT_GOODS` (`goods_id`),
  KEY `FK_COMMENT_FOR_COMMENT` (`for_comment_id`),
  CONSTRAINT `FK_COMMENT_FOR_COMMENT` FOREIGN KEY (`for_comment_id`) REFERENCES `mall_mem_comment` (`id`),
  CONSTRAINT `FK_COMMENT_GOODS` FOREIGN KEY (`goods_id`) REFERENCES `mall_goods` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_mem_receiver`;
CREATE TABLE `mall_mem_receiver` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `address`     VARCHAR(200) NOT NULL,
  `area_store`  VARCHAR(300) NOT NULL,
  `is_default`  BIT(1)       NOT NULL,
  `mobile`      VARCHAR(200) NOT NULL,
  `name`        VARCHAR(20)  NOT NULL,
  `phone`       VARCHAR(200) NOT NULL,
  `zip_code`    VARCHAR(200) NOT NULL,
  `member_id`   BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SHIP_ADDRESS_MEMBER` (`member_id`),
  CONSTRAINT `FK_SHIP_ADDRESS_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_order`;
CREATE TABLE `mall_order` (
  `id`                     BIGINT(20)     NOT NULL,
  `create_time`            DATETIME      DEFAULT NULL,
  `creator`                VARCHAR(20)   DEFAULT NULL,
  `modifier`               VARCHAR(20)   DEFAULT NULL,
  `modify_time`            DATETIME      DEFAULT NULL,
  `delivery_fee`           DECIMAL(15, 5) NOT NULL,
  `delivery_type_name`     VARCHAR(100)   NOT NULL,
  `goods_id_list_store`    VARCHAR(3000) DEFAULT NULL,
  `memo`                   VARCHAR(255)   NOT NULL,
  `order_status`           VARCHAR(20)    NOT NULL,
  `paid_amount`            DECIMAL(15, 5) NOT NULL,
  `payment_config_name`    VARCHAR(255)   NOT NULL,
  `payment_fee`            DECIMAL(15, 5) NOT NULL,
  `payment_status`         VARCHAR(20)    NOT NULL,
  `ship_address`           VARCHAR(255)   NOT NULL,
  `ship_area_store`        VARCHAR(255)   NOT NULL,
  `ship_mobile`            VARCHAR(255)   NOT NULL,
  `ship_name`              VARCHAR(255)   NOT NULL,
  `ship_phone`             VARCHAR(255)   NOT NULL,
  `ship_zip_code`          VARCHAR(255)   NOT NULL,
  `shipping_status`        VARCHAR(20)    NOT NULL,
  `sn`                     VARCHAR(255)   NOT NULL,
  `total_amount`           DECIMAL(15, 5) NOT NULL,
  `total_product_price`    DECIMAL(15, 5) NOT NULL,
  `total_product_quantity` INT(11)        NOT NULL,
  `total_product_weight`   INT(11)        NOT NULL,
  `delivery_type_id`       BIGINT(20)    DEFAULT NULL,
  `member_id`              BIGINT(20)    DEFAULT NULL,
  `payment_config_id`      BIGINT(20)    DEFAULT NULL,
  `order_type`             VARCHAR(20)   DEFAULT NULL,
  `version_id`             BIGINT(20)    DEFAULT NULL,
  `attribute_value_store`  LONGTEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  UNIQUE KEY `UK_rxrys5i15kb7jn9wp4pynbsfa` (`id`, `version_id`),
  KEY `FK_ORDER_DELIVERY_TYPE` (`delivery_type_id`),
  KEY `FK_ORDER_MEMBER` (`member_id`),
  KEY `FK_ORDER_PAYMENT_CONFIG` (`payment_config_id`),
  KEY `FK_2o69xi1iv214548abuh0oi9jf` (`version_id`),
  CONSTRAINT `FK_2o69xi1iv214548abuh0oi9jf` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`),
  CONSTRAINT `FK_ORDER_DELIVERY_TYPE` FOREIGN KEY (`delivery_type_id`) REFERENCES `mall_delivery_type` (`id`),
  CONSTRAINT `FK_ORDER_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`),
  CONSTRAINT `FK_ORDER_PAYMENT_CONFIG` FOREIGN KEY (`payment_config_id`) REFERENCES `mall_payment_config` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_order_item`;
CREATE TABLE `mall_order_item` (
  `id`                BIGINT(20)     NOT NULL,
  `create_time`       DATETIME      DEFAULT NULL,
  `creator`           VARCHAR(20)   DEFAULT NULL,
  `modifier`          VARCHAR(20)   DEFAULT NULL,
  `modify_time`       DATETIME      DEFAULT NULL,
  `attr`              VARCHAR(4000) DEFAULT NULL,
  `delivery_quantity` INT(11)        NOT NULL,
  `name`              VARCHAR(255)   NOT NULL,
  `product_price`     DECIMAL(15, 5) NOT NULL,
  `product_quantity`  INT(11)        NOT NULL,
  `sn`                VARCHAR(255)   NOT NULL,
  `order_id`          BIGINT(20)     NOT NULL,
  `product_id`        BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORDER_ITEM_ORDER` (`order_id`),
  KEY `FK_ORDER_ITEM_PRODUCT` (`product_id`),
  CONSTRAINT `FK_ORDER_ITEM_ORDER` FOREIGN KEY (`order_id`) REFERENCES `mall_order` (`id`),
  CONSTRAINT `FK_ORDER_ITEM_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `mall_product` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_payment`;
CREATE TABLE `mall_payment` (
  `id`                  BIGINT(20)     NOT NULL,
  `create_time`         DATETIME      DEFAULT NULL,
  `creator`             VARCHAR(20)   DEFAULT NULL,
  `modifier`            VARCHAR(20)   DEFAULT NULL,
  `modify_time`         DATETIME      DEFAULT NULL,
  `bank_account`        VARCHAR(255)  DEFAULT NULL,
  `bank_name`           VARCHAR(255)  DEFAULT NULL,
  `memo`                VARCHAR(3000) DEFAULT NULL,
  `payer`               VARCHAR(255)  DEFAULT NULL,
  `payment_config_name` VARCHAR(255)   NOT NULL,
  `payment_fee`         DECIMAL(15, 5) NOT NULL,
  `payment_status`      VARCHAR(255)   NOT NULL,
  `payment_type`        VARCHAR(255)   NOT NULL,
  `sn`                  VARCHAR(255)   NOT NULL,
  `total_amount`        DECIMAL(15, 5) NOT NULL,
  `member_id`           BIGINT(20)    DEFAULT NULL,
  `order_id`            BIGINT(20)    DEFAULT NULL,
  `payment_config_id`   BIGINT(20)    DEFAULT NULL,
  `order_sn`            VARCHAR(255)  DEFAULT NULL,
  `order_type`          VARCHAR(255)  DEFAULT NULL,
  `trade_no`            VARCHAR(255)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  KEY `FK_PAYMENT_MEMBER` (`member_id`),
  KEY `FK_PAYMENT_PAYMENT_CONFIG` (`payment_config_id`),
  KEY `FK_PAYMENT_ORDER` (`order_id`),
  CONSTRAINT `FK_PAYMENT_MEMBER` FOREIGN KEY (`member_id`) REFERENCES `mem_member` (`id`),
  CONSTRAINT `FK_PAYMENT_ORDER` FOREIGN KEY (`order_id`) REFERENCES `mall_order` (`id`),
  CONSTRAINT `FK_PAYMENT_PAYMENT_CONFIG` FOREIGN KEY (`payment_config_id`) REFERENCES `mall_payment_config` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_payment_config`;
CREATE TABLE `mall_payment_config` (
  `id`                  BIGINT(20)     NOT NULL,
  `create_time`         DATETIME      DEFAULT NULL,
  `creator`             VARCHAR(20)   DEFAULT NULL,
  `modifier`            VARCHAR(20)   DEFAULT NULL,
  `modify_time`         DATETIME      DEFAULT NULL,
  `bargainor_id`        VARCHAR(255)  DEFAULT NULL,
  `bargainor_key`       VARCHAR(255)  DEFAULT NULL,
  `description`         VARCHAR(3000) DEFAULT NULL,
  `name`                VARCHAR(255)   NOT NULL,
  `payment_config_type` VARCHAR(255)   NOT NULL,
  `payment_fee`         DECIMAL(15, 5) NOT NULL,
  `payment_fee_type`    VARCHAR(255)   NOT NULL,
  `payment_product_id`  VARCHAR(255)  DEFAULT NULL,
  `sort`                INT(11)       DEFAULT NULL,
  `seller_email`        VARCHAR(255)  DEFAULT NULL,
    PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_payment_refund`;
CREATE TABLE `mall_payment_refund` (
  `id`                  BIGINT(20)     NOT NULL,
  `create_time`         DATETIME      DEFAULT NULL,
  `creator`             VARCHAR(20)   DEFAULT NULL,
  `modifier`            VARCHAR(20)   DEFAULT NULL,
  `modify_time`         DATETIME      DEFAULT NULL,
  `bank_account`        VARCHAR(255)  DEFAULT NULL,
  `bank_name`           VARCHAR(255)  DEFAULT NULL,
  `memo`                VARCHAR(3000) DEFAULT NULL,
  `payee`               VARCHAR(255)  DEFAULT NULL,
  `payment_config_name` VARCHAR(255)   NOT NULL,
  `refund_type`         VARCHAR(255)   NOT NULL,
  `sn`                  VARCHAR(255)   NOT NULL,
  `total_amount`        DECIMAL(15, 5) NOT NULL,
  `order_id`            BIGINT(20)    DEFAULT NULL,
  `payment_config_id`   BIGINT(20)    DEFAULT NULL,
  `order_sn`            VARCHAR(255)  DEFAULT NULL,
  `order_type`          VARCHAR(255)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  KEY `FK_REFUND_PAYMENT_CONFIG` (`payment_config_id`),
  KEY `FK_REFUND_ORDER` (`order_id`),
  CONSTRAINT `FK_REFUND_ORDER` FOREIGN KEY (`order_id`) REFERENCES `mall_order` (`id`),
  CONSTRAINT `FK_REFUND_PAYMENT_CONFIG` FOREIGN KEY (`payment_config_id`) REFERENCES `mall_payment_config` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_product`;
CREATE TABLE `mall_product` (
  `id`                        BIGINT(20)     NOT NULL,
  `create_time`               DATETIME       DEFAULT NULL,
  `creator`                   VARCHAR(20)    DEFAULT NULL,
  `modifier`                  VARCHAR(20)    DEFAULT NULL,
  `modify_time`               DATETIME       DEFAULT NULL,
  `cost`                      DECIMAL(15, 5) DEFAULT NULL,
  `freeze_store`              INT(11)        NOT NULL,
  `goods_image_store`         VARCHAR(3000)  DEFAULT NULL,
  `is_default`                BIT(1)         NOT NULL,
  `market_price`              DECIMAL(15, 5) NOT NULL,
  `marketable`                BIT(1)         NOT NULL,
  `name`                      VARCHAR(255)   NOT NULL,
  `price`                     DECIMAL(15, 5) NOT NULL,
  `sn`                        VARCHAR(255)   NOT NULL,
  `specification_value_store` VARCHAR(3000)  DEFAULT NULL,
  `store`                     INT(11)        NOT NULL,
  `store_place`               VARCHAR(255)   DEFAULT NULL,
  `weight`                    INT(11)        NOT NULL,
  `goods_id`                  BIGINT(20)     NOT NULL,
  `warningsettings_id`        BIGINT(20)     DEFAULT NULL,
  `version_id`                BIGINT(20)     DEFAULT NULL,
  `attribute_value_store`     LONGTEXT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sn` (`sn`),
  UNIQUE KEY `UK_j9woh53twugc0e3bn3cuwi5m3` (`id`, `version_id`),
  KEY `FK_PRODUCT_GOODS` (`goods_id`),
  KEY `FK_PRODUCT_WARNINGSETTINGS` (`warningsettings_id`),
  KEY `FK_MALL_PRODUCT_VERSION` (`version_id`),
  CONSTRAINT `FK_MALL_PRODUCT_VERSION` FOREIGN KEY (`version_id`) REFERENCES `attr_version` (`id`),
  CONSTRAINT `FK_PRODUCT_GOODS` FOREIGN KEY (`goods_id`) REFERENCES `mall_goods` (`id`),
  CONSTRAINT `FK_PRODUCT_WARNINGSETTINGS` FOREIGN KEY (`warningsettings_id`) REFERENCES `mall_warning_settings` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_sales`;
CREATE TABLE `mall_sales` (
  `id`              BIGINT(20) NOT NULL,
  `create_time`     DATETIME       DEFAULT NULL,
  `creator`         VARCHAR(20)    DEFAULT NULL,
  `modifier`        VARCHAR(20)    DEFAULT NULL,
  `modify_time`     DATETIME       DEFAULT NULL,
  `amount`          DECIMAL(19, 2) DEFAULT NULL,
  `path`            VARCHAR(200)   DEFAULT NULL,
  `quantity`        INT(11)        DEFAULT NULL,
  `return_amount`   INT(11)        DEFAULT NULL,
  `return_quantity` INT(11)        DEFAULT NULL,
  `sn`              VARCHAR(50)    DEFAULT NULL,
  `time`            VARCHAR(8)     DEFAULT NULL,
  `time_unit`       VARCHAR(8)     DEFAULT NULL,
  `type`            VARCHAR(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`, `sn`, `time_unit`, `time`),
  UNIQUE KEY `UK_n0ma8ny1jx0xdg45y3sowke6` (`type`, `sn`, `time_unit`, `time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_shop`;
CREATE TABLE `mall_shop` (
  `id`          BIGINT(20)  NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `name`        VARCHAR(50) NOT NULL,
  `shop_id`     BIGINT(20)  NOT NULL,
  PRIMARY KEY (`shop_id`),
  KEY `FK_GOODS_SHOP` (`id`),
  KEY `FK8C523A41B3F2D439` (`shop_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_specification`;
CREATE TABLE `mall_specification` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `name`        VARCHAR(20)   DEFAULT NULL,
  `remark`      VARCHAR(500)  DEFAULT NULL,
  `sort`        INT(11)       DEFAULT NULL,
  `type`        INT(11)       DEFAULT NULL,
  `value_store` VARCHAR(3000) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_stock`;
CREATE TABLE `mall_stock` (
  `id`            BIGINT(20)   NOT NULL,
  `create_time`   DATETIME    DEFAULT NULL,
  `creator`       VARCHAR(20) DEFAULT NULL,
  `modifier`      VARCHAR(20) DEFAULT NULL,
  `modify_time`   DATETIME    DEFAULT NULL,
  `change_number` INT(11)      NOT NULL,
  `remark`        VARCHAR(255) NOT NULL,
  `status`        BIT(1)       NOT NULL,
  `producgt_id`   BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PRODUCT_STOCE` (`producgt_id`),
  CONSTRAINT `FK_PRODUCT_STOCE` FOREIGN KEY (`producgt_id`) REFERENCES `mall_product` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mall_warning_settings`;
CREATE TABLE `mall_warning_settings` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `expression`  VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mem_comment`;
CREATE TABLE `mem_comment` (
  `id`             BIGINT(20)    NOT NULL,
  `create_time`    DATETIME     DEFAULT NULL,
  `creator`        VARCHAR(20)  DEFAULT NULL,
  `modifier`       VARCHAR(20)  DEFAULT NULL,
  `modify_time`    DATETIME     DEFAULT NULL,
  `admin_reply`    BIT(1)        NOT NULL,
  `contact`        VARCHAR(255)  NOT NULL,
  `content`        LONGTEXT      NOT NULL,
  `ip`             VARCHAR(15)   NOT NULL,
  `path`           VARCHAR(1000) NOT NULL,
  `is_show`        BIT(1)        NOT NULL,
  `target_id`      BIGINT(20)    NOT NULL,
  `target_type`    VARCHAR(255)  NOT NULL,
  `username`       VARCHAR(255) DEFAULT NULL,
  `for_comment_id` BIGINT(20)   DEFAULT NULL,
  `member_id`      BIGINT(20)    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mem_member`;
CREATE TABLE `mem_member` (
  `id`                      BIGINT(20)  NOT NULL,
  `create_time`             DATETIME    DEFAULT NULL,
  `creator`                 VARCHAR(20) DEFAULT NULL,
  `modifier`                VARCHAR(20) DEFAULT NULL,
  `modify_time`             DATETIME    DEFAULT NULL,
  `non_expired`             BIT(1)      DEFAULT NULL,
  `non_locked`              BIT(1)      DEFAULT NULL,
  `credentials_non_expired` BIT(1)      DEFAULT NULL,
  `enabled`                 BIT(1)      DEFAULT NULL,
  `last_login_time`         DATETIME    DEFAULT NULL,
  `lock_time`               DATETIME    DEFAULT NULL,
  `nick_name`               VARCHAR(50) DEFAULT NULL,
  `password`                VARCHAR(20) NOT NULL,
  `username`                VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mem_member_details`;
CREATE TABLE `mem_member_details` (
  `member_id`    BIGINT(20) NOT NULL,
  `birthday`     DATETIME     DEFAULT NULL,
  `description`  VARCHAR(255) DEFAULT NULL,
  `email`        VARCHAR(50)  DEFAULT NULL,
  `mail_valid`   BIT(1)     NOT NULL,
  `mobile`       VARCHAR(20)  DEFAULT NULL,
  `mobile_valid` BIT(1)     NOT NULL,
  `name`         VARCHAR(20)  DEFAULT NULL,
  `score`        INT(11)      DEFAULT NULL,
  `sex`          VARCHAR(20)  DEFAULT NULL,
  `tel`          VARCHAR(20)  DEFAULT NULL,
  `vip`          BIT(1)       DEFAULT NULL,
  `website`      VARCHAR(50)  DEFAULT NULL,
  `avatar`       VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`member_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mem_points`;
CREATE TABLE `mem_points` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(255)  DEFAULT NULL,
  `point`       INT(11)      NOT NULL,
  `status`      VARCHAR(20)  NOT NULL,
  `title`       VARCHAR(255) NOT NULL,
  `url`         VARCHAR(1024) DEFAULT NULL,
  `member`      BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK24016A8D7401D453` (`member`),
  CONSTRAINT `FK24016A8D7401D453` FOREIGN KEY (`member`) REFERENCES `mem_member` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `mem_receiver`;
CREATE TABLE `mem_receiver` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `address`     VARCHAR(200) NOT NULL,
  `area_store`  VARCHAR(300) NOT NULL,
  `is_default`  BIT(1)       NOT NULL,
  `mobile`      VARCHAR(200) NOT NULL,
  `name`        VARCHAR(20)  NOT NULL,
  `zip_code`    VARCHAR(200) NOT NULL,
  `member_id`   BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `park_brand`;
CREATE TABLE `park_brand` (
  `id`               BIGINT(20)   NOT NULL,
  `create_time`      DATETIME      DEFAULT NULL,
  `creator`          VARCHAR(255)  DEFAULT NULL,
  `modifier`         VARCHAR(255)  DEFAULT NULL,
  `modify_time`      DATETIME      DEFAULT NULL,
  `eng_name`         VARCHAR(100)  DEFAULT NULL,
  `introduction`     VARCHAR(3000) DEFAULT NULL,
  `logo_path`        VARCHAR(150)  DEFAULT NULL,
  `name`             VARCHAR(255) NOT NULL,
  `url`              VARCHAR(150)  DEFAULT NULL,
  `logo_image_store` VARCHAR(500)  DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `photo_album`;
CREATE TABLE `photo_album` (
  `id`             BIGINT(20)  NOT NULL,
  `create_time`    DATETIME     DEFAULT NULL,
  `creator`        VARCHAR(20)  DEFAULT NULL,
  `modifier`       VARCHAR(20)  DEFAULT NULL,
  `modify_time`    DATETIME     DEFAULT NULL,
  `description`    VARCHAR(350) DEFAULT NULL,
  `name`           VARCHAR(50) NOT NULL,
  `poi_name`       VARCHAR(100) DEFAULT NULL,
  `time`           DATETIME     DEFAULT NULL,
  `photo_album_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PHOTO_ALBUM_PHOTO` (`photo_album_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME`    VARCHAR(100) NOT NULL,
  `TRIGGER_NAME`  VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(100) NOT NULL,
  `BLOB_DATA`     BLOB,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME`    VARCHAR(100) NOT NULL,
  `CALENDAR_NAME` VARCHAR(100) NOT NULL,
  `CALENDAR`      BLOB         NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME`      VARCHAR(100) NOT NULL,
  `TRIGGER_NAME`    VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(100) NOT NULL,
  `CRON_EXPRESSION` VARCHAR(100) NOT NULL,
  `TIME_ZONE_ID`    VARCHAR(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME`        VARCHAR(100) NOT NULL,
  `ENTRY_ID`          VARCHAR(95)  NOT NULL,
  `TRIGGER_NAME`      VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP`     VARCHAR(100) NOT NULL,
  `INSTANCE_NAME`     VARCHAR(100) NOT NULL,
  `FIRED_TIME`        BIGINT(13)   NOT NULL,
  `SCHED_TIME`        BIGINT(13)   NOT NULL,
  `PRIORITY`          INT(11)      NOT NULL,
  `STATE`             VARCHAR(16)  NOT NULL,
  `JOB_NAME`          VARCHAR(100) DEFAULT NULL,
  `JOB_GROUP`         VARCHAR(100) DEFAULT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1)   DEFAULT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1)   DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME`        VARCHAR(100) NOT NULL,
  `JOB_NAME`          VARCHAR(100) NOT NULL,
  `JOB_GROUP`         VARCHAR(100) NOT NULL,
  `DESCRIPTION`       VARCHAR(250) DEFAULT NULL,
  `JOB_CLASS_NAME`    VARCHAR(250) NOT NULL,
  `IS_DURABLE`        VARCHAR(1)   NOT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1)   NOT NULL,
  `IS_UPDATE_DATA`    VARCHAR(1)   NOT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1)   NOT NULL,
  `JOB_DATA`          BLOB,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` VARCHAR(100) NOT NULL,
  `LOCK_NAME`  VARCHAR(40)  NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME`    VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME`        VARCHAR(100) NOT NULL,
  `INSTANCE_NAME`     VARCHAR(100) NOT NULL,
  `LAST_CHECKIN_TIME` BIGINT(13)   NOT NULL,
  `CHECKIN_INTERVAL`  BIGINT(13)   NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME`      VARCHAR(100) NOT NULL,
  `TRIGGER_NAME`    VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(100) NOT NULL,
  `REPEAT_COUNT`    BIGINT(7)    NOT NULL,
  `REPEAT_INTERVAL` BIGINT(12)   NOT NULL,
  `TIMES_TRIGGERED` BIGINT(10)   NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME`    VARCHAR(100) NOT NULL,
  `TRIGGER_NAME`  VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(100) NOT NULL,
  `STR_PROP_1`    VARCHAR(100)   DEFAULT NULL,
  `STR_PROP_2`    VARCHAR(100)   DEFAULT NULL,
  `STR_PROP_3`    VARCHAR(100)   DEFAULT NULL,
  `INT_PROP_1`    INT(11)        DEFAULT NULL,
  `INT_PROP_2`    INT(11)        DEFAULT NULL,
  `LONG_PROP_1`   BIGINT(20)     DEFAULT NULL,
  `LONG_PROP_2`   BIGINT(20)     DEFAULT NULL,
  `DEC_PROP_1`    DECIMAL(13, 4) DEFAULT NULL,
  `DEC_PROP_2`    DECIMAL(13, 4) DEFAULT NULL,
  `BOOL_PROP_1`   VARCHAR(1)     DEFAULT NULL,
  `BOOL_PROP_2`   VARCHAR(1)     DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME`     VARCHAR(100) NOT NULL,
  `TRIGGER_NAME`   VARCHAR(100) NOT NULL,
  `TRIGGER_GROUP`  VARCHAR(100) NOT NULL,
  `JOB_NAME`       VARCHAR(100) NOT NULL,
  `JOB_GROUP`      VARCHAR(100) NOT NULL,
  `DESCRIPTION`    VARCHAR(100) DEFAULT NULL,
  `NEXT_FIRE_TIME` BIGINT(13)   DEFAULT NULL,
  `PREV_FIRE_TIME` BIGINT(13)   DEFAULT NULL,
  `PRIORITY`       INT(11)      DEFAULT NULL,
  `TRIGGER_STATE`  VARCHAR(16)  NOT NULL,
  `TRIGGER_TYPE`   VARCHAR(8)   NOT NULL,
  `START_TIME`     BIGINT(13)   NOT NULL,
  `END_TIME`       BIGINT(13)   DEFAULT NULL,
  `CALENDAR_NAME`  VARCHAR(100) DEFAULT NULL,
  `MISFIRE_INSTR`  SMALLINT(2)  DEFAULT NULL,
  `JOB_DATA`       BLOB,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `remind_model`;
CREATE TABLE `remind_model` (
  `code`              VARCHAR(255) NOT NULL,
  `create_time`       DATETIME      DEFAULT NULL,
  `creator`           VARCHAR(20)   DEFAULT NULL,
  `modifier`          VARCHAR(20)   DEFAULT NULL,
  `modify_time`       DATETIME      DEFAULT NULL,
  `content`           VARCHAR(500)  DEFAULT NULL,
  `model_image_store` VARCHAR(3000) DEFAULT NULL,
  `name`              VARCHAR(200)  DEFAULT NULL,
  `url`               VARCHAR(500)  DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `remind_notice`;
CREATE TABLE `remind_notice` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `content`     VARCHAR(500) DEFAULT NULL,
  `is_read`     BIT(1)       DEFAULT NULL,
  `url`         VARCHAR(255) DEFAULT NULL,
  `code`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_NOTICE_MODEL_CODE` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `shop_config`;
CREATE TABLE `shop_config` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME         DEFAULT NULL,
  `creator`     VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modifier`    VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `modify_time` DATETIME         DEFAULT NULL,
  `check_code`  VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `description` VARCHAR(500)
                COLLATE utf8_bin DEFAULT NULL,
  `merchan_id`  VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `name`        VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `url`         VARCHAR(255)
                COLLATE utf8_bin DEFAULT NULL,
  `mall`        VARCHAR(20)
                COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

DROP TABLE IF EXISTS `sms_captcha`;
CREATE TABLE `sms_captcha` (
  `id`          VARCHAR(32) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `retry`       INT(11)      DEFAULT NULL,
  `session_id`  VARCHAR(120) DEFAULT NULL,
  `value`       VARCHAR(120) DEFAULT NULL,
  `config_id`   VARCHAR(32)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CONFIG_CAPTCHA` (`config_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sms_captcha_config`;
CREATE TABLE `sms_captcha_config` (
  `id`            VARCHAR(32) NOT NULL,
  `create_time`   DATETIME    DEFAULT NULL,
  `creator`       VARCHAR(20) DEFAULT NULL,
  `modifier`      VARCHAR(20) DEFAULT NULL,
  `modify_time`   DATETIME    DEFAULT NULL,
  `active`        INT(11)     DEFAULT NULL,
  `expires`       INT(11)     DEFAULT NULL,
  `random_word`   VARCHAR(50) DEFAULT NULL,
  `retry`         INT(11)     DEFAULT NULL,
  `server_name`   VARCHAR(50) DEFAULT NULL,
  `template`      VARCHAR(50) DEFAULT NULL,
  `word_length`   INT(11)     DEFAULT NULL,
  `template_path` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sms_log_mobile`;
CREATE TABLE `sms_log_mobile` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `content`     VARCHAR(200) DEFAULT NULL,
  `description` VARCHAR(200) DEFAULT NULL,
  `mobilephone` VARCHAR(20)  DEFAULT NULL,
  `status`      BIT(1)       DEFAULT NULL,
  `config_id`   VARCHAR(32)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CONFIG_LOGMOBILE` (`config_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `snapshot`;
CREATE TABLE `snapshot` (
  `id`          BIGINT(20)  NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `content`     LONGTEXT    NOT NULL,
  `target_id`   BIGINT(20)  NOT NULL,
  `target_type` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_data`;
CREATE TABLE `swp_data` (
  `id`               BIGINT(20)   NOT NULL,
  `create_time`      DATETIME      DEFAULT NULL,
  `creator`          VARCHAR(20)   DEFAULT NULL,
  `modifier`         VARCHAR(20)   DEFAULT NULL,
  `modify_time`      DATETIME      DEFAULT NULL,
  `cache_interval`   BIGINT(20)    DEFAULT NULL,
  `scope`            VARCHAR(255)  DEFAULT NULL,
  `type`             INT(11)       DEFAULT NULL,
  `datainferface_id` BIGINT(20)    DEFAULT NULL,
  `data_analyzer`    TINYBLOB,
  `value`            VARCHAR(2000) DEFAULT NULL,
  `description`      VARCHAR(255)  DEFAULT NULL,
  `data_source`      VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5B5217DFF92C093` (`datainferface_id`),
  CONSTRAINT `FK5B5217DFF92C093` FOREIGN KEY (`datainferface_id`) REFERENCES `swp_data_inferface` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_data_analyzer`;
CREATE TABLE `swp_data_analyzer` (
  `id`              BIGINT(20) NOT NULL,
  `create_time`     DATETIME      DEFAULT NULL,
  `creator`         VARCHAR(20)   DEFAULT NULL,
  `modifier`        VARCHAR(20)   DEFAULT NULL,
  `modify_time`     DATETIME      DEFAULT NULL,
  `class_name`      VARCHAR(1000) DEFAULT NULL,
  `name`            VARCHAR(50)   DEFAULT NULL,
  `parameter_store` VARCHAR(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_data_inferface`;
CREATE TABLE `swp_data_inferface` (
  `id`            BIGINT(20)   NOT NULL,
  `data_type`     VARCHAR(255) DEFAULT NULL,
  `default_value` VARCHAR(255) DEFAULT NULL,
  `code`          VARCHAR(255) DEFAULT NULL,
  `name`          VARCHAR(255) DEFAULT NULL,
  `template_id`   BIGINT(20)   DEFAULT NULL,
  `java_type`     VARCHAR(255) DEFAULT NULL,
  `is_list`       BIT(1)       DEFAULT NULL,
  `data_source`   VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_DATA_INFERFACE_TEMPLATE` (`template_id`),
  CONSTRAINT `FK_DATA_INFERFACE_TEMPLATE` FOREIGN KEY (`template_id`) REFERENCES `swp_template` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_page`;
CREATE TABLE `swp_page` (
  `id`               BIGINT(20) NOT NULL,
  `create_time`      DATETIME     DEFAULT NULL,
  `creator`          VARCHAR(20)  DEFAULT NULL,
  `modifier`         VARCHAR(20)  DEFAULT NULL,
  `modify_time`      DATETIME     DEFAULT NULL,
  `name`             VARCHAR(255) DEFAULT NULL,
  `type`             VARCHAR(255) DEFAULT NULL,
  `template_id`      BIGINT(20)   DEFAULT NULL,
  `page_analyzer_id` BIGINT(20)   DEFAULT NULL,
  `website_id`       BIGINT(20)   DEFAULT NULL,
  `page_size`        INT(11)      DEFAULT '15',
  `path`             VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_PAGE` (`path`, `website_id`),
  KEY `FK5BA9462F3846941` (`template_id`),
  KEY `FK_142p7n8pgmhfr56x2pcifsf3o` (`website_id`),
  CONSTRAINT `FK5BA9462F3846941` FOREIGN KEY (`template_id`) REFERENCES `swp_template` (`id`),
  CONSTRAINT `FK_142p7n8pgmhfr56x2pcifsf3o` FOREIGN KEY (`website_id`) REFERENCES `sys_website` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_page_analyzer`;
CREATE TABLE `swp_page_analyzer` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `class_name`  VARCHAR(1000) DEFAULT NULL,
  `name`        VARCHAR(50)   DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_page_data`;
CREATE TABLE `swp_page_data` (
  `page_id` BIGINT(20) NOT NULL,
  `data_id` BIGINT(20) NOT NULL,
  KEY `FK_DATA_PAGE` (`data_id`),
  KEY `FK_PAGE_DATA` (`page_id`),
  CONSTRAINT `FK_DATA_PAGE` FOREIGN KEY (`data_id`) REFERENCES `swp_data` (`id`),
  CONSTRAINT `FK_PAGE_DATA` FOREIGN KEY (`page_id`) REFERENCES `swp_page` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_page_item`;
CREATE TABLE `swp_page_item` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `page_id`     BIGINT(20)   DEFAULT NULL,
  `content`     LONGTEXT,
  `file`        VARCHAR(255) DEFAULT NULL,
  `code`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SWP_PAGE_ITEM_PAGE` (`page_id`),
  CONSTRAINT `FK_SWP_PAGE_ITEM_PAGE` FOREIGN KEY (`page_id`) REFERENCES `swp_page` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_page_item_data`;
CREATE TABLE `swp_page_item_data` (
  `id`           BIGINT(20) NOT NULL,
  `create_time`  DATETIME      DEFAULT NULL,
  `creator`      VARCHAR(20)   DEFAULT NULL,
  `modifier`     VARCHAR(20)   DEFAULT NULL,
  `modify_time`  DATETIME      DEFAULT NULL,
  `bean_id`      VARCHAR(255)  DEFAULT NULL,
  `class_name`   VARCHAR(1000) DEFAULT NULL,
  `page_item_id` BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PAGE_ITEM_DATA` (`page_item_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_template`;
CREATE TABLE `swp_template` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `filepath`    VARCHAR(255) DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `type`        VARCHAR(255) DEFAULT NULL,
  `content`     LONGTEXT,
  `path`        VARCHAR(255) DEFAULT NULL,
  `website_id`  BIGINT(20)   DEFAULT NULL,
  `data_key`    VARCHAR(255) DEFAULT NULL,
  `page_type`   VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_TEMPLATE` (`path`, `website_id`),
  KEY `FK_oftx1u311050vemjogrlaxnkn` (`website_id`),
  CONSTRAINT `FK_oftx1u311050vemjogrlaxnkn` FOREIGN KEY (`website_id`) REFERENCES `sys_website` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_trigger`;
CREATE TABLE `swp_trigger` (
  `id`                BIGINT(20) NOT NULL,
  `create_time`       DATETIME     DEFAULT NULL,
  `creator`           VARCHAR(20)  DEFAULT NULL,
  `modifier`          VARCHAR(20)  DEFAULT NULL,
  `modify_time`       DATETIME     DEFAULT NULL,
  `description`       VARCHAR(255) DEFAULT NULL,
  `name`              VARCHAR(255) DEFAULT NULL,
  `value`             VARCHAR(255) DEFAULT NULL,
  `prioritycondition` VARCHAR(255) DEFAULT NULL,
  `page_id`           BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PAGE_TRIGGER` (`page_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `swp_url`;
CREATE TABLE `swp_url` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `code`        VARCHAR(50) NOT NULL,
  `type`        VARCHAR(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `name`        VARCHAR(200)  DEFAULT NULL,
  `sort`        INT(11)       DEFAULT NULL,
  `pcode`       VARCHAR(50)   DEFAULT NULL,
  `ptype`       VARCHAR(20)   DEFAULT NULL,
  PRIMARY KEY (`code`, `type`),
  KEY `FKCC2CF0F4810ADA0A` (`type`),
  KEY `FK_SYS_CONFIG_PARENT` (`pcode`, `ptype`),
  CONSTRAINT `FKCC2CF0F4810ADA0A` FOREIGN KEY (`type`) REFERENCES `sys_config_type` (`code`),
  CONSTRAINT `FK_SYS_CONFIG_PARENT` FOREIGN KEY (`pcode`, `ptype`) REFERENCES `sys_config` (`code`, `type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_config_type`;
CREATE TABLE `sys_config_type` (
  `code`        VARCHAR(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `name`        VARCHAR(200)  DEFAULT NULL,
  PRIMARY KEY (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_dd`;
CREATE TABLE `sys_dd` (
  `code`        VARCHAR(50) NOT NULL,
  `type`        VARCHAR(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `name`        VARCHAR(200)  DEFAULT NULL,
  `sort`        INT(11)       DEFAULT NULL,
  `pcode`       VARCHAR(50)   DEFAULT NULL,
  `ptype`       VARCHAR(20)   DEFAULT NULL,
  PRIMARY KEY (`code`, `type`),
  KEY `FKCB1C2332E70F93A8` (`type`),
  KEY `FK_SYS_DD_PARENT` (`pcode`, `ptype`),
  CONSTRAINT `FKCB1C2332E70F93A8` FOREIGN KEY (`type`) REFERENCES `sys_dd_type` (`code`),
  CONSTRAINT `FK_SYS_DD_PARENT` FOREIGN KEY (`pcode`, `ptype`) REFERENCES `sys_dd` (`code`, `type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_dd_type`;
CREATE TABLE `sys_dd_type` (
  `code`        VARCHAR(20) NOT NULL,
  `create_time` DATETIME      DEFAULT NULL,
  `creator`     VARCHAR(20)   DEFAULT NULL,
  `modifier`    VARCHAR(20)   DEFAULT NULL,
  `modify_time` DATETIME      DEFAULT NULL,
  `description` VARCHAR(2000) DEFAULT NULL,
  `name`        VARCHAR(200)  DEFAULT NULL,
  `sort`        INT(11)       DEFAULT NULL,
  `pcode`       VARCHAR(20)   DEFAULT NULL,
  `layer`       INT(11)       DEFAULT NULL,
  `path`        VARCHAR(200)  DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK_SYS_DD_TYPE_PID` (`pcode`),
  CONSTRAINT `FK_SYS_DD_TYPE_PID` FOREIGN KEY (`pcode`) REFERENCES `sys_dd_type` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence` (
  `gen_name`  VARCHAR(255) NOT NULL,
  `gen_value` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`gen_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `description` VARCHAR(200) DEFAULT NULL,
  `code`        VARCHAR(10)  DEFAULT NULL,
  `name`        VARCHAR(50)  DEFAULT NULL,
  `value`       VARCHAR(200) DEFAULT NULL,
  `website_key` VARCHAR(20)  DEFAULT NULL,
  `website_id`  BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `website_id` (`website_key`, `code`),
  UNIQUE KEY `UK_mrm9g3kn2fkaplrpq4r419cks` (`website_key`, `code`),
  UNIQUE KEY `UK_p92614fh48xxfurwu6a55aem1` (`website_key`, `code`),
  KEY `FKF71F56DEB6E2DF26` (`website_key`),
  KEY `FK_fv5jkx7r6x92kusfvtvxdwfit` (`website_id`),
  CONSTRAINT `FK_fv5jkx7r6x92kusfvtvxdwfit` FOREIGN KEY (`website_id`) REFERENCES `sys_website` (`id`),
  CONSTRAINT `FK_dc9eqpws7k125mgfat66tvlda` FOREIGN KEY (`website_key`) REFERENCES `sys_website` (`code`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_web_access_log`;
CREATE TABLE `sys_web_access_log` (
  `id`              BIGINT(20) NOT NULL,
  `create_time`     DATETIME      DEFAULT NULL,
  `creator`         VARCHAR(20)   DEFAULT NULL,
  `modifier`        VARCHAR(20)   DEFAULT NULL,
  `modify_time`     DATETIME      DEFAULT NULL,
  `browser`         VARCHAR(20)   DEFAULT NULL,
  `browser_version` VARCHAR(20)   DEFAULT NULL,
  `os_version`      VARCHAR(20)   DEFAULT NULL,
  `parameter`       VARCHAR(1000) DEFAULT NULL,
  `referer`         VARCHAR(100)  DEFAULT NULL,
  `session_id`      VARCHAR(50)   DEFAULT NULL,
  `url`             VARCHAR(200)  DEFAULT NULL,
  `user_ip`         VARCHAR(20)   DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `sys_website`;
CREATE TABLE `sys_website` (
  `id`                         BIGINT(20)  NOT NULL,
  `create_time`                DATETIME     DEFAULT NULL,
  `creator`                    VARCHAR(20)  DEFAULT NULL,
  `modifier`                   VARCHAR(20)  DEFAULT NULL,
  `modify_time`                DATETIME     DEFAULT NULL,
  `code`                       VARCHAR(20)  DEFAULT NULL,
  `name`                       VARCHAR(50)  DEFAULT NULL,
  `web`                        VARCHAR(100) DEFAULT NULL,
  `default_filemanager`        VARCHAR(50) NOT NULL,
  `default_upload_filemanager` VARCHAR(50) NOT NULL,
  `menu_id`                    BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `FK_WEBSITE_DEF_UPLOAD_FMID` (`default_upload_filemanager`),
  KEY `FK_WEBSITE_DEF_FMID` (`default_filemanager`),
  KEY `FK_WEBSITE_MENU_PID` (`menu_id`),
  CONSTRAINT `FK_WEBSITE_DEF_FMID` FOREIGN KEY (`default_filemanager`) REFERENCES `file_manager_config` (`id`),
  CONSTRAINT `FK_WEBSITE_DEF_UPLOAD_FMID` FOREIGN KEY (`default_upload_filemanager`) REFERENCES `file_manager_config` (`id`),
  CONSTRAINT `FK_WEBSITE_MENU_PID` FOREIGN KEY (`menu_id`) REFERENCES `auth_menu` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `test_article`;
CREATE TABLE `test_article` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `summary`     VARCHAR(255) DEFAULT NULL,
  `title`       VARCHAR(255) DEFAULT NULL,
  `version_id`  BIGINT(20)   DEFAULT NULL,
  `issue`       BIT(1)       DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_493w2rkvdvlsqxlkyrk76d45i` (`id`, `version_id`),
  KEY `FK_CMS_ARTICLE_VERSION` (`version_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `test_bean`;
CREATE TABLE `test_bean` (
  `t_key`   VARCHAR(255) NOT NULL,
  `t_value` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`t_key`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `test_cache_article`;
CREATE TABLE `test_cache_article` (
  `id`          VARCHAR(255) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `issue`       BIT(1)       DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `summary`     VARCHAR(255) DEFAULT NULL,
  `title`       VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `testbean`;
CREATE TABLE `testbean` (
  `t_key`   VARCHAR(255) NOT NULL,
  `t_value` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`t_key`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `video_transcoding_log`;
CREATE TABLE `video_transcoding_log` (
  `id`                 BIGINT(20) NOT NULL,
  `create_time`        DATETIME     DEFAULT NULL,
  `creator`            VARCHAR(20)  DEFAULT NULL,
  `modifier`           VARCHAR(20)  DEFAULT NULL,
  `modify_time`        DATETIME     DEFAULT NULL,
  `new_file_path`      VARCHAR(255) DEFAULT NULL,
  `new_format`         VARCHAR(255) DEFAULT NULL,
  `original_file_path` VARCHAR(255) DEFAULT NULL,
  `original_format`    VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_access_token`;
CREATE TABLE `wx_access_token` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `app_id`      VARCHAR(200) DEFAULT NULL,
  `app_secret`  VARCHAR(200) DEFAULT NULL,
  `expires_in`  INT(11)      DEFAULT NULL,
  `token`       VARCHAR(500) DEFAULT NULL,
  `token_name`  VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_account`;
CREATE TABLE `wx_account` (
  `app_id`       VARCHAR(200) NOT NULL,
  `create_time`  DATETIME     DEFAULT NULL,
  `creator`      VARCHAR(20)  DEFAULT NULL,
  `modifier`     VARCHAR(20)  DEFAULT NULL,
  `modify_time`  DATETIME     DEFAULT NULL,
  `aes_key`      VARCHAR(200) DEFAULT NULL,
  `name`         VARCHAR(100) DEFAULT NULL,
  `app_secret`   VARCHAR(200) DEFAULT NULL,
  `token_name`   VARCHAR(200) DEFAULT NULL,
  `type`         VARCHAR(20)  DEFAULT NULL,
  `primitive_id` VARCHAR(200) DEFAULT NULL,
  `agent_id`     VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_account_mapping`;
CREATE TABLE `wx_account_mapping` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `user_type`   VARCHAR(200) DEFAULT NULL,
  `username`    VARCHAR(200) DEFAULT NULL,
  `account_id`  VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ek6fj6w0kqxvttamx3h5ivs7i` (`username`, `user_type`),
  KEY `FK_482mgbrqfcf9xwb1vuscrqq2y` (`account_id`),
  CONSTRAINT `FK_482mgbrqfcf9xwb1vuscrqq2y` FOREIGN KEY (`account_id`) REFERENCES `wx_account` (`app_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_config`;
CREATE TABLE `wx_config` (
  `app_id`      VARCHAR(200) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `app_secret`  VARCHAR(200) DEFAULT NULL,
  `expires_in`  INT(11)      DEFAULT NULL,
  `token`       VARCHAR(500) DEFAULT NULL,
  `token_name`  VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_group`;
CREATE TABLE `wx_group` (
  `id`          BIGINT(20) NOT NULL,
  `count`       BIGINT(20)   DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_group_message`;
CREATE TABLE `wx_group_message` (
  `id`            BIGINT(20) NOT NULL,
  `content`       VARCHAR(1000) DEFAULT NULL,
  `group_id`      BIGINT(20)    DEFAULT NULL,
  `media_id`      VARCHAR(100)  DEFAULT NULL,
  `modify_time`   DATETIME      DEFAULT NULL,
  `msg_type`      VARCHAR(50)   DEFAULT NULL,
  `to_users_data` VARCHAR(5000) DEFAULT NULL,
  `create_time`   DATETIME      DEFAULT NULL,
  `creator`       VARCHAR(20)   DEFAULT NULL,
  `modifier`      VARCHAR(20)   DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_group_news`;
CREATE TABLE `wx_group_news` (
  `id`          BIGINT(20) NOT NULL,
  `created_at`  BIGINT(20)   DEFAULT NULL,
  `media_id`    VARCHAR(255) DEFAULT NULL,
  `type`        VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_group_news_article`;
CREATE TABLE `wx_group_news_article` (
  `id`                 BIGINT(20) NOT NULL,
  `author`             VARCHAR(255) DEFAULT NULL,
  `content`            VARCHAR(255) DEFAULT NULL,
  `content_source_url` VARCHAR(255) DEFAULT NULL,
  `digest`             VARCHAR(255) DEFAULT NULL,
  `show_cover_pic`     BIT(1)       DEFAULT NULL,
  `thumb_media_id`     VARCHAR(255) DEFAULT NULL,
  `title`              VARCHAR(255) DEFAULT NULL,
  `news_id`            BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_WX_GROUP_NEWS_ARTICLE` (`news_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_media`;
CREATE TABLE `wx_media` (
  `id`             BIGINT(20) NOT NULL,
  `created_at`     BIGINT(20)   DEFAULT NULL,
  `media_id`       VARCHAR(255) DEFAULT NULL,
  `thumb_media_id` VARCHAR(255) DEFAULT NULL,
  `type`           VARCHAR(255) DEFAULT NULL,
  `create_time`    DATETIME     DEFAULT NULL,
  `creator`        VARCHAR(20)  DEFAULT NULL,
  `modifier`       VARCHAR(20)  DEFAULT NULL,
  `modify_time`    DATETIME     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_menu`;
CREATE TABLE `wx_menu` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME     DEFAULT NULL,
  `creator`     VARCHAR(20)  DEFAULT NULL,
  `modifier`    VARCHAR(20)  DEFAULT NULL,
  `modify_time` DATETIME     DEFAULT NULL,
  `key`         VARCHAR(255) DEFAULT NULL,
  `layer`       INT(11)      DEFAULT NULL,
  `name`        VARCHAR(255) DEFAULT NULL,
  `sort`        INT(11)      DEFAULT NULL,
  `type`        VARCHAR(255) DEFAULT NULL,
  `url`         VARCHAR(255) DEFAULT NULL,
  `p_id`        BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_MENU_PARENT` (`p_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_message`;
CREATE TABLE `wx_message` (
  `id`              BIGINT(20) NOT NULL,
  `content`         VARCHAR(5000) DEFAULT NULL,
  `create_time`     BIGINT(20)    DEFAULT NULL,
  `description`     VARCHAR(2000) DEFAULT NULL,
  `event`           VARCHAR(255)  DEFAULT NULL,
  `event_key`       VARCHAR(255)  DEFAULT NULL,
  `format`          VARCHAR(255)  DEFAULT NULL,
  `from_user_name`  VARCHAR(255)  DEFAULT NULL,
  `label`           VARCHAR(255)  DEFAULT NULL,
  `location_x`      VARCHAR(255)  DEFAULT NULL,
  `location_y`      VARCHAR(255)  DEFAULT NULL,
  `mediaid`         VARCHAR(255)  DEFAULT NULL,
  `msg_id`          BIGINT(20)    DEFAULT NULL,
  `msg_type`        VARCHAR(255)  DEFAULT NULL,
  `pic_url`         VARCHAR(255)  DEFAULT NULL,
  `scale`           VARCHAR(255)  DEFAULT NULL,
  `title`           VARCHAR(500)  DEFAULT NULL,
  `to_user_name`    VARCHAR(255)  DEFAULT NULL,
  `type`            VARCHAR(255)  DEFAULT NULL,
  `url`             VARCHAR(500)  DEFAULT NULL,
  `openid`          VARCHAR(255)  DEFAULT NULL,
  `error_count`     INT(11)       DEFAULT NULL,
  `filter_count`    INT(11)       DEFAULT NULL,
  `latitude`        DOUBLE        DEFAULT NULL,
  `longitude`       DOUBLE        DEFAULT NULL,
  `recognition`     VARCHAR(255)  DEFAULT NULL,
  `sent_count`      INT(11)       DEFAULT NULL,
  `status`          VARCHAR(255)  DEFAULT NULL,
  `thumb_mediaid`   VARCHAR(255)  DEFAULT NULL,
  `ticket`          VARCHAR(255)  DEFAULT NULL,
  `total_count`     INT(11)       DEFAULT NULL,
  `media_data`      VARCHAR(5000) DEFAULT NULL,
  `precision_value` DOUBLE        DEFAULT NULL,
  `appid`           VARCHAR(255)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n5m3rox6krb38mhta0absdlm1` (`openid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_message_event`;
CREATE TABLE `wx_message_event` (
  `id`             BIGINT(20) NOT NULL,
  `create_time`    BIGINT(20)   DEFAULT NULL,
  `from_user_name` VARCHAR(255) DEFAULT NULL,
  `msg_id`         BIGINT(20)   DEFAULT NULL,
  `msg_type`       VARCHAR(255) DEFAULT NULL,
  `to_user_name`   VARCHAR(200) DEFAULT NULL,
  `evnet`          VARCHAR(255) DEFAULT NULL,
  `evnet_key`      VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_message_link`;
CREATE TABLE `wx_message_link` (
  `id`             BIGINT(20) NOT NULL,
  `create_time`    BIGINT(20)    DEFAULT NULL,
  `from_user_name` VARCHAR(255)  DEFAULT NULL,
  `msg_id`         BIGINT(20)    DEFAULT NULL,
  `msg_type`       VARCHAR(255)  DEFAULT NULL,
  `to_user_name`   VARCHAR(200)  DEFAULT NULL,
  `description`    VARCHAR(2000) DEFAULT NULL,
  `title`          VARCHAR(500)  DEFAULT NULL,
  `url`            VARCHAR(500)  DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_message_text`;
CREATE TABLE `wx_message_text` (
  `id`             BIGINT(20) NOT NULL,
  `create_time`    BIGINT(20)    DEFAULT NULL,
  `from_user_name` VARCHAR(255)  DEFAULT NULL,
  `msg_id`         BIGINT(20)    DEFAULT NULL,
  `msg_type`       VARCHAR(255)  DEFAULT NULL,
  `to_user_name`   VARCHAR(200)  DEFAULT NULL,
  `content`        VARCHAR(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_qrcode`;
CREATE TABLE `wx_qrcode` (
  `id`             BIGINT(20) NOT NULL,
  `create_time`    DATETIME     DEFAULT NULL,
  `expire_seconds` INT(11)      DEFAULT NULL,
  `img_path`       VARCHAR(800) DEFAULT NULL,
  `link_key`       VARCHAR(255) DEFAULT NULL,
  `url`            VARCHAR(255) DEFAULT NULL,
  `creator`        VARCHAR(20)  DEFAULT NULL,
  `modifier`       VARCHAR(20)  DEFAULT NULL,
  `modify_time`    DATETIME     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `wx_user_info`;
CREATE TABLE `wx_user_info` (
  `openid`            VARCHAR(255) NOT NULL,
  `city`              VARCHAR(255)          DEFAULT NULL,
  `country`           VARCHAR(255)          DEFAULT NULL,
  `headimgurl`        VARCHAR(600)          DEFAULT NULL,
  `language`          VARCHAR(255)          DEFAULT NULL,
  `nickname`          VARCHAR(200)
                      CHARACTER SET utf8mb4 DEFAULT NULL,
  `province`          VARCHAR(255)          DEFAULT NULL,
  `remark`            VARCHAR(255)          DEFAULT NULL,
  `sex`               INT(11)               DEFAULT NULL,
  `state`             VARCHAR(255)          DEFAULT NULL,
  `subscribe`         INT(11)               DEFAULT NULL,
  `subscribe_time`    BIGINT(20)            DEFAULT NULL,
  `last_message_time` BIGINT(20)            DEFAULT NULL,
  `last_look_time`    BIGINT(20)            DEFAULT NULL,
  `un_read_size`      INT(11)               DEFAULT NULL,
  `union_id`          VARCHAR(255)          DEFAULT NULL,
  `id`                BIGINT(20)            DEFAULT NULL,
  `group_id`          BIGINT(20)            DEFAULT NULL,
  `avatar`            VARCHAR(600)          DEFAULT NULL,
  `member_id`         BIGINT(20)            DEFAULT NULL,
  `create_time`       DATETIME              DEFAULT NULL,
  `creator`           VARCHAR(20)           DEFAULT NULL,
  `modifier`          VARCHAR(20)           DEFAULT NULL,
  `modify_time`       DATETIME              DEFAULT NULL,
  `appid`             VARCHAR(255) NOT NULL,
  PRIMARY KEY (`openid`),
  KEY `FK_j98xtr3bub56dvw641hinfep7` (`id`),
  KEY `FK_2ohwd3ky96i3439t1o2kapekx` (`group_id`),
  KEY `FK_5ytiwg2pew0ybepuen5bap3f4` (`member_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `yr_answer`;
CREATE TABLE `yr_answer` (
  `id`          BIGINT(20)   NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `content`     LONGTEXT     NOT NULL,
  `level`       VARCHAR(255) NOT NULL,
  `praise`      INT(11)     DEFAULT NULL,
  `unpraise`    INT(11)     DEFAULT NULL,
  `member_id`   BIGINT(20)  DEFAULT NULL,
  `question_id` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ANSWER_MEMBER` (`member_id`),
  KEY `FK_ANSWER_QUESTION` (`question_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `yr_answeradditional`;
CREATE TABLE `yr_answeradditional` (
  `id`          BIGINT(20) NOT NULL,
  `create_time` DATETIME    DEFAULT NULL,
  `creator`     VARCHAR(20) DEFAULT NULL,
  `modifier`    VARCHAR(20) DEFAULT NULL,
  `modify_time` DATETIME    DEFAULT NULL,
  `content`     LONGTEXT   NOT NULL,
  `answer_id`   BIGINT(20) NOT NULL,
  `member_id`   BIGINT(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_AA_ANSWER` (`answer_id`),
  KEY `FK_ANSWERADDITIONAL_MEMBER` (`member_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `yr_category`;
CREATE TABLE `yr_category` (
  `id`               BIGINT(20)   NOT NULL,
  `create_time`      DATETIME      DEFAULT NULL,
  `creator`          VARCHAR(20)   DEFAULT NULL,
  `modifier`         VARCHAR(20)   DEFAULT NULL,
  `modify_time`      DATETIME      DEFAULT NULL,
  `layer`            INT(11)      NOT NULL,
  `meta_description` VARCHAR(3000) DEFAULT NULL,
  `meta_keywords`    VARCHAR(3000) DEFAULT NULL,
  `name`             VARCHAR(255) NOT NULL,
  `path`             VARCHAR(200) NOT NULL,
  `sign`             VARCHAR(255) NOT NULL,
  `sort`             INT(11)       DEFAULT NULL,
  `p_id`             BIGINT(20)    DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kh6yjb02kw3l3hija36j25m74` (`sign`),
  KEY `FK_CATEGORY_PARENT` (`p_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `yr_question`;
CREATE TABLE `yr_question` (
  `id`                   BIGINT(20)  NOT NULL,
  `create_time`          DATETIME             DEFAULT NULL,
  `creator`              VARCHAR(20)          DEFAULT NULL,
  `modifier`             VARCHAR(20)          DEFAULT NULL,
  `modify_time`          DATETIME             DEFAULT NULL,
  `askquestion`          VARCHAR(255)         DEFAULT NULL,
  `content`              LONGTEXT    NOT NULL,
  `lasttime`             DATETIME             DEFAULT NULL,
  `answer_size`          INT(11)     NOT NULL DEFAULT '0',
  `question_status`      VARCHAR(20) NOT NULL,
  `title`                VARCHAR(50)          DEFAULT NULL,
  `question_category_id` BIGINT(20)  NOT NULL,
  `member_id`            BIGINT(20)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_QUESTION_CATEGORY` (`question_category_id`),
  KEY `FK_QUESTION_MEMBER` (`member_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `zxw_contactus`;
CREATE TABLE `zxw_contactus` (
  `id`              BIGINT(20) NOT NULL,
  `create_time`     DATETIME     DEFAULT NULL,
  `creator`         VARCHAR(20)  DEFAULT NULL,
  `modifier`        VARCHAR(20)  DEFAULT NULL,
  `modify_time`     DATETIME     DEFAULT NULL,
  `address`         VARCHAR(255) DEFAULT NULL,
  `channels`        VARCHAR(255) DEFAULT NULL,
  `demand`          VARCHAR(255) DEFAULT NULL,
  `description`     VARCHAR(255) DEFAULT NULL,
  `housing_type`    VARCHAR(255) DEFAULT NULL,
  `name`            VARCHAR(255) DEFAULT NULL,
  `phone`           VARCHAR(255) DEFAULT NULL,
  `amount_time`     VARCHAR(255) DEFAULT NULL,
  `ate_time`        VARCHAR(255) DEFAULT NULL,
  `decorate_budget` VARCHAR(255) DEFAULT NULL,
  `decorate_type`   VARCHAR(255) DEFAULT NULL,
  `housing_area`    VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 1;