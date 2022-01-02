package org.yxr_qrx.graduationproject.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yxr_qrx.graduationproject.entity.Orders;

/**
 * @ClassName:OrderUpdateDTO
 * @Author:41713
 * @Date 2021/11/26  11:04
 * @Version 1.0
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateVO {
    private String problem;
    private Long addressId;
    private Long repairTypeId;
    private Long repairmanId;
}
