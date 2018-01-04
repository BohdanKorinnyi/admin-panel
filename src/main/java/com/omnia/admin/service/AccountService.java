package com.omnia.admin.service;

import com.omnia.admin.model.FinanceAccount;

import java.util.List;

public interface AccountService {
    List<String> getAccountTypes();

    List<FinanceAccount> getFinanceAccounts();
}
