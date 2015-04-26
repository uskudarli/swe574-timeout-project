package common;

public class ResponseHeader {
	private String type;
	private String message;
	
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

	
	
}
