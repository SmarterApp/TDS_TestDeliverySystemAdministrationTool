/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
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
