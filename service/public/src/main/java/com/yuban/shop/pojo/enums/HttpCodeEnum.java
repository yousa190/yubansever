package com.yuban.shop.pojo.enums;

/**
 * HTTP状态错误码枚举（遵循《Java开发手册（泰山版）》错误码规范）
 * 错误码规则：5位字符串，格式为「来源前缀+4位编号」
 * A：用户端错误；B：系统执行出错；C：第三方服务出错；00000：成功
 */
public enum HttpCodeEnum {

 // 成功状态
 SUCCESS("00000", "操作成功"),

 // 用户端错误：登录相关（A02xx）
 NEED_LOGIN("A0230", "用户登录已过期，请重新登录"), // 对应「用户登陆已过期」
 LOGIN_ERROR("A0210", "用户名或密码错误"), // 对应「用户密码错误」

 // 用户端错误：权限相关（A03xx）
 NO_OPERATOR_AUTH("A0301", "访问未授权，无操作权限"), // 对应「访问未授权」

 // 用户端错误：注册/用户信息相关（A01xx）
 USERNAME_EXIST("A0111", "用户名已存在"), // 对应「用户名已存在」
 PHONENUMBER_EXIST("A0150", "手机号已存在"), // 归类「用户基本信息校验失败」
 EMAIL_EXIST("A0150", "邮箱已存在"), // 归类「用户基本信息校验失败」
 NICKNAME_EXIST("A0150", "昵称已存在"), // 归类「用户基本信息校验失败」
 EMAIL_ALREADY_USED("A0150", "邮箱已被使用"),
 NAME_DUPLICATE("A0150", "名称重复"),

 // 用户端错误：请求参数相关（A04xx）
 REQUIRE_USERNAME("A0410", "用户名为必填参数"), // 对应「请求必填参数为空」
 USERNAME_NOT_NULL("A0410", "用户名为必填参数，不能为空"), // 对应「请求必填参数为空」
 NICKNAME_NOT_NULL("A0410", "昵称为必填参数，不能为空"), // 对应「请求必填参数为空」
 PASSWORD_NOT_NULL("A0410", "密码为必填参数，不能为空"), // 对应「请求必填参数为空」
 EMAIL_NOT_NULL("A0410", "邮箱为必填参数，不能为空"), // 对应「请求必填参数为空」
 CONTENT_NOT_NULL("A0410", "评论内容为必填参数，不能为空"), // 对应「请求必填参数为空」
 CATID_NOT_NULL("A0410","分类id不能为空" ),
 GROUPID_NOT_NULL("A0410","分类参数id不能为空"  ),
 FILE_TYPE_ERR("A0421", "文件类型错误，参数格式不匹配，请上传png文件"), // 对应「参数格式不匹配」
 CATEGORY_NOT_EXIST("A0422", "分类不存在"),
 SAME_PARENT_CATEGORY_DUPLICATE_NAME("A0431", "同一父分类下名称不能重复,包含重复内容"),
 PARENT_CATEGORY_NOT_EXIST("A0422", "父分类不存在"),
 PARENT_CATEGORY_LEVEL_EXCEED("A0421", "父分类层级已达上限"),

 // 系统执行出错（B00xx）
 SYSTEM_ERROR("B0001", "系统执行出错，请稍后重试"),

 // 第三方服务出错：通知服务（C05xx）
 MAIL_SEND_ERROR("C0503", "邮件提醒服务失败，发送邮件出错"),
 // 数据库服务超时
 DB_TIMEOUT("C0250", "数据库操作失败"),
 // 表不存在
 TABLE_NOT_EXIST("C0311", "数据库表不存在"),
 // 列不存在
 COLUMN_NOT_EXIST("C0312", "数据库列不存在"),
 // 多表关联存在同名列
 DUPLICATE_COLUMN("C0321", "多表关联中存在多个相同名称的列"),
 // 数据库死锁
 DB_DEADLOCK("C0331", "数据库发生死锁，操作被终止"),
 // 主键冲突
 PRIMARY_KEY_CONFLICT("C0341", "主键冲突，数据插入失败"),;

 ;




 private final String code; // 错误码（5位字符串）
 private final String msg;  // 错误描述

 HttpCodeEnum(String code, String msg) {
  this.code = code;
  this.msg = msg;
 }

 public String getCode() {
  return code;
 }

 public String getMsg() {
  return msg;
 }
}