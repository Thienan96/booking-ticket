package com.example.booking.dto.base;

import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.booking.base.constant.ReturnCodeEnum;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author VietMX
 */

@Data
public class Response implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
    private final int code;
    private final String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis()));
    private String message;
    private String messageEn;
    private String exception;
    private Object data;
    private Map<String, Object> extra;
    // Support multi language
    private String keyName;

    public Response(final ReturnCodeEnum returnCode) {
        this.keyName = returnCode.getKeyName();
        this.status = HttpStatus.valueOf(returnCode.getStatus());
        this.code = returnCode.getCode();
        String[]smgArr = returnCode.getMessage().split("\\|");
        this.message = smgArr[0];
        if(smgArr.length > 1) {
        	this.messageEn = smgArr[1];
        }else {
        	this.messageEn = smgArr[0];
		}
    }

    public Response(ActionResult result) {
        this.keyName = result.getReturnCode().getKeyName();
        this.status = HttpStatus.valueOf(result.getReturnCode().getStatus());
        this.code = result.getReturnCode().getCode();
        String[]smgArr = result.getReturnCode().getMessage().split("\\|");
        this.message = smgArr[0];
        if(smgArr.length > 1) {
        	this.messageEn = smgArr[1];
        }else {
        	this.messageEn = smgArr[0];
		}
        this.data = result.getData();
        this.exception = "";
    }

    public int getStatus() {
        return status.value();
    }

    public Response setResponse(final Object data) {
        this.data = data;
        return this;
    }

    public ResponseEntity get() {
        return new ResponseEntity(this, status);
    }
}