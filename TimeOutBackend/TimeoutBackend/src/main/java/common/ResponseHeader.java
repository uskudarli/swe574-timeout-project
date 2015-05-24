package common;

public class ResponseHeader {
	private String type;
	private String code;
	private String message;
	private String sessionId;

	public ResponseHeader(String sessionId) {
		type = "Success";
		code = "S100";
		message = "It is successfully done.";
		this.sessionId = sessionId;
	}

	public ResponseHeader() {
		type = "Success";
		code = "S100";
		message = "It is successfully done.";
	}

	public ResponseHeader(boolean howToReturn) {
		if (!howToReturn) {
			type = "Fail";
			code = "E100";
			message = "Specified information is wrong!";
		} else {
			type = "Success";
			code = "S100";
			message = "It is successfully done.";
		}
	}

	public ResponseHeader(boolean howToReturn, String message) {
		if (howToReturn)
			type = "Success";
		else
			type = "Fail";
		this.code = "E100";
		this.message = message;
	}

	public ResponseHeader(boolean howToReturn, String code, String message) {
		if (howToReturn)
			type = "Success";
		else
			type = "Fail";
		this.code = code;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
