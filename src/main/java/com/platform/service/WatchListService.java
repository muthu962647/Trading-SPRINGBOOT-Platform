package com.platform.service;

import com.platform.model.Coin;
import com.platform.model.User;
import com.platform.model.WatchList;

public interface WatchListService {

    WatchList findUserWatchList(Long userId) throws Exception;

    WatchList createWatchList(User user);

    WatchList findById(Long id) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;
}
