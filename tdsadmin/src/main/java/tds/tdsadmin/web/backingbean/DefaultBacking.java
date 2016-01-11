package tds.tdsadmin.web.backingbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class DefaultBacking {
	private String radiossid = null;
	private String extssid = null;
	private String sessionid = null;
	private List<String> procedures = null;

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

	public void show() {
		setSessionid(radiossid);
		System.out.println("click is working");
	}

	public List<String> getProcedures() {
		return procedures;
	}

	public void setProcedures(List<String> procedures) {
		this.procedures = procedures;
	}
}
