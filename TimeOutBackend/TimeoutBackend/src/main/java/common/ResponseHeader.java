package common;

public class ResponseHeader {
	private String type;
	private String message;
	private String cookie;
	
	public ResponseHeader(String cookie) {
		type = "Success";
		message = "It is successfully done.";
		this.cookie = cookie;
	}

	public ResponseHeader() {
		type = "Success";
		message = "It is successfully done.";
	}
	
	public ResponseHeader(boolean howToReturn) {
		if (!howToReturn){
			type = "Fail";
			message = "Specified information is wrong!";
		}else{
			type = "Success";
			message = "It is successfully done.";
		}
	}
	
	public ResponseHeader(boolean howToReturn, String message) {
		if (howToReturn)
			type = "Success";
		else
			type = "Fail";
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

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}
