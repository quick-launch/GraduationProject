package org.yxr_qrx.graduationproject.entity;

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

/**
 * @ClassName:Evaluation
 * @Author:41713
 * @Date 2021/11/9  14:50
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "evaluation")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class Evaluation {
    /**
     * 评论编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 评价等级，一共5级
     * 1极差
     * 2较差
     * 3一般
     * 4较好
     * 5极好
     */
    @Column(name = "level", nullable = false)
    private int level;

    /**
     * 评论内容
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 被评价订单编号
     */
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    /**
     * 是否已读，默认false未读
     */
    @Column(name = "evaluation_status",nullable = false,columnDefinition = "tinyint(1) DEFAULT 0")
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
