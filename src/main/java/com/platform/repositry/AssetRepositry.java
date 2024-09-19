package com.platform.repositry;

import com.platform.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepositry extends JpaRepository<Asset,Long> {

    List<Asset> findByUserId(Long userId);

    Asset findByUserIdAndCoinId(Long userId, String coinId);
}
