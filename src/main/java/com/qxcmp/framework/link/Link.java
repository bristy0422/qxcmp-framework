package com.qxcmp.framework.link;

import com.qxcmp.framework.web.view.annotation.table.EntityTable;
import com.qxcmp.framework.web.view.annotation.table.RowAction;
import com.qxcmp.framework.web.view.annotation.table.TableAction;
import com.qxcmp.framework.web.view.annotation.table.TableField;
import com.qxcmp.framework.web.view.modules.form.FormMethod;
import com.qxcmp.framework.web.view.support.Color;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;

/**
 * 链接实体
 * <p>
 * 可用于管理友情链接、热门标签等
 *
 * @author Aaric
 */
@EntityTable(value = "链接管理", action = QXCMP_BACKEND_URL + "/link",
        tableActions = @TableAction(value = "添加链接", action = "new", primary = true),
        rowActions = {
                @RowAction(value = "编辑", action = "edit", secondary = true),
                @RowAction(value = "删除", action = "remove", method = FormMethod.POST, color = Color.RED)
        })
@Entity
@Table
@Data
public class Link {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 链接文本
     */
    @TableField("名称")
    private String title;

    /**
     * 链接类型
     */
    @TableField("类型")
    private String type;

    /**
     * 创建日期
     */
    private Date dateCreated;

    /**
     * 修改日期
     */
    private Date dateModified;

    /**
     * 超链接
     */
    private String href;

    /**
     * 打开方式
     */
    private String target;

    /**
     * 优先级
     */
    private int sort;
}
