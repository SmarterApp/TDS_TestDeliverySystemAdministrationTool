package tds.tdsadmin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TestOpportunity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID oppKey;
	private String altSsid;
	private String name;
	private String testName;
	private String sessionId;
	private String status;
	private Date dateStarted;
	private Date dateExpired;
	private Date dateCompleted;
	private Date datePaused;
	private String segmentName;
	private int restart;
	private String result;
	private String restoreOn;
	private int segmentPosition;
	private int ispermeable;
	private boolean permeable;
	private String reason;
	private boolean selected;

	public UUID getOppKey() {
		return oppKey;
	}

	public void setOppKey(UUID oppKey) {
		this.oppKey = oppKey;
	}

	public String getAltSsid() {
		return altSsid;
	}

	public void setAltSsid(String altSsid) {
		this.altSsid = altSsid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public Date getDateExpired() {
		return dateExpired;
	}

	public void setDateExpired(Date dateExpired) {
		this.dateExpired = dateExpired;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public Date getDatePaused() {
		return datePaused;
	}

	public void setDatePaused(Date datePaused) {
		this.datePaused = datePaused;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public int getRestart() {
		return restart;
	}

	public void setRestart(int restart) {
		this.restart = restart;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRestoreOn() {
		return restoreOn;
	}

	public void setRestoreOn(String restoreOn) {
		this.restoreOn = restoreOn;
	}

	public int getSegmentPosition() {
		return segmentPosition;
	}

	public void setSegmentPosition(int segmentPosition) {
		this.segmentPosition = segmentPosition;
	}

	public int isIspermeable() {
		ispermeable = (permeable == true) ? 1 : -1;
		return ispermeable;
	}

	public void setIspermeable(int ispermeable) {
		this.ispermeable = ispermeable;
		permeable = (ispermeable > 0) ? true : false;
	}

	public boolean isPermeable() {
		return permeable;
	}

	public void setPermeable(boolean permeable) {
		this.permeable = permeable;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
