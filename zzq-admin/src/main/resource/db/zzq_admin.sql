/*
 Navicat MySQL Data Transfer

 Source Server         : ttt
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : zzq_admin

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 23/11/2018 21:49:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父级节点',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `href` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源路径',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `order_num` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `enabled` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '启用禁用',
  `showed` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否显示',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES ('1', '用户管理', '5', NULL, '/admin/user', 'fa fa-user', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('10', 'Spring-Boot监控', '9', NULL, 'http://localhost:8764', 'fa fa-line-chart', 0, '', NULL, NULL, NULL, '2017-07-25 19:38:11');
INSERT INTO `system_menu` VALUES ('11', 'Hystrix监控', '9', NULL, 'http://localhost:8764/hystrix', 'fa fa-bar-chart', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('13', '权限管理系统', '-1', NULL, '', 'fa fa-terminal', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('14', '内容管理系统', '-1', NULL, '', 'fa-newspaper-o', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('17', '文章管理', '20', NULL, '/admin/blog/article', 'fa fa-book', 0, '', NULL, NULL, NULL, '2017-07-15 23:45:24');
INSERT INTO `system_menu` VALUES ('18', '评论管理', '20', NULL, '', '', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('20', '文章评论管理', '14', NULL, '', 'fa fa-bookmark', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('21', '数据字典', '5', NULL, '', 'fa fa-book', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('22', '服务端api文档', '13', NULL, '', 'fa fa-folder', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('23', 'Admin Rest API', '22', NULL, '/back/swagger-ui.html', 'fa fa-file-code-o', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('24', 'Admin Druid数据监控', '9', NULL, '/back/druid/datasource.html', 'fa fa-line-chart', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('27', '操作日志', '5', NULL, '/admin/gateLog', 'fa fa-book', 0, '', NULL, NULL, '2017-07-01 00:00:00', NULL);
INSERT INTO `system_menu` VALUES ('5', '基础配置管理', '13', NULL, '', 'fa fa-cog fa-spin', 0, '用户', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('6', '菜单管理', '5', NULL, '/admin/menu', 'fa fa-list', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('7', '角色组管理', '5', NULL, '/admin/group', 'fa fa-users', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('8', '角色类型管理', '5', NULL, '/admin/groupType', 'fa fa-address-card-o', 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `system_menu` VALUES ('9', '系统监控', '13', NULL, '', 'fa fa-area-chart', 0, '', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名字',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生日',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '住址',
  `mobile_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机电话',
  `tel_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '座机电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否有效0有效1无效',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES ('1', 'admin', '$2a$12$S/yLlj9kzi5Dgsz97H4rAekxrPlk/10eXp1lUJcAVAx.2M9tOpWie', 'Mr.AG', '', NULL, '', NULL, '', '男', NULL, '微服务架构师', NULL, '2017-07-25 14:54:21');
INSERT INTO `system_user` VALUES ('2', 'admin1', '$2a$12$zWe6knO6rGp15UVfdWTTxu.Ykt.k3QnD5FPoj6a1cnL63csHY2A1S', '测试账户', '', NULL, '', NULL, '', '男', NULL, '', NULL, '2017-07-15 19:18:07');
INSERT INTO `system_user` VALUES ('4', 'admin2', '$2a$12$S/yLlj9kzi5Dgsz97H4rAekxrPlk/10eXp1lUJcAVAx.2M9tOpWie', 'Mr.AG(博主)', '', NULL, '', NULL, '', '男', NULL, '', NULL, '2017-08-28 08:50:04');

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色中间表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
