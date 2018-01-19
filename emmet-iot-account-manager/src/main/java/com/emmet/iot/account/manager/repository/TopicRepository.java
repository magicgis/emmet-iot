package com.emmet.iot.account.manager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.emmet.iot.account.manager.entity.TopicEntity;

public interface TopicRepository extends PagingAndSortingRepository<TopicEntity, String>{

}
