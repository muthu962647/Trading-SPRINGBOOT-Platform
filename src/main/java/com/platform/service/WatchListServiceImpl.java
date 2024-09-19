package com.platform.service;

import com.platform.model.Coin;
import com.platform.model.User;
import com.platform.model.Wallet;
import com.platform.model.WatchList;
import com.platform.repositry.WatchListRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements WatchListService{

    @Autowired
    private WatchListRepositry watchListRepositry;


    @Override
    public WatchList findUserWatchList(Long userId) throws Exception {
        WatchList watchList = watchListRepositry.findByUserId(userId);
        if(watchList == null){
                throw new Exception("watchList not Found");
        }

        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepositry.save(watchList);
    }

    @Override
    public WatchList findById(Long id) throws Exception {
        Optional<WatchList> watchListOptional = watchListRepositry.findById(id);
        if(watchListOptional.isEmpty()){
            throw new Exception("watchList Not Found");
        }
        return watchListOptional.get();
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) throws Exception {
        WatchList watchList = findUserWatchList(user.getId());

        if(watchList.getCoins().contains(coin)){
            watchList.getCoins().remove(coin);
        }
        else watchList.getCoins().add(coin);

        watchListRepositry.save(watchList);

        return coin;
    }
}
