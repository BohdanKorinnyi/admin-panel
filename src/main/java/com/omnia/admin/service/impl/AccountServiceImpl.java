package com.omnia.admin.service.impl;

import com.omnia.admin.dao.AccountDao;
import com.omnia.admin.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Override
    public List<String> getAccountTypes() {
        return accountDao.getAccountTypes();
    }
}
