/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.db.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import AIR.Common.DB.AbstractDAO;
import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.dll.api.ITDSAdminDLL;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.ProcedureResult;
import tds.tdsadmin.model.TestOpportunity;

/**
 * @author mkhan
 *
 */
public class TDSAdminDAOImpl extends AbstractDAO implements TDSAdminDAO, Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(TDSAdminDAOImpl.class);

	@Autowired
	private ITDSAdminDLL _tdsAdminDLL = null;

	@Override
	public OpportunitySerializable getOpportunities(String v_extSsId, String v_sessionId, String v_procedure)
			throws ReturnStatusException {
		OpportunitySerializable opportunities = new OpportunitySerializable();

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.getOpportunities(connection, v_extSsId, v_sessionId,
					v_procedure);
			Iterator<DbResultRecord> records = results.getRecords();
			while (records.hasNext()) {
				DbResultRecord record = records.next();
				TestOpportunity opp = new TestOpportunity();

				if (record.hasColumn("_key"))
					opp.setOppKey(record.<UUID> get("_key"));
				if (record.hasColumn("testeeid"))
					opp.setAltSsid(record.<String> get("testeeid"));
				if (record.hasColumn("testeename"))
					opp.setName(record.<String> get("testeename"));
				if (record.hasColumn("_efk_testid"))
					opp.setTestName(record.<String> get("_efk_testid"));
				if (record.hasColumn("subject"))
					opp.setSubject(record.<String> get("subject"));
				if (record.hasColumn("datecompleted"))
					opp.setDateCompleted(record.<Date> get("datecompleted"));
				if (record.hasColumn("dateexpired"))
					opp.setDateExpired(record.<Date> get("dateexpired"));
				if (record.hasColumn("datepaused"))
					opp.setDatePaused(record.<Date> get("datepaused"));
				if (record.hasColumn("datestarted"))
					opp.setDateStarted(record.<Date> get("datestarted"));
				if (record.hasColumn("sessid"))
					opp.setSessionId(record.<String> get("sessid"));
				if (record.hasColumn("status"))
					opp.setStatus(record.<String> get("status"));
				if (record.hasColumn("_efk_testid"))
					opp.setTestName(record.<String> get("_efk_testid"));
				if (record.hasColumn("segmentid"))
					opp.setSegmentName(record.<String> get("segmentid"));
				if (record.hasColumn("restart"))
					opp.setRestart(record.<Integer> get("restart"));
				if (record.hasColumn("restorepermon"))
					opp.setRestoreOn(record.<String> get("restorepermon"));
				if (StringUtils.isEmpty(opp.getRestoreOn()))
					opp.setRestoreOn("segment");
				if (record.hasColumn("ispermeable"))
					opp.setIspermeable(record.<Integer> get("ispermeable"));
				if (record.hasColumn("segmentposition"))
					opp.setSegmentPosition(record.<Integer> get("segmentposition"));
				opp.setSelected(false);

				opportunities.add(opp);
			}
			_logger.info(String.format("Get opportunity successful for External ssid=%s, SessionId=%s, Procedure=%s.",
					v_extSsId, v_sessionId, v_procedure));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return opportunities;
	}

	@Override
	public ProcedureResult resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {

		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_resettestopportunity_SP(connection, v_oppKey, v_requestor,
					v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format("Reset successful for: Oppkey=%s, Requestor=%s, Reason=%s.", v_oppKey,
					v_requestor, v_reason));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;
	}

	@Override
	public ProcedureResult invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {

		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_invalidatetestopportunity_SP(connection, v_oppKey, v_requestor,
					v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format("Invalidate successful for: Oppkey=%s, Requestor=%s, Reason=%s.", v_oppKey,
					v_requestor, v_reason));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;

	}

	@Override
	public ProcedureResult restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {

		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_restoretestopportunity_SP(connection, v_oppKey, v_requestor,
					v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format("Restore successful for: Oppkey=%s, Requestor=%s, Reason=%s.", v_oppKey,
					v_requestor, v_reason));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;
	}

	@Override
	public ProcedureResult reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {

		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_reopenopportunity_SP(connection, v_oppKey, v_requestor,
					v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format("Reopen successful for: Oppkey=%s, Requestor=%s, Reason=%s.", v_oppKey,
					v_requestor, v_reason));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;
	}

	@Override
	public ProcedureResult extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException {

		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_extendingoppgraceperiod_SP(connection, v_oppKey, v_requestor,
					v_selectedsitting, v_doupdate, v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format(
					"Extending grace period successful for: Oppkey=%s, Requestor=%s, Reason=%s, SelectedSitting=%s, Doupdate=%s.",
					v_oppKey, v_requestor, v_reason, v_selectedsitting, v_doupdate));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;
	}

	@Override
	public ProcedureResult alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException {

		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_alteropportunityexpiration_SP(connection, v_oppKey,
					v_requestor, v_dayincrement, v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format(
					"Alter expiration successful for: Oppkey=%s, Requestor=%s, Reason=%s, DayIncrement=%s.", v_oppKey,
					v_requestor, v_reason, v_dayincrement));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;
	}

	@Override
	public ProcedureResult setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, String v_segmentid,
			int v_segmentposition, String v_restoreon, int v_ispermeable, String v_reason)
					throws ReturnStatusException {
		ProcedureResult result = null;

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.A_setopportunitysegmentperm_SP(connection, v_oppKey, v_requestor,
					v_segmentid, v_segmentposition, v_restoreon, v_ispermeable, v_reason);
			Iterator<DbResultRecord> records = results.getRecords();
			if (records.hasNext()) {
				DbResultRecord record = records.next();
				result = new ProcedureResult();
				if (record.hasColumn("status"))
					result.setStatus(record.<String> get("status"));
				if (record.hasColumn("reason"))
					result.setReason(record.<String> get("reason"));
				if (record.hasColumn("context"))
					result.setContext(record.<String> get("context"));
				if (record.hasColumn("appkey"))
					result.setAppKey(record.<String> get("appkey"));
			}
			_logger.info(String.format(
					"Set segment permeability successful for: Oppkey=%s, Requestor=%s, Reason=%s, SegmentId=%s, SegmentPosition=%s, RestoreOn=%s, Ispermeable=%s.",
					v_oppKey, v_requestor, v_reason, v_segmentid, v_segmentposition, v_restoreon, v_ispermeable));
		} catch (SQLException e) {
			_logger.error(e.getMessage(), e);
			throw new ReturnStatusException(e);
		}
		return result;
	}

}
