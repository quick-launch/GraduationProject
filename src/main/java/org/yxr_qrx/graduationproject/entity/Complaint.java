package org.yxr_qrx.graduationproject.entity;

/**
 * @ClassName:Complaint
 * @Author:41713
 * @Date 2021/11/9  14:46
 * @Version 1.0
 **/

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "complaint")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class Complaint {
    /**
     * 投诉单编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 投诉单内容
     */
    @Column(name ="content",nullable = false)
    private String content;

    /**
     * 投诉单关联订单
     */
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;


    /**
     * 是否已读，默认false未读
     */
    @Column(name = "complaint_status",nullable = false,columnDefinition = "tinyint(1) DEFAULT 0")
    private boolean status;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

}
