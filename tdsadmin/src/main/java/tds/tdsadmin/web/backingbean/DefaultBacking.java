/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.web.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpResponseException;
import org.opentestsystem.shared.security.domain.SbacUser;
import org.opentestsystem.shared.trapi.ITrClient;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.ProcedureResult;
import tds.tdsadmin.model.TestOpportunity;
import tds.tdsadmin.rest.TDSAdminController;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.LazyOppDataModel;

@ManagedBean
@SessionScoped
public class DefaultBacking implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(DefaultBacking.class);

	private static final long serialVersionUID = 1L;
	private String radiossid = null;
	private String extssid = null;
	private String sessionid = null;
	private HashMap<String, String> procedures = null;
	private String procedure = null;
	private List<TestOpportunity> opportunities = new ArrayList<TestOpportunity>();
	private String selectIdText = null;
	private String selectRadioText = null;
	private String requestor = null;
	private String reason = null;
	private int oppCount = 0;
	private LazyDataModel<TestOpportunity> lazyOpps;
	private boolean executeDisabled;
	private String nomatch;
	private String executionResult;

	private HttpServletResponse response;

	private TDSAdminController controller;

	public DefaultBacking() {
		WebApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		TDSAdminDAO tdsAdminDAO = ctx.getBean(TDSAdminDAO.class);
		controller = new TDSAdminController();
		this.controller.setDao(tdsAdminDAO);
		ITrClient trClient = ctx.getBean(ITrClient.class);
		this.controller.setTrClient(trClient);
		response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.setRequestor(user.getEmail());
		if (trClient == null)
			_logger.error("TrClient is null");
		if (tdsAdminDAO == null)
			_logger.error("tdsAdminDAO is null");
	}

	public String getRadiossid() {
		return radiossid;
	}

	public void setRadiossid(String radiossid) {
		this.radiossid = radiossid;
	}

	public String getExtssid() {
		return extssid;
	}

	public void setExtssid(String extssid) {
		this.extssid = extssid;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public HashMap<String, String> getProcedures() {
		procedures = new HashMap<String, String>();
		procedures.put("changeperm", "Change Segment Permeability");
		procedures.put("reset", "Reset Opportunity");
		procedures.put("invalidate", "Invalidate Opportunity");
		procedures.put("restore", "Restore Opportunity");
		procedures.put("reopen", "Reopen Opportunity");
		procedures.put("extend", "Extend Grace Period");
		procedures.put("alter", "Extend Expiration Date");

		return procedures;
	}

	public void setProcedures(HashMap<String, String> procedures) {
		this.procedures = procedures;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public List<TestOpportunity> getOpportunities() {
		return opportunities;
	}

	public void setOpportunities(List<TestOpportunity> opportunities) {
		this.opportunities = opportunities;
	}

	public String getSelectIdText() {
		return selectIdText;
	}

	public void setSelectIdText(String invalidText) {
		this.selectIdText = invalidText;
	}

	public String getSelectRadioText() {
		return selectRadioText;
	}

	public void setSelectRadioText(String selectRadioText) {
		this.selectRadioText = selectRadioText;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getOppCount() {
		return this.opportunities.size();
	}

	public void setOppCount(int oppCount) {
		this.oppCount = oppCount;
	}

	public LazyDataModel<TestOpportunity> getLazyOpps() {
		return lazyOpps;
	}

	public void setLazyOpps(List<TestOpportunity> lazyOpps) {
		this.lazyOpps = new LazyOppDataModel(lazyOpps);
	}

	public boolean getExecuteDisabled() {
		for (TestOpportunity opp : this.opportunities)
			if (opp.getSelected())
				return false;
		return true;
	}

	public void setExecuteDisabled(boolean executeDisabled) {
		this.executeDisabled = executeDisabled;
	}

	public String getNomatch() {
		return nomatch;
	}

	public void setNomatch(String nomatch) {
		this.nomatch = nomatch;
	}

	public String getExecutionResult() {
		return executionResult;
	}

	public void setExecutionResult(String executionResult) {
		this.executionResult = executionResult;
	}

	public boolean searchOpportunity(String extSsId, String sessionId) {
		if (!validateInput(extSsId, sessionId)) {
			_logger.error(String.format("Input validation failed for Ext SSID:%s, SessionId=%s", extSsId, sessionId));
			return false;
		}
		this.opportunities.clear();
		this.setExecutionResult(null);
		this.setNomatch(null);

		String ssId = "ssid".equals(radiossid) ? extSsId : null;
		if (!StringUtils.isEmpty(ssId))
			extSsId = null;
		OpportunitySerializable opps;
		try {
			opps = controller.getOpportunities(response, extSsId, ssId, sessionId, procedure);
			setOpportunities(opps);
			setLazyOpps(opps);
			_logger.info(
					String.format("Fetching opportunities successful for SSID:%s,ExtSSID=%s,SessionId=%s, Procedure=%s",
							ssId, extSsId, sessionId, procedure));
		} catch (HttpResponseException e) {
			_logger.error(e.getMessage(), e);
		}
		if (this.opportunities.size() <= 0)
			setNomatch("No matching opportunity found");
		return true;
	}

	public void execute() {
		String msg = "";
		int success = 0, failure = 0;
		for (TestOpportunity opp : this.opportunities) {
			if (!opp.getSelected())
				continue;
			ProcedureResult result = executeProcedure(opp);
			if (result != null) {
				opp.setResult(result.getStatus());
				opp.setReason(result.getReason());
			} else {
				opp.setResult("failed");
				opp.setReason("No information available.");
			}
			opp.setSelected(false);
			if ("success".equals(opp.getResult()))
				success++;
			else
				failure++;
		}
		msg += "Succes:" + success + ", failure:" + failure;
		this.setExecutionResult(msg);
	}

	private ProcedureResult executeProcedure(TestOpportunity testOpp) {
		ProcedureResult result = null;
		try {
			switch (this.procedure) {
			case "changeperm":
				result = controller.setOpportunitySegmentPerm(response, testOpp.getOppKey(), this.getRequestor(),
						testOpp.getSegmentName(), testOpp.getSegmentPosition(), testOpp.getRestoreOn(),
						testOpp.getIspermeable(), this.getReason());
				break;
			case "alter":
				result = controller.alterOpportunityExpiration(response, testOpp.getOppKey(), this.getRequestor(),
						testOpp.getDayIncrement(), this.getReason());
				break;
			case "extend":
				result = controller.extendingOppGracePeriod(response, testOpp.getOppKey(), this.getRequestor(),
						testOpp.getSelectedSitting(), testOpp.getDoUpdate(), this.getReason());
				break;
			case "reopen":
				result = controller.reopenOpportunity(response, testOpp.getOppKey(), this.getRequestor(),
						this.getReason());
				break;
			case "reset":
				result = controller.resetOpportunity(response, testOpp.getOppKey(), this.getRequestor(),
						this.getReason());
				break;
			case "invalidate":
				result = controller.invalidateTestOpportunity(response, testOpp.getOppKey(), this.getRequestor(),
						this.getReason());
				break;
			case "restore":

				result = controller.restoreTestOpportunity(response, testOpp.getOppKey(), this.getRequestor(),
						this.getReason());

				break;
			}
			_logger.info(String.format("Success for procedure=%s, oppkey=%s", procedure, testOpp.getOppKey()));
		} catch (HttpResponseException e) {
			_logger.error(e.getMessage(), e);
		}
		return result;
	}

	private boolean validateInput(String extSsId, String sessionId) {
		if (StringUtils.isEmpty(extSsId) && StringUtils.isEmpty(sessionId)) {
			String msg = "At least one id is required";
			setSelectIdText(msg);
			return false;
		} else if (!StringUtils.isEmpty(extSsId) && StringUtils.isEmpty(radiossid)) {
			setSelectRadioText("Either SSID or External SSID must be selected");
			return false;
		} else {
			setSelectIdText(null);
			setSelectRadioText(null);
			return true;
		}
	}

	public void procedureChange() {
		/*
		 * for (TestOpportunity opp : this.selectedOpportunities) {
		 * opp.setSelected(false); }
		 */
		this.opportunities.clear();
		// this.selectedOpportunities.clear();
		this.setOppCount(0);
		while (lazyOpps != null)
			this.lazyOpps = null;
		this.setExecutionResult(null);
		this.setNomatch(null);
	}

	public String getStyle(String result) {
		if ("success".equalsIgnoreCase(result))
			return "color:green";
		else
			return "color:red";
	}
}
