package tds.tdsadmin.rest;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	 */
	@RequestMapping(value = "/rest/getOpportunities", method = RequestMethod.GET)
	@ResponseBody
	public OpportunitySerializable getOpportunities(@RequestParam(value = "extSsId", required = false) String extSsId,
			@RequestParam(value = "ssId", required = false) String ssId,
			@RequestParam(value = "sessionId", required = false) String sessionId) {

		OpportunitySerializable results = new OpportunitySerializable();
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
}
