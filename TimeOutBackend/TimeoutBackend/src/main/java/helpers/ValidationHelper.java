package helpers;

import common.BusinessException;
import common.ErrorMessages;

public class ValidationHelper {

	public static void validateEmail(String email) throws BusinessException {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		if (!m.matches()){
			ServiceHelper.businessError(ErrorMessages.emailValidateCode, ErrorMessages.emailValidate);
		}
	}

	public static void validatePassword(String password) throws BusinessException {
		if (password.isEmpty() || password == null)
			ServiceHelper.businessError(ErrorMessages.emailValidateCode, ErrorMessages.emailValidate);
		
		if (password.length() <= 4)
			ServiceHelper.businessError(ErrorMessages.emailValidateCode, ErrorMessages.emailValidate);
	}
	
	public static boolean isNullOrEmpty(String s) {
	    return s == null || s.length() == 0;
	}

	public static boolean isNullOrWhitespace(String s) {
	    return s == null || isWhitespace(s);

	}
	private static boolean isWhitespace(String s) {
	    int length = s.length();
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	            if (!Character.isWhitespace(s.charAt(i))) {
	                return false;
	            }
	        }
	        return true;
	    }
	    return false;
	}
}
