package org.yxr_qrx.graduationproject.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.yxr_qrx.graduationproject.entity.Orders;

import java.sql.Timestamp;

/**
 * @ClassName:ComplaintVO
 * @Author:41713
 * @Date 2021/11/12  23:24
 * @Version 1.0
 **/
@Data
public class ComplaintVO {
    private Long id;
    private Orders order;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;
}
