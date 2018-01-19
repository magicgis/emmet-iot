package com.emmet.iot.account.manager.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subject")
public class SubjectEntity {
	
	@Id
	private String id;
	private String name;
	
	@ManyToOne
	@JsonIgnore
	private AccountEntity account;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<TopicEntity> topics;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TopicEntity> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicEntity> topics) {
		this.topics = topics;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}
	
	

}
