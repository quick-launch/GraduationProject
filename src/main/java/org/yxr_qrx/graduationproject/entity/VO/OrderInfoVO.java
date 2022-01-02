package org.yxr_qrx.graduationproject.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yxr_qrx.graduationproject.entity.ImgPath;
import org.yxr_qrx.graduationproject.entity.Orders;

import java.util.List;

/**
 * @ClassName:OrderInfoVO
 * @Author:41713
 * @Date 2021/12/6  15:26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoVO {
    private Orders order;
    private List<ImgPath> imgPaths;
}
