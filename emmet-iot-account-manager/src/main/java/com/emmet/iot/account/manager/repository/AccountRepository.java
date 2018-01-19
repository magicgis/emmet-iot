package com.emmet.iot.account.manager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.emmet.iot.account.manager.entity.AccountEntity;

public interface AccountRepository extends PagingAndSortingRepository<AccountEntity, String>{

}
