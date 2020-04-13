package com.tang.stock.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "de_duplication")
public class DeDuplication {
    @Id
    @Column(name = "tx_no")
    private String txNo;

    @Column(name = "create_time")
    private Date createTime;
}