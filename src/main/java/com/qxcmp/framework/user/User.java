package com.qxcmp.framework.user;

import com.google.common.collect.Sets;
import com.qxcmp.framework.domain.Label;
import com.qxcmp.framework.security.Role;
import com.qxcmp.framework.support.IDGenerator;
import com.qxcmp.framework.validation.Phone;
import com.qxcmp.framework.validation.Username;
import com.qxcmp.framework.view.annotation.TableView;
import com.qxcmp.framework.view.annotation.TableViewAction;
import com.qxcmp.framework.view.annotation.TableViewField;
import com.qxcmp.framework.view.table.TableViewActionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;

/**
 * 用户实体定义
 * <p>
 * 该实体定义了平台的用户，包含了用户的注册信息，个人信息和权限相关的信息
 *
 * @author aaric
 */
@Entity
@Table
@TableView(name = "user", caption = "用户管理", actionUrlPrefix = QXCMP_BACKEND_URL + "/security/user/", entityIndex = "username", createAction = @TableViewAction(disabled = true), findAction = @TableViewAction(disabled = true), removeAction = @TableViewAction(disabled = true))
@TableView(name = "wechat", caption = "公众号用户", actionUrlPrefix = QXCMP_BACKEND_URL + "/weixin/user/", createAction = @TableViewAction(disabled = true), removeAction = @TableViewAction(disabled = true), updateAction = @TableViewAction(disabled = true), customActions = @TableViewAction(title = "同步", type = TableViewActionEntity.ActionType.TABLE, urlSuffix = "sync", isForm = true))
@Data
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    /**
     * 用户平台ID，由平台自动生成
     * <p>
     * 采用默认主键生成算法 {@link IDGenerator#next()}
     */
    @Id
    private String id;

    /**
     * 用户名，唯一，可用于登录使用
     */
    @Username
    @Column(length = 30, unique = true)
    @TableViewField(name = "user", title = "用户名")
    private String username;

    /**
     * 用户绑定邮箱，唯一，可用于登录使用
     */
    @Email
    @Column(length = 60, unique = true)
    private String email;

    /**
     * 用户绑定手机号，唯一，可用于登录使用
     */
    @Phone
    @Column(length = 20, unique = true)
    private String phone;

    /**
     * 用户OpenID，用于与微信集成
     */
    @Column(unique = true)
    private String openID;

    /**
     * 用户角色，用户的角色角色了用户的权限
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    @TableViewField(name = "user", title = "拥有角色", isCollection = true, collectionEntityIndex = "name", urlEnabled = true, urlPrefix = QXCMP_BACKEND_URL + "/security/role/", urlSuffix = "/edit")
    private Set<Role> roles = Sets.newHashSet();

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户密码修改历史
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> passwordHistory = Sets.newHashSet();

    /**
     * 用户支付密码
     * <p>
     * 支付密码默认为空，且无法支付
     * <p>
     * 用户需要自行设置支付密码
     */
    private String payPassword;

    /**
     * 支付密码修改历史
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> payPasswordHistory = Sets.newHashSet();

    /**
     * 用户账户是否未过期
     */
    @TableViewField(title = "账户是否未过期", name = "user", useI18n = true)
    private boolean accountNonExpired;

    /**
     * 用户账户是否被锁定
     */
    @TableViewField(title = "账户是否未锁定", name = "user", useI18n = true)
    private boolean accountNonLocked;

    /**
     * 用户密码是否未过期
     */
    @TableViewField(title = "密码是否未过期", name = "user", useI18n = true)
    private boolean credentialsNonExpired;

    /**
     * 用户账户是否可用
     */
    @TableViewField(title = "账户是否启用", name = "user", useI18n = true)
    private boolean enabled;

    /**
     * 账户锁定时间
     */
    private Date dateLock = new Date();

    /**
     * 上次登陆时间
     */
    private Date dateLogin = new Date();

    /**
     * 账户创建时间
     */
    private Date dateCreated = new Date();

    /**
     * 上一次密码修改时间
     */
    private Date datePasswordModified = new Date();

    /**
     * 用户真实姓名
     */
    private String name;

    /**
     * 用户昵称
     */
    @TableViewField(name = "wechat", title = "昵称")
    private String nickname;

    /**
     * 用户头像URL
     */
    @TableViewField(name = "wechat", title = "头像", order = 1, isImage = true)
    private String portrait;

    /**
     * 用户是否订阅平台所属的公众号
     */
    @TableViewField(name = "wechat", title = "订阅", useI18n = true)
    private boolean subscribe;

    /**
     * 用户性别
     */
    @TableViewField(name = "wechat", title = "性别")
    private String sex;

    /**
     * 用户所使用语言
     */
    @TableViewField(name = "wechat", title = "语言")
    private String language;

    /**
     * 用户所在城市
     */
    @TableViewField(name = "wechat", title = "城市")
    private String city;

    /**
     * 用户所在省份
     */
    @TableViewField(name = "wechat", title = "省份")
    private String province;

    /**
     * 用户所在国家
     */
    @TableViewField(name = "wechat", title = "国家")
    private String country;

    /**
     * 用户关注平台公众号时间
     */
    private Long subscribeTime;

    /**
     * 用户微信UnionID
     */
    private String unionId;

    /**
     * 用户性别代码
     */
    private Integer sexId;

    /**
     * 用户备注信息
     */
    private String remark;

    /**
     * 用户组ID
     */
    private Integer groupId;

    /**
     * 用户标签ID
     */
    private Long[] tagIds;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 用户个性签名
     */
    private String personalizedSignature;

    /**
     * 用户标签
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Label> labels = Sets.newHashSet();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = Sets.newHashSet();
        roles.forEach(role -> role.getPrivileges().forEach(privilege -> {
            if (!privilege.isDisabled()) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + privilege.getName()));
            }
        }));
        return grantedAuthorities;
    }
}