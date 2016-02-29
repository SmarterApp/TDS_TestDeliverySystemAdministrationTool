package tds.tdsadmin.rest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.ProcedureResult;
import tds.tdsadmin.model.TestOpportunity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
@WebAppConfiguration
public class TDSAdminControllerTest {

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@Mock
	TDSAdminDAO _dao;

	@InjectMocks
	@Spy
	TDSAdminController controller;

	@Before
	public void setup() throws ReturnStatusException {
		// _dao = mock(TDSAdminDAOImpl.class);
		// controller._dao = this._dao;
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	// Test with jayway jsonpath
	@Test
	public void getOpportunities() throws Exception {

		OpportunitySerializable opps = getOpps();
		when(_dao.getOpportunities("103", "four-3", null)).thenReturn(opps);
		opps = new OpportunitySerializable();
		when(_dao.getOpportunities("420", null, null)).thenReturn(opps);
		// test with student id and session id
		mockMvc.perform(get("/rest/getOpportunities?extSsId=103&sessionId=four-3")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("Shajib", "Khan")))
				.andExpect(jsonPath("$[*].testName", Matchers.containsInAnyOrder("english", "math")))
				.andExpect(jsonPath("$[*].altSsid", Matchers.containsInAnyOrder("777", "380")))
				.andExpect(jsonPath("$[*].status", Matchers.hasItems("paused", "expired")));

		// test with no request parameter
		mockMvc.perform(get("/rest/getOpportunities")).andExpect(status().isBadRequest());

		// test with non-existing id
		mockMvc.perform(get("/rest/getOpportunities?extSsId=420")).andExpect(jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	public void changeSegementPerm() throws Exception {

		ProcedureResult result = this.getResult("appkey1", "sbac", "na", "success");
		UUID oppkey = UUID.randomUUID();
		when(_dao.setOpportunitySegmentPerm(oppkey, null, "dummysegment", 0, "segment", 0, "na")).thenReturn(result);

		// test with student id and session id
		mockMvc.perform(post("/rest/setOpportunitySegmentPerm").param("oppkey", String.format("%s", oppkey))
				.param("segmentposition", "0").param("segmentid", "dummysegment").param("restoreon", "segment")
				.param("reason", "na").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk())
				// .andExpect(content().contentType("application/json;charset=UTF-8"))

				.andExpect(jsonPath("$.status", Matchers.comparesEqualTo("success")))
				.andExpect(jsonPath("$.appKey", Matchers.comparesEqualTo("appkey1")))
				.andExpect(jsonPath("$.context", Matchers.comparesEqualTo("sbac")))
				.andExpect(jsonPath("$.reason", Matchers.comparesEqualTo("na")));

		// test with no request parameter
		mockMvc.perform(post("/rest/setOpportunitySegmentPerm")).andExpect(status().isBadRequest());

	}

	public OpportunitySerializable getOpps() {
		TestOpportunity opp1 = new TestOpportunity();
		TestOpportunity opp2 = new TestOpportunity();

		opp1.setAltSsid("380");
		opp1.setName("Shajib");
		opp1.setTestName("math");
		opp1.setStatus("paused");

		opp2.setAltSsid("777");
		opp2.setName("Khan");
		opp2.setTestName("english");
		opp2.setStatus("expired");

		OpportunitySerializable opps = new OpportunitySerializable();
		opps.add(opp1);
		opps.add(opp2);

		return opps;
	}

	private ProcedureResult getResult(String appkey, String context, String reason, String status) {
		ProcedureResult result = new ProcedureResult();
		result.setAppKey(appkey);
		result.setContext(context);
		result.setReason(reason);
		result.setStatus(status);
		return result;
	}

}
