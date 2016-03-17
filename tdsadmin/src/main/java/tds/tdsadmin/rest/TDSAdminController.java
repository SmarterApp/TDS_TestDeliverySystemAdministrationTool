package tds.tdsadmin.rest;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
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

/**
 * Handles requests for the application home page.
 */
@Controller
public class TDSAdminController {

	private static final Logger logger = LoggerFactory.getLogger(TDSAdminController.class);

	@Autowired
	TDSAdminDAO _dao = null;

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
		if (StringUtils.isEmpty(extSsId) && StringUtils.isEmpty(ssId) && StringUtils.isEmpty(sessionId)) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST,
					"Needs either external ssid or ssid or session id");
		}
		try {
			results = _dao.getOpportunities(extSsId, sessionId, procedure);
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
			result = _dao.resetOpportunity(v_oppKey, v_requestor, v_reason);
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
			result = _dao.invalidateTestOpportunity(v_oppKey, v_requestor, v_reason);
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
			result = _dao.restoreTestOpportunity(v_oppKey, v_requestor, v_reason);
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
			result = _dao.reopenOpportunity(v_oppKey, v_requestor, v_reason);
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
		if (v_oppKey == null) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, "Needs parameters: oppkey, requestor, reason");
		}
		try {
			result = _dao.extendingOppGracePeriod(v_oppKey, v_requestor, v_selectedsitting, v_doupdate, v_reason);
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
		if (v_oppKey == null) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST, "Needs parameters: oppkey, requestor, reason");
		}
		try {
			result = _dao.alterOpportunityExpiration(v_oppKey, v_requestor, v_dayincrement, v_reason);
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
			result = _dao.setOpportunitySegmentPerm(v_oppKey, v_requestor, v_segmentid, v_segmentposition, v_restoreon,
					v_ispermeable, v_reason);
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@ExceptionHandler(HttpResponseException.class)
	@ResponseBody
	public String handleException(HttpResponseException e) {
		return "HTTP ERROR " + e.getStatusCode() + " : " + e.getMessage();
	}
}
