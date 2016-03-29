package tds.tdsadmin.db.abstractions;

import java.util.UUID;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.ProcedureResult;

public interface TDSAdminDAO {
	public OpportunitySerializable getOpportunities(String v_extSsId, String v_sessionId, String v_procedure)
			throws ReturnStatusException;

	public ProcedureResult resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public ProcedureResult invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public ProcedureResult restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public ProcedureResult reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException;

	public ProcedureResult extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException;

	public ProcedureResult alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException;

	public ProcedureResult setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, String v_segmentid,
			int v_segmentposition, String v_restoreon, int v_ispermeable, String v_reason) throws ReturnStatusException;
}
