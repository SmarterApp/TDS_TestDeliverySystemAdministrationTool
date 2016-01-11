package tds.tdsadmin.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import AIR.Common.DB.AbstractDAO;
import AIR.Common.DB.SQLConnection;
import AIR.Common.DB.results.DbResultRecord;
import AIR.Common.DB.results.SingleDataResultSet;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.dll.api.ITDSAdminDLL;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.TestOpportunity;

public class TDSAdminDAOImpl extends AbstractDAO implements TDSAdminDAO {

	@Autowired
	private ITDSAdminDLL _tdsAdminDLL = null;

	@Override
	public List<TestOpportunity> getOpportunities(String v_extSsId, String v_sessionId) throws ReturnStatusException {
		List<TestOpportunity> opportunities = new ArrayList<TestOpportunity>();

		try (SQLConnection connection = getSQLConnection()) {
			SingleDataResultSet results = _tdsAdminDLL.getOpportunities(connection, v_extSsId, v_sessionId);
			Iterator<DbResultRecord> records = results.getRecords();
			while (records.hasNext()) {
				DbResultRecord record = records.next();
				TestOpportunity opp = new TestOpportunity();

				opp.setAltSsid(record.<String> get("testeeid"));
				opp.setName(record.<String> get("testeename"));
				opp.setDateCompleted(record.<Date> get("datecompleted"));
				opp.setDateExpired(record.<Date> get("dateexpired"));
				opp.setDatePaused(record.<Date> get("datepaused"));
				opp.setDateStarted(record.<Date> get("datestarted"));
				opp.setSegmented(record.<Boolean> get("issegmented"));
				opp.setSessionId(record.<String> get("sessid"));
				opp.setStatus(record.<String> get("status"));
				opp.setTestName(record.<String> get("_efk_testid"));

				opportunities.add(opp);
			}
		} catch (SQLException e) {

			throw new ReturnStatusException(e);
		}
		return opportunities;
	}

	@Override
	public SingleDataResultSet resetOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDataResultSet invalidateTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDataResultSet restoreTestOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDataResultSet reopenOpportunity(UUID v_oppKey, String v_requestor, String v_reason)
			throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDataResultSet extendingOppGracePeriod(UUID v_oppKey, String v_requestor, int v_selectedsitting,
			boolean v_doupdate, String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDataResultSet alterOpportunityExpiration(UUID v_oppKey, String v_requestor, int v_dayincrement,
			String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDataResultSet setOpportunitySegmentPerm(UUID v_oppKey, String v_requestor, boolean v_ispermeable,
			String v_restoreon, String v_reason) throws ReturnStatusException {
		// TODO Auto-generated method stub
		return null;
	}

}
