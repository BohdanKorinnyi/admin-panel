package com.omnia.admin.service;

import com.omnia.admin.model.Buyer;

import java.util.List;

public interface BuyerService {
    List<String> getBuyersName();

    List<Buyer> getBuyers();
}
