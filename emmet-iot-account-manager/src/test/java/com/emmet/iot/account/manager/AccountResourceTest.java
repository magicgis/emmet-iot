package com.emmet.iot.account.manager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.emmet.iot.account.manager.entity.AccountEntity;
import com.emmet.iot.account.manager.entity.SubjectEntity;
import com.emmet.iot.account.manager.entity.TopicEntity;
import com.emmet.iot.core.util.JsonHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootConfiguration()
@WebAppConfiguration
@Profile("test")
@SpringBootTest(classes = AccountServiceApplication.class)
public class AccountResourceTest {

	@Autowired
	private WebApplicationContext context;

	MockMvc mvc;

	AccountEntity account;
	SubjectEntity subject;
	TopicEntity topic;

	@Before
	public void init() {

		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).alwaysDo(print()).build();

		account = new AccountEntity();
		account.setId("A01");
		account.setName("John Doe");

		subject = new SubjectEntity();
		subject.setId("S01");
		subject.setName("Subject 1");

		topic = new TopicEntity();
		topic.setId("T01");
		topic.setName("Topic 1");

	}

	@Test
	public void test() throws Exception {

		// create a new account
		mvc.perform(post("/api/account").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(JsonHelper.ObjectToJsonString(account)))//
				.andExpect(status().isCreated());//

		// confirm
		mvc.perform(get("/api/account/" + account.getId()))//
				.andExpect(jsonPath("name", equalTo(account.getName())));

		mvc.perform(get("/api/account"))//
				.andExpect(jsonPath("$[0].name", equalTo(account.getName())));

		// create a new subject for the account
		mvc.perform(post("/api/account/" + account.getId() + "/subject")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JsonHelper.ObjectToJsonString(subject)))//
				.andExpect(status().isCreated());//

		// confirm
		mvc.perform(get("/api/account/" + account.getId()))//
				.andExpect(jsonPath("subjects[0].name", equalTo(subject.getName())));

		// create a topic
		mvc.perform(post("/api/topic/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(JsonHelper.ObjectToJsonString(topic)))//
				.andExpect(status().isCreated());//

		// add a topic to specific subject
		mvc.perform(post("/api/account/A01/subject/S01/topic").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content("{\"topicId\":\"T01\"}"))//
				.andExpect(status().isOk());//

		// confirm
		mvc.perform(get("/api/account/" + account.getId()))//
				.andExpect(jsonPath("subjects[0].topics[0].name", equalTo(topic.getName())));

		// remove a topic from subject
		mvc.perform(delete("/api/account/A01/subject/S01/topic/T01")).andExpect(status().isOk());//

		// confirm
		mvc.perform(get("/api/account/" + account.getId()))//
				.andExpect(jsonPath("subjects[0].topics").isEmpty());

		// remove a subject of a account
		mvc.perform(delete("/api/account/A01/subject/S01")).andExpect(status().isOk());//

		// confirm
		mvc.perform(get("/api/account/" + account.getId()))//
				.andExpect(jsonPath("subjects").isEmpty());

		// .andExpect(jsonPath("customerId", equalTo("Bar")))//
		// .andExpect(jsonPath("name", equalTo("Foo")))//
		// .andDo(modifyResponseTo(ResponsePostProcessors.prettyPrintContent())//
		// .andDocument("get_customer")//
		// .withResponseFields(//
		// fieldWithPath("customerId").description("The Customer ID"),
		// fieldWithPath("name").description("The Customer Name")));
		//
	}

}
