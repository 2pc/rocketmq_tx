package com.tang.stock.listener;

import com.tang.stock.mapper.StockMapper;
import com.tang.stock.service.StockService;
import com.tang.stock.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Classname ListenerMessage
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/11 15:58
 * @Created by ASUS
 */
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "rocket-tx-group",topic = "topic_tx_msg")
public class ListenerMessage implements RocketMQListener<String> {

    @Resource
    private StockService stockService;

    @Override
    public void onMessage(String message) {

        log.info("接收到的消息为--->" + message);

        try {

            Map map = MapperUtils.JsonToObject(message, Map.class);

            String orderId = map.get("orderId").toString();

            String txId = map.get("txId").toString();
            //========================
            // 减少库存
            stockService.reduceNumber(orderId,txId);
            //========================

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}