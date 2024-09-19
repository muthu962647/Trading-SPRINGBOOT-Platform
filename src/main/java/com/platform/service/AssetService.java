package com.platform.service;

import com.platform.model.Asset;
import com.platform.model.Coin;
import com.platform.model.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity );

    Asset getAssetById(Long assetId) throws Exception;

    Asset getAssetByUserIdAndId(Long userId, Long assetId);

    List<Asset> getUserAsset(Long userId);

    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);
}
