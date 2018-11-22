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

 Date: 22/11/2018 20:19:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for zzq_group
-- ----------------------------
DROP TABLE IF EXISTS `zzq_group`;
CREATE TABLE `zzq_group`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名字',
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父级',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型1:角色2：部门',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `crt_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人id',
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人名字',
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人ip',
  `upd_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人id',
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人名字',
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人ip',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zzq_group
-- ----------------------------
INSERT INTO `zzq_group` VALUES ('1', '管理员', '-1', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group` VALUES ('3', '体验组', '-1', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group` VALUES ('4', '游客', '3', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group` VALUES ('6', 'AG集团', '-1', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group` VALUES ('7', '财务部', '6', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group` VALUES ('8', '人力资源部', '6', NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group` VALUES ('9', '博客管理员', '-1', NULL, '', '2017-07-16 16:59:30', '1', 'Mr.AG', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for zzq_group_member
-- ----------------------------
DROP TABLE IF EXISTS `zzq_group_member`;
CREATE TABLE `zzq_group_member`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户id',
  `isleader` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否是leader：0是1不是',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zzq_group_member
-- ----------------------------
INSERT INTO `zzq_group_member` VALUES ('10', '1', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group_member` VALUES ('2', '4', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_group_member` VALUES ('9', '9', '4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for zzq_group_menu
-- ----------------------------
DROP TABLE IF EXISTS `zzq_group_menu`;
CREATE TABLE `zzq_group_menu`  (
  `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组ID',
  `menu` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zzq_menu
-- ----------------------------
DROP TABLE IF EXISTS `zzq_menu`;
CREATE TABLE `zzq_menu`  (
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
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zzq_menu
-- ----------------------------
INSERT INTO `zzq_menu` VALUES ('1', '用户管理', '5', NULL, '/admin/user', 'fa fa-user', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('10', 'Spring-Boot监控', '9', NULL, 'http://localhost:8764', 'fa fa-line-chart', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, '2017-07-25 19:38:11', '1', 'Mr.AG', '0:0:0:0:0:0:0:1');
INSERT INTO `zzq_menu` VALUES ('11', 'Hystrix监控', '9', NULL, 'http://localhost:8764/hystrix', 'fa fa-bar-chart', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('13', '权限管理系统', '-1', NULL, '', 'fa fa-terminal', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('14', '内容管理系统', '-1', NULL, '', 'fa-newspaper-o', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('17', '文章管理', '20', NULL, '/admin/blog/article', 'fa fa-book', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, '2017-07-15 23:45:24', '1', '管理员', '0:0:0:0:0:0:0:1');
INSERT INTO `zzq_menu` VALUES ('18', '评论管理', '20', NULL, '', '', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('20', '文章评论管理', '14', NULL, '', 'fa fa-bookmark', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('21', '数据字典', '5', NULL, '', 'fa fa-book', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('22', '服务端api文档', '13', NULL, '', 'fa fa-folder', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('23', 'Admin Rest API', '22', NULL, '/back/swagger-ui.html', 'fa fa-file-code-o', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('24', 'Admin Druid数据监控', '9', NULL, '/back/druid/datasource.html', 'fa fa-line-chart', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('27', '操作日志', '5', NULL, '/admin/gateLog', 'fa fa-book', 0, '', NULL, NULL, '2017-07-01 00:00:00', '1', 'admin', '0:0:0:0:0:0:0:1', NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('5', '基础配置管理', '13', NULL, '', 'fa fa-cog fa-spin', 0, '用户', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('6', '菜单管理', '5', NULL, '/admin/menu', 'fa fa-list', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('7', '角色组管理', '5', NULL, '/admin/group', 'fa fa-users', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('8', '角色类型管理', '5', NULL, '/admin/groupType', 'fa fa-address-card-o', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `zzq_menu` VALUES ('9', '系统监控', '13', NULL, '', 'fa fa-area-chart', 0, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for zzq_user
-- ----------------------------
DROP TABLE IF EXISTS `zzq_user`;
CREATE TABLE `zzq_user`  (
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
  `crt_time` datetime(0) NULL DEFAULT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `crt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `crt_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_time` datetime(0) NULL DEFAULT NULL,
  `upd_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `upd_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zzq_user
-- ----------------------------
INSERT INTO `zzq_user` VALUES ('1', 'admin', '$2a$12$S/yLlj9kzi5Dgsz97H4rAekxrPlk/10eXp1lUJcAVAx.2M9tOpWie', 'Mr.AG', '', NULL, '', NULL, '', '男', NULL, '微服务架构师', NULL, NULL, NULL, NULL, '2017-07-25 14:54:21', '1', 'Mr.AG', '0:0:0:0:0:0:0:1');
INSERT INTO `zzq_user` VALUES ('2', 'admin', '$2a$12$zWe6knO6rGp15UVfdWTTxu.Ykt.k3QnD5FPoj6a1cnL63csHY2A1S', '测试账户', '', NULL, '', NULL, '', '男', NULL, '', NULL, NULL, NULL, NULL, '2017-07-15 19:18:07', '1', '管理员', '0:0:0:0:0:0:0:1');
INSERT INTO `zzq_user` VALUES ('4', 'admin', '$2a$12$S/yLlj9kzi5Dgsz97H4rAekxrPlk/10eXp1lUJcAVAx.2M9tOpWie', 'Mr.AG(博主)', '', NULL, '', NULL, '', '男', NULL, '', NULL, NULL, NULL, NULL, '2017-08-28 08:50:04', '1', 'Mr.AG', '127.0.0.1');

SET FOREIGN_KEY_CHECKS = 1;
