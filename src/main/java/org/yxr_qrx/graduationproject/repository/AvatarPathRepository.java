package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yxr_qrx.graduationproject.entity.AvatarPath;

/**
 * @InterfaceName:AvatarPathRepository
 * @Author:41713
 * @Date 2021/11/22  19:57
 * @Version 1.0
 **/
@Repository
public interface AvatarPathRepository extends JpaRepository<AvatarPath,Long> {
    AvatarPath findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE avatar_path SET path = ?2 WHERE user_id = ?1", nativeQuery = true)
    int updatePathByUserId(Long userId,String path);
}
