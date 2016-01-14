package tds.tdsadmin.db.abstractions;

import java.util.UUID;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.model.OpportunitySerializable;

public interface TDSAdminDAO {
	public OpportunitySerializable getOpportunities(String v_extSsId, String v_sessionId) throws ReturnStatusException;

	public OpportunitySerializable resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public OpportunitySerializable invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public OpportunitySerializable restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public OpportunitySerializable reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public OpportunitySerializable extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException;

	public OpportunitySerializable alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException;

	public OpportunitySerializable setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, boolean v_ispermeable,
			String v_restoreon, String v_reason) throws ReturnStatusException;
}
