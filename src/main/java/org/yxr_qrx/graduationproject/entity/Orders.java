package org.yxr_qrx.graduationproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @ClassName:Order
 * @Author:41713
 * @Date 2021/11/1  0:17
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "orders")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class Orders {

    /**
     * 订单编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单发起人编号
     */
    @Column(name = "custom_id",nullable=false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customId;

    /**
     * 订单发起人编号
     */
    @Column(name = "custom_name",nullable=false)
    private String customName;

    /**
     * 订单具体描述
     */
    @Column(name = "problem", nullable = false)
    private String problem;

    /**
     * 维修地址
     */
    @Column(name = "address",nullable = false)
    private String address;

    /**
     * 省份编号
     */
    @Column(name = "province",nullable = false,length = 2)
    private String province;

    /**
     * 城市编码
     */
    @Column(name = "city",nullable = false,length = 2)
    private String city;

    /**
     * 下属区编码
     */
    @Column(name = "area",nullable = false,length = 2)
    private String area;

    /**
     * 订单状态
     * 0-等待审核
     * 1-通过审核等待接手或者被指定维修人员拒接
     * 2-已通过审核待指定维修人员接手
     * 3-已被维修人员接手
     * 4-已完成订单
     * 5-已支付订单
     * -1 -未通过审核
     */
    @Column(name = "order_status", nullable = false,columnDefinition = "int DEFAULT 0")
    private int status;

    /**
     * 维修人员
     */
    @OneToOne
    @JoinColumn(name="repairman_id")
    private User repairman;

    /**
     * 订单需求维修方专长
     */
    @Column(name = "repairtype")
    private String repairType;

    /**
     * 订单跟踪记录
     */
    @OneToOne
    @JoinColumn(name = "orderhistory_id",unique = true)
    private OrderHistory orderhistory;

    /**
     * 创建时间编号
     */
    @CreatedDate
    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;
}

