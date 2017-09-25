package com.qxcmp.framework.security;

import com.google.common.collect.Sets;
import com.qxcmp.framework.web.view.annotation.table.*;
import com.qxcmp.framework.web.view.modules.form.FormMethod;
import com.qxcmp.framework.web.view.support.Color;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Set;

import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;

/**
 * 角色实体定义
 *
 * @author aaric
 */
@EntityTable(value = "角色列表", action = QXCMP_BACKEND_URL + "/security/role",
        tableActions = {@TableAction(value = "新建", action = "new", primary = true)},
        rowActions = {@RowAction(value = "编辑", action = "edit", secondary = true), @RowAction(value = "删除", action = "remove", method = FormMethod.POST, color = Color.RED)}
)
@Entity
@Table
@Data
public class Role {

    /**
     * 角色主键，有框架自动生成
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 角色名称
     */
    @TableField("角色名称")
    private String name;

    /**
     * 角色描述
     */
    @TableField("角色描述")
    private String description;

    /**
     * 角色拥有权限集合
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    @TableField(value = "拥有权限", collectionEntityIndex = "name", enableUrl = true, urlPrefix = QXCMP_BACKEND_URL + "/security/privilege", urlEntityIndex = "id", maxCollectionCount = 3)
    private Set<Privilege> privileges = Sets.newHashSet();

    @RowActionCheck("编辑")
    public boolean canEdit() {
        return !StringUtils.equals("ROOT", name);
    }

    @RowActionCheck("删除")
    public boolean canRemove() {
        return !StringUtils.equals("ROOT", name);
    }
}
