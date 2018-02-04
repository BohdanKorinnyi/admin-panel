package com.omnia.admin.dao;

import com.omnia.admin.entity.Wallet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "wallets", collectionResourceRel = "wallets")
public interface WalletRepository extends PagingAndSortingRepository<Wallet, Long> {
}
