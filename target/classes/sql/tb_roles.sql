/*
Navicat MySQL Data Transfer

Source Server         : discuz
Source Server Version : 50717
Source Host           : 103.45.101.109:4406
Source Database       : test2

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-06-05 14:35:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_roles
-- ----------------------------
DROP TABLE IF EXISTS `tb_roles`;
CREATE TABLE `tb_roles` (
  `R_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `R_NAME` varchar(30) DEFAULT NULL,
  `R_CODE` bigint(20) DEFAULT '6' COMMENT '1：客服\n 2：运营\n 3：买家\n 4：卖家\n 5：个人\n 6：平台',
  `R_CREATE_NAME` varchar(32) DEFAULT NULL,
  `R_CREATE_TIME` datetime DEFAULT NULL,
  `R_FLAG` int(11) DEFAULT '0' COMMENT '逻辑删除[0:正常，1:删除]',
  `R_REMARK` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`R_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_roles
-- ----------------------------
INSERT INTO `tb_roles` VALUES ('1', '超级管理员', '6', 'system', '2018-01-10 00:00:00', '0', '备注');
INSERT INTO `tb_roles` VALUES ('33', '普通成员', '0', 'admin', '2018-04-09 00:00:00', '0', null);
