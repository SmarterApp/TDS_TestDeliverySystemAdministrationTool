package tds.tdsadmin.db.abstractions;

import java.util.List;
import java.util.UUID;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.model.TestOpportunity;

public interface TDSAdminDAO {
	public List<TestOpportunity> getOpportunities(String v_extSsId, String v_sessionId) throws ReturnStatusException;

	public SingleDataResultSet resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public SingleDataResultSet invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public SingleDataResultSet restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public SingleDataResultSet reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public SingleDataResultSet extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException;

	public SingleDataResultSet alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException;

	public SingleDataResultSet setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, boolean v_ispermeable,
			String v_restoreon, String v_reason) throws ReturnStatusException;
}
