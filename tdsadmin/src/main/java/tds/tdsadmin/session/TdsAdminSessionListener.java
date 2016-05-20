/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TdsAdminSessionListener implements HttpSessionListener {
	private static final Logger _logger = LoggerFactory.getLogger(TdsAdminSessionListener.class);

	public void sessionCreated(HttpSessionEvent event) {
		PropertiesBean props = WebApplicationContextUtils
				.getWebApplicationContext(event.getSession().getServletContext()).getBean(PropertiesBean.class);
		int tdsadminTimeout = props.getTdsadminTimeout();
		event.getSession().setMaxInactiveInterval(tdsadminTimeout * 60);
		_logger.info(String.format("TDS Admin session timeout set as %s minutes.", tdsadminTimeout));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {

	}
}
