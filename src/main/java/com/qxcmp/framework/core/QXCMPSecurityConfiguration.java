package com.qxcmp.framework.core;

import com.qxcmp.framework.security.PrivilegeAutowired;
import org.springframework.context.annotation.Configuration;


/**
 * 平台权限配置
 *
 * @author Aaric
 */
@Configuration
@PrivilegeAutowired
public class QXCMPSecurityConfiguration {

    /*
     * 平台内置角色
     * */
    public static final String ROLE_NEWS = "新闻编辑";
    public static final String ROLE_NEWS_DESCRIPTION = "内置角色：可以访问新闻管理、我的文章、我的栏目页面";

    /*
     * 基本权限
     * */
    public static final String PRIVILEGE_SYSTEM_ADMIN = "系统管理员权限";
    public static final String PRIVILEGE_SYSTEM_ADMIN_DESCRIPTION = "可以进入后台系统";
    public static final String PRIVILEGE_ADMIN_SETTINGS = "系统配置管理权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DESCRIPTION = "可以修改系统全局配置";
    public static final String PRIVILEGE_ADMIN_SECURITY = "系统安全管理权限";
    public static final String PRIVILEGE_ADMIN_SECURITY_DESCRIPTION = "可以修改系统安全配置";

    /*
     * 系统工具权限
     * */
    public static final String PRIVILEGE_ADMIN_TOOL = "系统工具使用权限";
    public static final String PRIVILEGE_ADMIN_TOOL_DESCRIPTION = "可以使用系统工具，还需要具有具体工具的使用权限";
    public static final String PRIVILEGE_ADMIN_LOG = "平台日志管理权限";
    public static final String PRIVILEGE_ADMIN_LOG_DESCRIPTION = "可以管理平台日志";
    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT = "广告管理权限";
    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT_DESCRIPTION = "可以管理平台广告";
    public static final String PRIVILEGE_ADMIN_REDEEM = "兑换码管理权限";
    public static final String PRIVILEGE_ADMIN_REDEEM_DESCRIPTION = "可以管理平台兑换码";
    public static final String PRIVILEGE_ADMIN_SPIDER = "蜘蛛管理权限";
    public static final String PRIVILEGE_ADMIN_SPIDER_DESCRIPTION = "可以管理平台蜘蛛";

    /*
     * 用户管理模块
     * */
    public static final String PRIVILEGE_USER = "用户管理权限";
    public static final String PRIVILEGE_USER_DESCRIPTION = "可以查看用户管理入口页面";
    public static final String PRIVILEGE_USER_ROLE = "用户管理权限";
    public static final String PRIVILEGE_USER_ROLE_DESCRIPTION = "可以修改用户角色";
    public static final String PRIVILEGE_USER_STATUS = "用户管理权限";
    public static final String PRIVILEGE_USER_STATUS_DESCRIPTION = "可以修改用户状态";

    /*
     * 财务模块权限
     * */
    public static final String PRIVILEGE_FINANCE = "财务管理权限";
    public static final String PRIVILEGE_FINANCE_DESCRIPTION = "可以查看财务管理入口页面";
    public static final String PRIVILEGE_FINANCE_WEIXIN = "微信支付配置管理权限";
    public static final String PRIVILEGE_FINANCE_WEIXIN_DESCRIPTION = "可以修改微信支付配置";

    /*
     * 消息服务权限
     * */
    public static final String PRIVILEGE_MESSAGE = "消息服务使用权限";
    public static final String PRIVILEGE_MESSAGE_DESCRIPTION = "可以查看消息服务入口页面";
    public static final String PRIVILEGE_MESSAGE_EMAIL_CONFIG = "邮件服务配置权限";
    public static final String PRIVILEGE_MESSAGE_EMAIL_CONFIG_DESCRIPTION = "可以修改邮件服务配置";
    public static final String PRIVILEGE_MESSAGE_EMAIL_SEND = "邮件发送权限";
    public static final String PRIVILEGE_MESSAGE_EMAIL_SEND_DESCRIPTION = "可以进行邮件发送";
    public static final String PRIVILEGE_MESSAGE_SMS_CONFIG = "短信服务配置权限";
    public static final String PRIVILEGE_MESSAGE_SMS_CONFIG_DESCRIPTION = "可以修改短信服务配置";
    public static final String PRIVILEGE_MESSAGE_SMS_SEND = "短信发送权限";
    public static final String PRIVILEGE_MESSAGE_SMS_SEND_DESCRIPTION = "可以进行短信发送";

    /*
     * 新闻模块权限
     * */
    public static final String PRIVILEGE_NEWS = "新闻管理权限";
    public static final String PRIVILEGE_NEWS_DESCRIPTION = "可以查看新闻管理入口页面";
    public static final String PRIVILEGE_NEWS_CHANNEL = "栏目管理权限";
    public static final String PRIVILEGE_NEWS_CHANNEL_DESCRIPTION = "可以对栏目进行管理";
    public static final String PRIVILEGE_NEWS_ARTICLE_AUDIT = "文章审核权限";
    public static final String PRIVILEGE_NEWS_ARTICLE_AUDIT_DESCRIPTION = "可以对平台申请审核的文章进行审核，决定发布或驳回";
    public static final String PRIVILEGE_NEWS_ARTICLE_MANAGEMENT = "文章管理权限";
    public static final String PRIVILEGE_NEWS_ARTICLE_MANAGEMENT_DESCRIPTION = "可以对平台已发布文章进行管理，禁用或者删除";

    /*
     * 微信公众号模块权限
     * */
    public static final String PRIVILEGE_WEIXIN = "微信公众号管理权限";
    public static final String PRIVILEGE_WEIXIN_DESCRIPTION = "可以查看微信公众号管理入口页面";
    public static final String PRIVILEGE_WEIXIN_CONFIG = "微信公众号配置管理权限";
    public static final String PRIVILEGE_WEIXIN_CONFIG_DESCRIPTION = "可以修改微信公众号配置";
    public static final String PRIVILEGE_WEIXIN_MENU = "微信公众号菜单管理权限";
    public static final String PRIVILEGE_WEIXIN_MENU_DESCRIPTION = "可以修改微信公众号菜单";
    public static final String PRIVILEGE_WEIXIN_MATERIAL = "微信公众号素材管理权限";
    public static final String PRIVILEGE_WEIXIN_MATERIAL_DESCRIPTION = "可以管理微信公众号素材";

    /*
     * 商城模块权限
     * */
    public static final String PRIVILEGE_MALL = "商城管理权限";
    public static final String PRIVILEGE_MALL_DESCRIPTION = "可以查看商城管理入口页面";
    public static final String PRIVILEGE_MALL_ORDER = "商城订单管理权限";
    public static final String PRIVILEGE_MALL_ORDER_DESCRIPTION = "可以管理商城所有订单";
    public static final String PRIVILEGE_MALL_COMMODITY = "商城商品管理权限";
    public static final String PRIVILEGE_MALL_COMMODITY_DESCRIPTION = "可以管理商城所有商品";
    public static final String PRIVILEGE_MALL_STORE = "商城店铺管理权限";
    public static final String PRIVILEGE_MALL_STORE_DESCRIPTION = "可以管理商城店铺，店铺所有者和管理员能够管理自己店铺的商品和订单";
}
