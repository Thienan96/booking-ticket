package com.example.booking.dto.base;

import com.example.booking.base.constant.ReturnCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult <T> implements Serializable {
    private T data;
    private ReturnCodeEnum returnCode = ReturnCodeEnum.OK;
    private String exception;

    public ActionResult(ReturnCodeEnum returnCode) {
        this.returnCode = returnCode;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return ReturnCodeEnum.OK.equals(returnCode);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
