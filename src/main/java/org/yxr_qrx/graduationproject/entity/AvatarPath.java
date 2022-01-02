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
 * @ClassName:AvatarPath
 * @Author:41713
 * @Date 2021/11/22  19:50
 * @Version 1.0
 **/
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "AvatarPath")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer","handler"})
public class AvatarPath {
    /**
     * 图片存储路径编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 对应账号编号
     */
    @Column(name = "user_id",nullable = false,unique = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

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

