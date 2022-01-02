package org.yxr_qrx.graduationproject.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yxr_qrx.graduationproject.entity.AvatarPath;
import org.yxr_qrx.graduationproject.entity.User;

/**
 * @ClassName:UserInfoVO
 * @Author:41713
 * @Date 2021/12/6  15:42
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVO {
    private User user;
    private AvatarPath avatarPath;
}
