package com.platform.service;

import com.platform.model.Asset;
import com.platform.model.Coin;
import com.platform.model.User;
import com.platform.repositry.AssetRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService{

    @Autowired
    private AssetRepositry assetRepositry;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());

        return assetRepositry.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        return assetRepositry.findById(assetId).orElseThrow(() -> new Exception("Assert not Found"));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAsset(Long userId) {
        return assetRepositry.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {

        Asset oldAsset = getAssetById(assetId);
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());
        return assetRepositry.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepositry.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepositry.deleteById(assetId);
    }
}
