package com.tang.order.message;

import com.tang.order.service.DeDuplicationService;
import com.tang.order.service.OrderService;
import com.tang.order.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Classname RocketMqTxMsgListener
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/13 13:09
 * @Created by ASUS
 */
@Slf4j
@Component
@RocketMQTransactionListener
public class RocketMqTxMsgListener implements RocketMQLocalTransactionListener {

    @Resource
    private OrderService orderService;

    @Resource
    private DeDuplicationService deDuplicationService;

    /**
     * 消息发送给mq成功进行回调
     * @param msg
     * @param arg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("消息发送成功，执行事务消息的回调..........start");
        try {
            // 取出消息的内容
            Map<String, Object> map = MapperUtils.JsonToMap(
                    new String((byte[])msg.getPayload())
            );

            if (map != null) {

                String orderId = map.get("orderId").toString();

                String txId = map.get("txId").toString();

                // 执行本地事务  创建订单
                boolean result = orderService.createOrderMq(orderId,txId);

                if (result) {

                    log.info("消息发送成功，执行事务消息的回调..........end");

                    // 本地事务执行成功，return COMMIT 消费方可以收到刚刚发送的消息
                    return RocketMQLocalTransactionState.COMMIT;
                }
            }

        } catch (Exception e) {

            log.info("异常发生了，取消这次消息发送....");

            return RocketMQLocalTransactionState.UNKNOWN;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    /**
     * 网络原因，上面的回调返回UNKNOWN,ROLLBACK将触发这个方法，调用事务状态回查,查询创建订单是否成功
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {

        // 取出消息的内容
        Map<String, Object> map = MapperUtils.JsonToMap(
                new String((byte[])msg.getPayload())
        );

        String txId = map.get("txId").toString();

        log.info("事务消息的检查......." + txId);

        // 查询幂等表
        if (deDuplicationService.selectExistsById(txId)) {
            return RocketMQLocalTransactionState.COMMIT;
        }

        log.info("事务消息的检查......." + txId);

        return RocketMQLocalTransactionState.UNKNOWN;
    }
}