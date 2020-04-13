package com.tang.order.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "orders")
public class Orders {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 订单名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 下单时间
     */
    @Column(name = "order_createtime")
    private Date orderCreatetime;

    /**
     * 订单状态 0 已经未支付 1已经支付 2已退单
     */
    @Column(name = "order_state")
    private Integer orderState;

    /**
     * 订单价格
     */
    @Column(name = "order_money")
    private Double orderMoney;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "tx_id")
    private String txId;

}