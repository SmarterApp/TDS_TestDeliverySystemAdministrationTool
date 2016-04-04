/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tds.tdsadmin.rest.TDSAdminController;

@FacesValidator("tdsValidator")
public class TDSValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(TDSValidator.class);

	private static final String INPUT_PATTERN = "[A-Za-z0-9\\-]*";

	private Pattern pattern;
	private Matcher matcher;

	public TDSValidator() {
		pattern = Pattern.compile(INPUT_PATTERN);
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String elemId = component.getId();
		String elemValue = value.toString();
		matcher = pattern.matcher(elemValue);
		if (elemId.equalsIgnoreCase("essid") || elemId.equalsIgnoreCase("session")) {
			validateSessionSSID(elemId, elemValue);
		} else if (elemId.contains("dayincrement")) {
			validateDayIncrement(elemId, elemValue);
		}
	}

	private void validateSessionSSID(String elemId, String elemValue) {
		if (!matcher.matches() || elemValue.length() > 40) {
			String msg = "Input has to be alphanumeric";
			if (elemValue.length() > 40)
				msg = "Maximum input length is 40";
		}
	}

	private void validateDayIncrement(String elemId, String elemValue) {
		int dayincrement = Integer.parseInt(elemValue);
		if (dayincrement < 365 || dayincrement > 365) {
			String msg = "Value has to be between -365 and 365";
			throwException(elemId, msg);
		}
	}

	private void throwException(String elemId, String msg) {
		FacesMessage fmsg = new FacesMessage(msg, msg);
		fmsg.setSeverity(FacesMessage.SEVERITY_ERROR);
		logger.error(String.format(msg + " for element:%s", elemId));
		throw new ValidatorException(fmsg);
	}

}
