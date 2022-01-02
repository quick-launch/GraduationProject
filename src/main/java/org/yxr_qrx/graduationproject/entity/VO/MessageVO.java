package org.yxr_qrx.graduationproject.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * @ClassName:MessageVO
 * @Author:41713
 * @Date 2021/11/25  16:29
 * @Version 1.0
 **/
@Data
public class MessageVO {
    private Long id;
    private int type;
    private boolean messageStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;
    private String content;
}
