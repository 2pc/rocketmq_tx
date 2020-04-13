package com.tang.stock.service;

import com.tang.stock.domain.DeDuplication;
import com.tang.stock.domain.Stock;
import com.tang.stock.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @Classname StockService
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/11 14:23
 * @Created by ASUS
 */
@Slf4j
@Service
public class StockService {

    @Resource
    private DeDuplicationService deDuplicationService;

    @Resource
    private StockMapper stockMapper;

    /**
     * 减少数量
     * @param orderId 指定的订单库存减少
     * @param txId  幂等性判定
     */
    @Transactional(rollbackFor = Exception.class)
    public void reduceNumber(String orderId, String txId) {

        log.info(Thread.currentThread().getName() + " 开始减少库存............start");

        // 幂等性验证
        if (deDuplicationService.selectExistsById(txId)) {
            return;
        }

        Example example = new Example(Stock.class);

        example.createCriteria()
                .andEqualTo("orderId", orderId);

        Stock stock = stockMapper.selectOneByExample(example);

        stock.setStock( stock.getStock() - 1 );
        //                                       要更新的实体属性， 条件
        int i = stockMapper.updateByExampleSelective(stock, example);

        // 插入日志表
        if (i > 0) {

            DeDuplication deDuplication = new DeDuplication();

            deDuplication.setTxNo(txId);

            deDuplication.setCreateTime(new Date());

            deDuplicationService.addTx(deDuplication);

            log.info("开始减少库存............end");
        }

    }

}