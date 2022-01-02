package org.yxr_qrx.graduationproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @ClassName:Address
 * @Author:41713
 * @Date 2021/11/18  17:13
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class Address {
    /**
     * 存储地址编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     *用户id
     */
    @Column(name = "custom_id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customId;

    /**
     * 6位地区编码，前两位省份，中间两位市，最后两位区
     */
    @Column(name = "area_code",nullable = false,length = 6)
    private String areaCode;

    /**
     * 详细地址
     */
    @Column(name = "detail",nullable = false)
    private String detail;

    /**
     * 联系电话
     */
    @Column(name = "phone",nullable=false,length = 11)
    @JsonProperty(value ="tel")
    private String phone;

    /**
     * 联系人名字
     */
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * 地址默认状态
     * true-默认地址
     * false-其他地址
     */
    @Column(name = "default_status",nullable = false)
    @JsonProperty("isDefault")
    private boolean defaultStatus;

    @Override
    public String toString() {
        return  " 用户姓名：" + name  +
                ", 用户联系方式：" + phone +
                ", 详细地址：" + detail;
    }
}
