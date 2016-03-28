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

	@ManagedProperty(value = "#{userServiceImpl}")
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
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
		SbacUser user = (SbacUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}

}
