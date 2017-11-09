package com.omnia.admin.dao;

import com.omnia.admin.model.Buyer;

import java.util.List;

public interface BuyerDao {
    List<String> getBuyersName();

    List<Buyer> getBuyers();

    void saveBuyers(List<Buyer> buyers);

    void updateBuyers(List<Buyer> buyers);

    String getBuyerById(int buyerId);
}
