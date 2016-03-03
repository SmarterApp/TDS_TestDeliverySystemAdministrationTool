package tds.tdsadmin.web.backingbean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.ProcedureResult;
import tds.tdsadmin.model.TestOpportunity;
import tds.tdsadmin.model.LazyOppDataModel;

@ManagedBean
@SessionScoped
public class DefaultBacking implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String radiossid = null;
	private String extssid = null;
	private String sessionid = null;
	private HashMap<String, String> procedures = null;
	private String procedure = null;
	private List<TestOpportunity> opportunities = new ArrayList<TestOpportunity>();
	// private List<TestOpportunity> selectedOpportunities = new
	// ArrayList<TestOpportunity>();
	private String selectIdText = null;
	private String selectRadioText = null;
	private String requestor = null;
	private String reason = null;
	private int oppCount = 0;
	private LazyDataModel<TestOpportunity> lazyOpps;
	private boolean executeDisabled;
	private boolean nomatch;

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
		/*
		 * if (this.selectedOpportunities != null &&
		 * this.selectedOpportunities.size() > 0) return false;
		 */
		for (TestOpportunity opp : this.opportunities)
			if (opp.getSelected())
				return false;
		return true;
	}

	public void setExecuteDisabled(boolean executeDisabled) {
		this.executeDisabled = executeDisabled;
	}

	public boolean getNomatch() {
		return nomatch;
	}

	public void setNomatch(boolean nomatch) {
		this.nomatch = nomatch;
	}

	public boolean searchOpportunity(String extSsId, String sessionId) {
		if (!validateInput(extSsId, sessionId))
			return false;

		// clear selectedOpportunities
		// this.selectedOpportunities.clear();
		this.opportunities.clear();

		HttpURLConnection connection = null;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = null;
		try {
			path = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
					request.getContextPath()).toString();
		} catch (MalformedURLException e1) {
			return false;
		}

		String url = path + "/rest/getOpportunities?extSsId=%s&sessionId=%s&procedure=%s";
		url = String.format(url, extSsId, sessionId, procedure);

		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			connection.connect();
			int status = connection.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				ObjectMapper mapper = new ObjectMapper();
				OpportunitySerializable opps = mapper.readValue(sb.toString(), OpportunitySerializable.class);
				setOpportunities(opps);
				setLazyOpps(opps);
				System.out.print(sb.toString());
			}

		} catch (IOException e) {
		} finally {
			connection.disconnect();
		}
		if (this.opportunities.size() <= 0)
			setNomatch(true);
		return true;
	}

	public void execute() {
		String api = null;
		switch (this.procedure) {
		case "changeperm":
			api = "setOpportunitySegmentPerm";
			break;
		case "alter":
			api = "alterOpportunityExpiration";
			break;
		case "extend":
			api = "extendingOppGracePeriod";
			break;
		case "reopen":
			api = "reopenOpportunity";
			break;
		case "reset":
			api = "resetOpportunity";
			break;
		case "invalidate":
			api = "invalidateTestOpportunity";
			break;
		case "restore":
			api = "restoreTestOpportunity";
			break;
		}
		for (TestOpportunity opp : this.opportunities) {
			if (!opp.getSelected())
				continue;
			ProcedureResult result = executeProcedure(opp, api);
			if (result != null) {
				opp.setResult(result.getStatus());
				opp.setReason(result.getReason());
			} else {
				opp.setResult("failed");
				opp.setReason("No information available.");
			}
			opp.setSelected(false);
		}
	}

	private ProcedureResult executeProcedure(TestOpportunity testOpp, String api) {
		HttpURLConnection connection = null;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = null;
		try {
			path = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
					request.getContextPath()).toString();
		} catch (MalformedURLException e1) {

		}

		String url = path + "/rest/" + api;
		String urlParameters = getUrlParams(testOpp);
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;

		ProcedureResult result = null;

		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			connection.setUseCaches(false);
			connection.getOutputStream().write(postData);

			int status = connection.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				ObjectMapper mapper = new ObjectMapper();
				result = mapper.readValue(sb.toString(), ProcedureResult.class);

				System.out.print(sb.toString());
			}

		} catch (IOException e) {
		} finally {
			connection.disconnect();
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

	private String getUrlParams(TestOpportunity testOpp) {
		String urlParameters = "oppkey=%s&requestor=%s&reason=%s";
		urlParameters = String.format(urlParameters, testOpp.getOppKey(), this.getRequestor(), this.getReason());

		if ("changeperm".equals(procedure)) {
			urlParameters = urlParameters + "&segmentid=%s&segmentposition=%s&restoreon=%s&ispermeable=%s";
			urlParameters = String.format(urlParameters, testOpp.getSegmentName(), testOpp.getSegmentPosition(),
					testOpp.getRestoreOn(), testOpp.getIspermeable());
		} else if ("extend".equals(procedure)) {
			urlParameters += "&selectedsitting=%s&doupdate=%s";
			urlParameters = String.format(urlParameters, testOpp.getSelectedSitting(), testOpp.getDoUpdate());
		} else if ("alter".equals(procedure)) {
			urlParameters += "&dayincrement=%s";
			urlParameters = String.format(urlParameters, testOpp.getDayIncrement());
		}
		return urlParameters;
	}

	/*
	 * public void addorRemoveOpportunity(TestOpportunity opp) { //
	 * TestOpportunity opp = null; if (this.selectedOpportunities.contains(opp))
	 * {
	 * this.selectedOpportunities.remove(this.selectedOpportunities.indexOf(opp)
	 * ); } else { this.selectedOpportunities.add(opp); } }
	 */

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
	}
}
