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
 * @ClassName:Messages
 * @Author:41713
 * @Date 2021/11/18  17:17
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "messages")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class Message {
    /**
     * 消息编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 消息接收人
     */
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    /**
     * 消息种类
     * 1-管理员通知
     * 2-订单指定提示
     */
    @Column(name = "type",nullable = false)
    private int type;

    /**
     * 消息是否已读。false-未读，true-已读
     */
    @Column(name = "message_status",nullable = false,columnDefinition = "tinyint(1) DEFAULT 0")
    private boolean messageStatus;

    /**
     * 消息内容
     */
    @Column(name = "content",nullable = false)
    private String content;

    /**
     * 消息创建时间
     */
    @CreatedDate
    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;
}
