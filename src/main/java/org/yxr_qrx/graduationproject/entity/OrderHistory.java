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
 * @ClassName:OrderHistory
 * @Author:41713
 * @Date 2021/11/18  17:18
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "OrderHistory")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class OrderHistory {
    /**
     * 订单历史记录编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 对应订单编号
     */
    @Column(name = "order_id",unique = true,nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    /**
     * 订单创建时间
     */
    @Column(name = "create_date",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

    /**
     * 订单通过审核时间
     */
    @Column(name = "pend_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp pendDate;

    /**
     * 订单被接手时间
     */
    @Column(name = "pick_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp pickDate;

    /**
     * 订单完成时间
     */
    @Column(name = "finish_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp finishDate;

    /**
     * 订单状态
     */
    @Column(name = "order_status",nullable = false,columnDefinition = "int DEFAULT 0")
    private int status;

}
