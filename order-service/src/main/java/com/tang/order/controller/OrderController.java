package com.tang.order.controller;

import com.tang.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Classname OrderController
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/11 12:26
 * @Created by ASUS
 */
@RestController
@RequestMapping("/com/tang/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/create")
    public Map<String, Object> createOrder() {

        String orderId = "efd8e38168614076a4b21f6b591df70f";

        String txId = UUID.randomUUID().toString().replaceAll("-", "");

        orderService.sendStockReduceMq(orderId,txId);

        Map<String, Object> map = new HashMap<>(8);

        map.put("是否创建成功", true);

        return map;
    }

}