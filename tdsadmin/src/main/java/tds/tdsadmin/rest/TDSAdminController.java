package tds.tdsadmin.rest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import TDS.Shared.Exceptions.ReturnStatusException;
import tds.tdsadmin.db.abstractions.TDSAdminDAO;
import tds.tdsadmin.model.TestOpportunity;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TDSAdminController {

	private static final Logger logger = LoggerFactory.getLogger(TDSAdminController.class);

	@Autowired
	TDSAdminDAO _dao = null;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/rest/getOpportunities", method = RequestMethod.GET)
	@ResponseBody
	public List<TestOpportunity> getOpportunities(@RequestParam(value = "extSsId", required = false) String extSsId,
			@RequestParam(value = "ssId", required = false) String ssId,
			@RequestParam(value = "sessionId", required = false) String sessionId) {

		List<TestOpportunity> results = new ArrayList<TestOpportunity>();
		try {
			results = _dao.getOpportunities(extSsId, sessionId);
		} catch (ReturnStatusException e) {
			logger.error(e.getMessage());
		}

		return results;
	}

}
