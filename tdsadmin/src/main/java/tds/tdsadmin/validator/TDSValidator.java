package tds.tdsadmin.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("tdsValidator")
public class TDSValidator implements Validator {

	private static final String INPUT_PATTERN = "[A-Za-z0-9\\-]+";

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
		} else if (elemId.equalsIgnoreCase("")) {
		}
	}

	private void validateSessionSSID(String elemId, String elemValue) {
		if (!matcher.matches() || elemValue.length() > 40) {
			String msg = "Input has to be alphanumeric";
			if (elemValue.length() > 40)
				msg = "Maximum input length is 40";
			FacesMessage fmsg = new FacesMessage(msg, msg);
			fmsg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(fmsg);
		}
	}

}
