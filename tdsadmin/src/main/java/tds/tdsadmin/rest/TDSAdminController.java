/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.opentestsystem.shared.security.domain.SbacUser;
import org.opentestsystem.shared.trapi.ITrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.ProcedureResult;
import tds.tdsadmin.validator.TDSValidator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TDSAdminController implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(TDSAdminController.class);

	@Autowired
	private TDSAdminDAO _dao = null;

	@Autowired
	private ITrClient _trClient = null;

	public TDSAdminDAO getDao() {
		return _dao;
	}

	public void setDao(TDSAdminDAO _dao) {
		this._dao = _dao;
	}

	public ITrClient getTrClient() {
		return _trClient;
	}

	public void setTrClient(ITrClient _trClient) {
		this._trClient = _trClient;
	}

	private List<String> getExtSSID(String ssid) {
		List<String> externalssid = new ArrayList<String>();
		String artUri = "student?entityId=" + ssid;
		String response = getTrClient().getForObject(artUri);
		JsonNode node;
		try {
			node = new ObjectMapper().readTree(response);
			node = node.get("searchResults");
			if (node.isArray()) {
				for (JsonNode child : node) {
					String extid = child.get("externalSsid").asText();
					if (ssid.equals(child.get("entityId").asText()) && !StringUtils.isEmpty(extid))
						externalssid.add(extid);
				}
			}
			logger.info(String.format("Parsing the student info from ART is successful for ssid:%s", ssid));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return externalssid;
	}

	/**
	 * @param extSsId
	 * @param ssId
	 * @param sessionId
	 * @return
	 * @throws HttpResponseException
	 */
	@RequestMapping(value = "/rest/getOpportunities", method = RequestMethod.GET)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Read", "ROLE_Opportunity Modify" })
	public OpportunitySerializable getOpportunities(HttpServletResponse response,
			@RequestParam(value = "extSsId", required = false) String extSsId,
			@RequestParam(value = "ssId", required = false) String ssId,
			@RequestParam(value = "sessionId", required = false) String sessionId,
			@RequestParam(value = "procedure", required = false) String procedure) throws HttpResponseException {

		OpportunitySerializable results = new OpportunitySerializable();
		if (StringUtils.isEmpty(procedure)
				|| StringUtils.isEmpty(extSsId) && StringUtils.isEmpty(ssId) && StringUtils.isEmpty(sessionId)) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST,
					"Needs either external ssid or ssid or session id along with procedure");
		}
		TDSValidator validator = new TDSValidator();
		try {
			String studentid = (StringUtils.isNotEmpty(ssId)) ? ssId : extSsId;
			if (StringUtils.isNotEmpty(studentid))
				validator.validateSessionSSID("extssid", studentid);
			if (StringUtils.isNotEmpty(sessionId))
				validator.validateSessionSSID("session", sessionId);
		} catch (ValidatorException e) {
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, e.getMessage());
		}
		try {
			if (!StringUtils.isEmpty(ssId)) {
				for (String extid : getExtSSID(ssId)) {
					results.addAll(_dao.getOpportunities(extid, sessionId, procedure));
				}
			} else
				results = getDao().getOpportunities(extSsId, sessionId, procedure);
			logger.info(
					String.format("Get Opportunities successful for SSID:%s, Ext SSID:%s, SessionID=%s, Procedure=%s",
							ssId, extSsId, sessionId, procedure));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}

		return results;
	}

	@RequestMapping(value = "/rest/resetOpportunity", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult resetOpportunity(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {
		ProcedureResult result = null;
		if (v_oppKey == null) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, "Needs parameters: oppkey, requestor, reason");
		}
		try {
			result = getDao().resetOpportunity(v_oppKey, v_requestor, v_reason);
			logger.info(String.format("Reset successful for Oppkey=%s, Requestor=%s, Reason=%s", v_oppKey, v_requestor,
					v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/rest/invalidateTestOpportunity", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult invalidateTestOpportunity(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {
		ProcedureResult result = null;
		if (v_oppKey == null) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, "Needs parameters: oppkey, requestor, reason");
		}
		try {
			result = getDao().invalidateTestOpportunity(v_oppKey, v_requestor, v_reason);
			logger.info(String.format("Invalidate successful for Oppkey=%s, Requestor=%s, Reason=%s", v_oppKey,
					v_requestor, v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/rest/restoreTestOpportunity", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult restoreTestOpportunity(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {
		ProcedureResult result = null;
		if (v_oppKey == null) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, "Needs parameters: oppkey, requestor, reason");
		}
		try {
			result = getDao().restoreTestOpportunity(v_oppKey, v_requestor, v_reason);
			logger.info(String.format("Restore successful for Oppkey=%s, Requestor=%s, Reason=%s", v_oppKey,
					v_requestor, v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/rest/reopenOpportunity", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult reopenOpportunity(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {
		ProcedureResult result = null;
		if (v_oppKey == null) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, "Needs parameters: oppkey, requestor, reason");
		}
		try {
			result = getDao().reopenOpportunity(v_oppKey, v_requestor, v_reason);
			logger.info(String.format("Reopen successful for Oppkey=%s, Requestor=%s, Reason=%s", v_oppKey, v_requestor,
					v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/rest/extendingOppGracePeriod", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult extendingOppGracePeriod(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "selectedsitting", required = false, defaultValue = "0") int v_selectedsitting,
			@RequestParam(value = "doupdate", required = false) boolean v_doupdate,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {
		ProcedureResult result = null;
		if (v_oppKey == null || v_selectedsitting < 0 || v_selectedsitting > 99) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST,
					"Needs parameters: oppkey, requestor, reason and selectedsitting range:<-365,365> ");
		}
		try {
			result = getDao().extendingOppGracePeriod(v_oppKey, v_requestor, v_selectedsitting, v_doupdate, v_reason);
			logger.info(String.format(
					"Extend grace period successful for Oppkey=%s, Requestor=%s, SelectedSitting=%s, Doupdate=%s, Reason=%s",
					v_oppKey, v_requestor, v_selectedsitting, v_doupdate, v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/rest/alterOpportunityExpiration", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult alterOpportunityExpiration(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "dayincrement", required = false, defaultValue = "0") int v_dayincrement,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {

		ProcedureResult result = null;
		if (v_oppKey == null || v_dayincrement < -365 || v_dayincrement > 365) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST,
					"Needs parameters: oppkey, requestor, reason and dayIncrement range:<-365,365>");
		}
		try {
			result = getDao().alterOpportunityExpiration(v_oppKey, v_requestor, v_dayincrement, v_reason);
			logger.info(String.format("Reset successful for Oppkey=%s, Requestor=%s, Reason=%s", v_oppKey, v_requestor,
					v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/rest/setOpportunitySegmentPerm", method = RequestMethod.POST)
	@ResponseBody
	@Secured({ "ROLE_Opportunity Modify" })
	public ProcedureResult setOpportunitySegmentPerm(HttpServletResponse response,
			@RequestParam(value = "oppkey", required = false) UUID v_oppKey,
			@RequestParam(value = "requestor", required = false) String v_requestor,
			@RequestParam(value = "segmentid", required = false) String v_segmentid,
			@RequestParam(value = "segmentposition", required = false, defaultValue = "0") int v_segmentposition,
			@RequestParam(value = "restoreon", required = false) String v_restoreon,
			@RequestParam(value = "ispermeable", required = false, defaultValue = "0") int v_ispermeable,
			@RequestParam(value = "reason", required = false) String v_reason) throws HttpResponseException {

		ProcedureResult result = null;
		if (v_oppKey == null || StringUtils.isEmpty(v_segmentid) || StringUtils.isEmpty(v_restoreon)) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST,
					"Needs parameters: oppkey, segmentid, restoreon");
		}
		try {
			result = getDao().setOpportunitySegmentPerm(v_oppKey, v_requestor, v_segmentid, v_segmentposition,
					v_restoreon, v_ispermeable, v_reason);
			logger.info(String.format(
					"Set opportunity segment permeability successful for Oppkey=%s, Requestor=%s, SegmentId=%s, SegmentPosition=%s, RestoreOn=%s, Ispermeable=%s, Reason=%s",
					v_oppKey, v_requestor, v_segmentid, v_segmentposition, v_restoreon, v_ispermeable, v_reason));
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@ExceptionHandler(HttpResponseException.class)
	@ResponseBody
	public String handleException(HttpResponseException e) {
		logger.error(e.getMessage());
		return "HTTP ERROR " + e.getStatusCode() + " : " + e.getMessage();
	}
}
