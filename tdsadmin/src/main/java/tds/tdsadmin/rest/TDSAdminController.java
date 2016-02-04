package tds.tdsadmin.rest;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.OpportunitySerializable;

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
	public OpportunitySerializable getOpportunities(HttpServletResponse response,
			@RequestParam(value = "extSsId", required = false) String extSsId,
			@RequestParam(value = "ssId", required = false) String ssId,
			@RequestParam(value = "sessionId", required = false) String sessionId) throws HttpResponseException {

		OpportunitySerializable results = new OpportunitySerializable();
		if (StringUtils.isEmpty(extSsId) && StringUtils.isEmpty(ssId) && StringUtils.isEmpty(sessionId)) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			throw new HttpResponseException(HttpStatus.SC_BAD_REQUEST,
					"Needs either external ssid or ssid or session id");
		}
		try {
			results = _dao.getOpportunities(extSsId, sessionId);
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}

		return results;
	}

	@RequestMapping(value = "/rest/resetOpportunity", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/rest/invalidateTestOpportunity", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/rest/restoreTestOpportunity", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/rest/reopenOpportunity", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/rest/extendingOppGracePeriod", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/rest/alterOpportunityExpiration", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/rest/setOpportunitySegmentPerm", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, boolean v_ispermeable,
			String v_restoreon, String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@ExceptionHandler(HttpResponseException.class)
	@ResponseBody
	public String handleException(HttpResponseException e) {
		return "HTTP ERROR " + e.getStatusCode() + " : " + e.getMessage();
	}
}
