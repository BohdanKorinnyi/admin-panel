package com.omnia.admin.dao;

import com.omnia.admin.entity.Buyer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "buyers", collectionResourceRel = "buyers")
public interface BuyerRepository extends PagingAndSortingRepository<Buyer, Long> {
}
