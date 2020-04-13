package com.tang.order.service;

import com.tang.order.domain.DeDuplication;
import com.tang.order.mapper.DeDuplicationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname DeDuplicationService
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/13 13:57
 * @Created by ASUS
 */
@Service
public class DeDuplicationService {

    @Resource
    private DeDuplicationMapper deDuplicationMapper;

    public void addTx(DeDuplication deDuplication) {

        deDuplicationMapper.insert(deDuplication);

    }

    public boolean selectExistsById(String txId) {
        return deDuplicationMapper.existsWithPrimaryKey(txId);
    }

}