package com.qxcmp.framework.mall;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 平台充值订单
 * <p>
 * 平台充值流程如下 <ol> <li>生成充值订单</li> <li>调用相关支付接口： 微信、支付宝</li> <li>用户完成支付操作</li> <li>收到支付接口发来的回调消息，消息里面应该包含充值订单号</li>
 * <li>调用平台充值服务接口处理充值订单</li> <li>处理订单成功以后，给用户的钱包里面增加对应金钱</li> </ol>
 *
 * @author aaric
 */
@Entity
@Table
@Data
public class DepositOrder {

    /**
     * 订单号，由平台自动生成
     */
    @Id
    private String id;

    /**
     * 订单对应的用户ID
     */
    private String userId;

    /**
     * 订单金额，单位为分
     */
    private int fee;

    /**
     * 货币类型，默认为CNY、即人民币
     * <p>
     * 具体值参考 <a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">货币类型</a>
     */
    private String feeType = "CNY";

    /**
     * 订单描述
     */
    private String description;

    /**
     * 订单起始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeStart;

    /**
     * 订单结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeEnd;

    /**
     * 订单状态
     */
    private OrderStatusEnum status;

}
