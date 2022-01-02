package org.yxr_qrx.graduationproject.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.yxr_qrx.graduationproject.entity.Address;
import org.yxr_qrx.graduationproject.entity.OrderHistory;
import org.yxr_qrx.graduationproject.entity.RepairType;
import org.yxr_qrx.graduationproject.entity.User;

import java.sql.Timestamp;

/**
 * @ClassName:OrderVO
 * @Author:41713
 * @Date 2021/11/10  21:53
 * @Version 1.0
 **/
@Data
public class OrderVO {
    private Long id;
    private String customName;
    private String repairType;
    private String province;
    private String city;
    private String area;
    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;
}
