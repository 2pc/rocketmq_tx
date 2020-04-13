package com.tang.order.service;

import com.tang.order.domain.DeDuplication;
import com.tang.order.domain.Orders;
import com.tang.order.mapper.OrdersMapper;
import com.tang.order.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname OrderService
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/11 12:27
 * @Created by ASUS
 */
@Service
@Slf4j
public class OrderService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private DeDuplicationService deDuplicationService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送库存减少的消息
     *
     * @param orderId
     * @param txId
     * @return
     */
    public void sendStockReduceMq(String orderId,String txId) {

        log.info("开始发送消息....");

        Map<String, String> map = new HashMap<>();

        map.put("orderId", orderId);
        map.put("txId", txId);

        // 构建消息
        Message<String> message = MessageBuilder.withPayload(MapperUtils.ObjectToJson(map)).build();

        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction("topic_tx_msg",
                message, null);

        System.out.println( "执行结果...."+sendResult );
    }

    /**
     * 创建订单记录
     * @param orderId
     * @param txId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrderMq(String orderId, String txId) {

        log.info("开始创建订单........start");

        Orders orders = new Orders();
        orders.setName("蚂蚁课堂");
        orders.setOrderCreatetime(new Date());
        orders.setOrderState(0);
        orders.setOrderMoney(10.0);
        orders.setOrderId(orderId);
        orders.setTxId(txId);

        DeDuplication deDuplication = new DeDuplication();
        deDuplication.setCreateTime(new Date());
        deDuplication.setTxNo(txId);

        // 写入日志，做幂等验证
        deDuplicationService.addTx(deDuplication);

        log.info("开始创建订单........end");

        if (true) {
//            throw new RuntimeException("手动错误...");
        }

        return ordersMapper.insert(orders) == 1;
    }

}