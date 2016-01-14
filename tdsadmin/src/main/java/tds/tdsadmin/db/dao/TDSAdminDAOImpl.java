package tds.tdsadmin.db.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import AIR.Common.DB.AbstractDAO;
import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.dll.api.ITDSAdminDLL;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.OpportunitySerializable;
import tds.tdsadmin.model.TestOpportunity;

/**
 * @author mkhan
 *
 */
public class TDSAdminDAOImpl extends AbstractDAO implements TDSAdminDAO {

	@Autowired
	private ITDSAdminDLL _tdsAdminDLL = null;

	@Override
	public OpportunitySerializable getOpportunities(String v_extSsId, String v_sessionId) throws ReturnStatusException {
		OpportunitySerializable opportunities = new OpportunitySerializable();

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.getOpportunities(connection, v_extSsId, v_sessionId);
			Iterator<DbResultRecord> records = results.getRecords();
			while (records.hasNext()) {
				DbResultRecord record = records.next();
				TestOpportunity opp = new TestOpportunity();
				if (record.hasColumn("testeeid"))
					opp.setAltSsid(record.<String> get("testeeid"));
				if (record.hasColumn("testeename"))
					opp.setName(record.<String> get("testeename"));
				if (record.hasColumn("datecompleted"))
					opp.setDateCompleted(record.<Date> get("datecompleted"));
				if (record.hasColumn("dateexpired"))
					opp.setDateExpired(record.<Date> get("dateexpired"));
				if (record.hasColumn("datepaused"))
					opp.setDatePaused(record.<Date> get("datepaused"));
				if (record.hasColumn("datestarted"))
					opp.setDateStarted(record.<Date> get("datestarted"));
				if (record.hasColumn("issegmented"))
					opp.setSegmented(record.<Boolean> get("issegmented"));
				if (record.hasColumn("sessid"))
					opp.setSessionId(record.<String> get("sessid"));
				if (record.hasColumn("status"))
					opp.setStatus(record.<String> get("status"));
				if (record.hasColumn("_efk_testid"))
					opp.setTestName(record.<String> get("_efk_testid"));
				if (record.hasColumn("insegment"))
					opp.setSegmentPosition(record.<Integer> get("insegment"));

				opportunities.add(opp);
			}
		} catch (SQLException e) {

			throw new ReturnStatusException(e);
		}
		return opportunities;
	}

	@Override
	public OpportunitySerializable resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpportunitySerializable invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpportunitySerializable restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpportunitySerializable reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpportunitySerializable extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpportunitySerializable alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpportunitySerializable setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, boolean v_ispermeable,
			String v_restoreon, String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

}
