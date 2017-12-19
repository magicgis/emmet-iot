package com.emmet.iot.adapter.jdbc.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.emmet.iot.adapter.jdbc.entity.DeviceStatusLogEntity;

@RepositoryRestResource(collectionResourceRel = "device", path = "device-status-log")
public interface DeviceStatusRepository extends PagingAndSortingRepository<DeviceStatusLogEntity, Long> {

}
