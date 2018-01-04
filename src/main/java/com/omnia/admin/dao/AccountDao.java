package com.omnia.admin.dao;

import com.omnia.admin.model.FinanceAccount;

import java.util.List;

public interface AccountDao {
    List<String> getAccountTypes();

    List<FinanceAccount> getFinanceAccounts();
}
