/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.web.backingbean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.opentestsystem.shared.progman.client.domain.Tenant;
import org.opentestsystem.shared.security.domain.SbacRole;
import org.opentestsystem.shared.security.domain.SbacUser;
import org.opentestsystem.shared.security.service.UserService;
import org.opentestsystem.shared.security.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;

import AIR.Common.Utilities.SpringApplicationContext;

@ManagedBean
@SessionScoped
public class UserBean {

	private static final Logger _logger = LoggerFactory.getLogger(UserBean.class);

	@ManagedProperty(value = "#{userServiceImpl}")
	private UserService userService;
	private String logoImageURL;
	private String logoImageTitle;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getLogoImageTitle() {
		if (logoImageTitle == null) {
			populateLogoImageDetails();
		}
		return logoImageTitle;
	}

	public String getLogoImageURL() {
		if (logoImageURL == null) {
			populateLogoImageDetails();
		}
		return logoImageURL;
	}

	public Map<String, Object> getAssets(String tenantId) {
		return getUserService().getAssetsForTenant(tenantId);
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

	@SuppressWarnings("unchecked")
	public void populateLogoImageDetails() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		// Path for the default SBAC Image
		logoImageURL = request.getContextPath() + "/images/logo_sbac.png";
		logoImageTitle = "Smarter Balanced Assessment Consortium";
		try {
			String tenantId = "";
			List<Tenant> tenantsList = getUserService().getUniqueTenantsForUser().getTenants();
			// If Only one tenant associated than get the image from progman and
			// display it else display the default sbac image.
			if (tenantsList != null && tenantsList.size() == 1) {
				Tenant tenant = tenantsList.get(0);
				tenantId = tenant.getId();

				Map<String, Object> assets = getUserService().getAssetsForTenant(tenantId);
				if (assets != null && !assets.isEmpty()) {
					List<Map<String, String>> assetsList = (List<Map<String, String>>) assets.get("assets");
					for (Map<String, String> asset : assetsList) {
						if (asset.get("name").equals("logo")) {
							logoImageURL = asset.get("url");
						} else if (asset.get("name").equalsIgnoreCase("title")) {
							logoImageTitle = asset.get("url");
						}
					}
				}
			}
		} catch (Exception e) {
			_logger.error(e.toString(), e);
		}
	}

	public String getRole() {

		try {
			for (Tenant tenant : getUserService().getUniqueTenantsForUser().getTenants()) {
				System.out.println(tenant.getName());
				System.out.println(tenant.getType().getTypeName());
			}
			SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Collection<SbacRole> sbacRoles = user.getRoles();
			StringBuilder roleNames = new StringBuilder("");
			for (SbacRole role : sbacRoles) {
				roleNames.append(role.getRoleName()).append(", ");
			}
			roleNames.replace(roleNames.length() - 2, roleNames.length() - 1, "");
			return roleNames.toString();
		} catch (Exception e) {
			_logger.error(e.toString(), e);
		}

		return "Administrator";
	}

	public boolean hasPermission() {
		SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.hasPermission("Opportunity Read") || user.hasPermission("Opportunity Modify"))
			return true;
		return false;
	}

	public SbacUser getUser() {
		SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}

}
