package org.yxr_qrx.graduationproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;

/**
 * @ClassName:User
 * @Author:41713
 * @Date 2021/11/1  0:14
 * @Version 1.0
 **/

/**
 * 用户类
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class User {
    /**
     * 添加role参数，用于赋予权限
     */
    @Enumerated(EnumType.STRING)
    private Role role;
    /**
     * 编号
     * 自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     *账号
     */
    @Column(nullable = false, unique=true, name = "account")
    private String account;

    /**
     * 密码
     * password为8位数以上，包含大小写、数字
     */
    @Column(name = "password",nullable = false)
    private String password;

    /**
     * 名称
     */
    @Column(name = "name",nullable = false,unique=true)
    private String name;

    /**
     * 联系方式
     */
    @Column(name = "phone",nullable = false,unique=true,length = 11)
    private String phone;

    /**
     * 维修方专长
     */
    @OneToOne
    @JoinColumn(name = "repairtype_id")
    private RepairType repairType;



}
