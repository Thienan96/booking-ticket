package com.example.booking.base.constant;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum ReturnCodeEnum {

    OK("OK", 200, 20001, "OK"),
    SOMETHING_WRONG("SOMETHING_WRONG", 400, 20244, "Some thing wrong"),
    TICKET_NOT_EXIST("TICKET_NOT_EXIST", 400, 20244, "Ticket not exist"),
    TICKET_SOLD_OUT("TICKET_SOLD_OUT", 400, 20244, "Ticket sold out"),
    USER_DISABLED("USER_DISABLED", 401, 20244, "User disabled"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", 401, 20244, "UserName or password is incorrect"),
    TOKEN_INVALID("TOKEN_INVALID", 401, 20244, "Token invalid"),

    ;

    private static Map<Integer, ReturnCodeEnum> map = new HashMap<>();

    static {
        for (ReturnCodeEnum returnCode : ReturnCodeEnum.values()) {
            map.put(returnCode.code, returnCode);
        }
    }

    private int status;
    private int code;
    private String message;
    private String keyName;

    ReturnCodeEnum(String keyName, int status, int code, String message) {
        this.keyName = keyName;
        this.status = status;
        this.code = code;
        this.message = message;

    }

    public static ReturnCodeEnum valueOf(int code) {
        return map.get(code);
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(status);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getKeyName() {
        return keyName;
    }

    public static ReturnCodeEnum findByCode(int code) {
        return map.get(code);
    }

	public void setMessage(String message) {
		this.message = message;
	}

	public void setKeyName(String keyName) {this.keyName = keyName;}
}
