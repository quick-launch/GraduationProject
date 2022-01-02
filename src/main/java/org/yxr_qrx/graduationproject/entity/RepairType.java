package org.yxr_qrx.graduationproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @ClassName:RepairType
 * @Author:41713
 * @Date 2021/11/18  17:26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="repairType")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class RepairType {
    /**
     * 维修种类编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 维修种类
     */
    @Column(name = "name",nullable = false,unique = true)
    private String name;
}
