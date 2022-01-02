package org.yxr_qrx.graduationproject.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName:OrdersSearchAo
 * @Author:41713
 * @Date 2021/11/25  23:20
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersSearchVO {
   private String repairType;
   private String areaCode;
   private boolean sequence;
}
