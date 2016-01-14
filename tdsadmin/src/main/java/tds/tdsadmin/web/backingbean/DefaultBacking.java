package tds.tdsadmin.web.backingbean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.TestOpportunity;

@ManagedBean
@RequestScoped
public class DefaultBacking {
	private String radiossid = null;
	private String extssid = null;
	private String sessionid = null;
	private List<String> procedures = null;
	private List<TestOpportunity> opportunities = null;

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

	public List<String> getProcedures() {
		return procedures;
	}

	public void setProcedures(List<String> procedures) {
		this.procedures = procedures;
	}

	public List<TestOpportunity> getOpportunities() {
		return opportunities;
	}

	public void setOpportunities(List<TestOpportunity> opportunities) {
		this.opportunities = opportunities;
	}

	public boolean searchOpportunity(String extSsId, String sessionId) {
		if (StringUtils.isEmpty(extSsId) && StringUtils.isEmpty(sessionId))
			return false;
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
		String url = path + "/rest/getOpportunities?extSsId=%s&sessionId=%s";
		url = String.format(url, extSsId, sessionId);
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
				System.out.print(sb.toString());
			}

		} catch (IOException e) {
		} finally {
			connection.disconnect();
		}
		return true;
	}
}
