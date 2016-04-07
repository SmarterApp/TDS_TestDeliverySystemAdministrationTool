/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.web.backingbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.opentestsystem.shared.security.domain.SbacUser;
import org.opentestsystem.shared.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean
@SessionScoped
public class UserBean {

	private static final Logger _logger = LoggerFactory.getLogger(UserBean.class);

	private SbacUser user;

	public UserBean() {
		user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		_logger.info(String.format("Logged in user is:%s", user.getEmail()));
	}

	public String getFullName() {
		try {
			SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return user.getFullName();
		} catch (Exception e) {
			_logger.error(e.toString(), e);
		}
		return "Unable to retrieve Name";
	}

	public boolean hasPermission() {
		SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.hasPermission("Opportunity Read") || user.hasPermission("Opportunity Modify"))
			return true;
		return false;
	}

	public SbacUser getUser() {
		if (user == null)
			user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}

}
