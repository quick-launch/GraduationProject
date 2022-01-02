package org.yxr_qrx.graduationproject.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName:SearchDTO
 * @Author:41713
 * @Date 2021/12/2  17:09
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageVO {
    private int page;
    private int pageSize;
}
