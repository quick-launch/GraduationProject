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
 * @ClassName:ImgPath
 * @Author:41713
 * @Date 2021/11/18  17:17
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ImgPath")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class ImgPath {
    /**
     * 图片存储路径编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 对应订单编号
     */
    @Column(name = "order_id",nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    /**
     * 图片在仓库存储路径
     */
    @Column(name = "path",nullable = false)
    private String path;

    /**
     * 获取图片路径
     */
    @Column(name="download_url",nullable = false)
    private String downloadUrl;
}
