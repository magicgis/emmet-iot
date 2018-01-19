package com.emmet.iot.account.manager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmet.iot.account.manager.entity.AccountEntity;
import com.emmet.iot.account.manager.entity.SubjectEntity;
import com.emmet.iot.account.manager.entity.TopicEntity;
import com.emmet.iot.account.manager.repository.AccountRepository;
import com.emmet.iot.account.manager.repository.SubjectRepository;
import com.emmet.iot.account.manager.repository.TopicRepository;
import com.emmet.iot.account.manager.rest.vo.AddTopicRequest;

@RestController
@RequestMapping("/api")
public class AccountResource {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TopicRepository topicRepository;

	@RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAccount(@PathVariable("id") String id) {

		AccountEntity account = accountRepository.findOne(id);
		if (account != null) {
			return new ResponseEntity<AccountEntity>(account, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();

	}

	@RequestMapping(path = "/account", method = RequestMethod.POST)
	public ResponseEntity<?> createAccount(@RequestBody AccountEntity account) {

		if (accountRepository.findOne(account.getId()) != null) {
			return new ResponseEntity<>("Existed account.", HttpStatus.BAD_REQUEST);
		}

		AccountEntity accountEntity = accountRepository.save(account);

		return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/account/{id}/subject", method = RequestMethod.POST)
	public ResponseEntity<?> createSubject(@PathVariable("id") String accountId, @RequestBody SubjectEntity subject) {

		AccountEntity account = accountRepository.findOne(accountId);
		if (account == null) {
			return new ResponseEntity<>("Account not exist.", HttpStatus.BAD_REQUEST);
		}

		subject.setAccount(account);

		return new ResponseEntity<SubjectEntity>(subjectRepository.save(subject), HttpStatus.CREATED);

	}

	@RequestMapping(path = "/account/{aid}/subject/{sid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> createSubject(@PathVariable("aid") String accountId,
			@PathVariable("sid") String subjectId) {
		AccountEntity account = accountRepository.findOne(accountId);
		if (account == null) {
			return new ResponseEntity<>("Account not exist.", HttpStatus.BAD_REQUEST);
		}

		SubjectEntity subject = subjectRepository.findOne(subjectId);
		if (subject == null) {
			return new ResponseEntity<>("Subject not exist.", HttpStatus.BAD_REQUEST);
		}
		subjectRepository.delete(subject);
		
		return new ResponseEntity<AccountEntity>(account, HttpStatus.OK);

	}

	@RequestMapping(path = "/topic", method = RequestMethod.POST)
	public ResponseEntity<?> createTopic(@RequestBody TopicEntity topic) {

		return new ResponseEntity<TopicEntity>(topicRepository.save(topic), HttpStatus.CREATED);

	}

	/**
	 * Relate a topic to a subject
	 * 
	 * @param accountId
	 * @param subjectId
	 * @param request
	 * @return
	 */

	@RequestMapping(path = "/account/{aid}/subject/{sid}/topic", method = RequestMethod.POST)
	public ResponseEntity<?> addTopicToSubject(@PathVariable("aid") String accountId,
			@PathVariable("sid") String subjectId, @RequestBody AddTopicRequest request) {

		SubjectEntity subject = subjectRepository.findOne(subjectId);
		if (subject == null) {
			return new ResponseEntity<>("Subject not exist.", HttpStatus.BAD_REQUEST);
		}

		TopicEntity topic = topicRepository.findOne(request.getTopicId());
		if (topic == null) {
			return new ResponseEntity<>("Topic not exist.", HttpStatus.BAD_REQUEST);
		}

		subject.getTopics().add(topic);

		return new ResponseEntity<TopicEntity>(topicRepository.save(topic), HttpStatus.OK);

	}

	@RequestMapping(path = "/account/{aid}/subject/{sid}/topic/{tid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> addTopicToSubject(@PathVariable("aid") String accountId,
			@PathVariable("sid") String subjectId, @PathVariable("tid") String topicId) {

		SubjectEntity subject = subjectRepository.findOne(subjectId);
		if (subject == null) {
			return new ResponseEntity<>("Subject not exist.", HttpStatus.BAD_REQUEST);
		}

		TopicEntity topic = topicRepository.findOne(topicId);
		if (topic == null) {
			return new ResponseEntity<>("Topic not exist.", HttpStatus.BAD_REQUEST);
		}

		subject.getTopics().remove(topic);
		topicRepository.save(topic);

		return new ResponseEntity<TopicEntity>(topicRepository.save(topic), HttpStatus.OK);
	}
}
