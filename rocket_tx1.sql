/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : rocket_tx1

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-04-13 19:47:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for de_duplication
-- ----------------------------
DROP TABLE IF EXISTS `de_duplication`;
CREATE TABLE `de_duplication` (
  `tx_no` varchar(255) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of de_duplication
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '订单名称',
  `order_createtime` datetime DEFAULT NULL COMMENT '下单时间',
  `order_state` int(11) DEFAULT NULL COMMENT '订单状态 0 已经未支付 1已经支付 2已退单',
  `order_money` double(10,0) DEFAULT NULL COMMENT '订单价格',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单ID',
  `tx_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------
