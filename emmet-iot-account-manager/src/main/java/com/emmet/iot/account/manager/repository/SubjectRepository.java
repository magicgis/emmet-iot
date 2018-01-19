package com.emmet.iot.account.manager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.emmet.iot.account.manager.entity.SubjectEntity;

public interface SubjectRepository extends PagingAndSortingRepository<SubjectEntity, String>{

}
