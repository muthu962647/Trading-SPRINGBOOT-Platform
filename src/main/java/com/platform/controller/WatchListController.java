package com.platform.controller;

import com.platform.model.Coin;
import com.platform.model.User;
import com.platform.model.WatchList;
import com.platform.service.CoinService;
import com.platform.service.UserService;
import com.platform.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchList")
public class WatchListController {

    @Autowired
    public WatchListService watchListService;

    @Autowired
    public UserService userService;

    @Autowired
    public CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(
            @RequestHeader("Authorization") String jwt
    )throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());
        return ResponseEntity.ok(watchList);
    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList(
            @RequestHeader("Authorization") String jwt
    )throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.createWatchList(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(watchList);
    }

    @GetMapping("/{watchList}")
    public ResponseEntity<WatchList> getWatchListById(
            @PathVariable Long watchListId) throws Exception{
        WatchList watchList = watchListService.findById(watchListId);
        return ResponseEntity.ok(watchList);
    }

    @PatchMapping("add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    )throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin = watchListService.addItemToWatchList(coin, user);
        return ResponseEntity.ok(addedCoin);
    }
}
