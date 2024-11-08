package com.example.booking.dto.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class ResponseBuilder {

    private String clientVersion;

    // @Autowired
    // private RedisService redisService;

    private HttpStatus getHttpStatus(Response response) {
        return HttpStatus.valueOf(response.getStatus());
    }

    private Map<String, Object> addExtraInfo() {
        // clientVersion = redisService.get(CoreConst.CLIENT_VERSION) == null? "0" : redisService.get(CoreConst.CLIENT_VERSION).toString();
        Map<String, Object> extra = new HashMap<String, Object>();
        extra.put("clientVersion", clientVersion);
        return extra;
    }

    public ResponseEntity build(ActionResult result) {
        Response response = new Response(result);
        response.setExtra(addExtraInfo());
        return new ResponseEntity(response, getHttpStatus(response));
    }

    // public ResponseEntity buildPartner(final ReturnCodeEnum returnCode, Object result) {
    //     OldResponse response = new OldResponse(returnCode, result);
    //     return new ResponseEntity(response, HttpStatus.valueOf(returnCode.getStatus()));
    // }

    // public ResponseEntity buildExportResponse(final ExportTypeEnum exportType, final ExportResult result) {
    //     Response response = new Response(result.getReturnCode());
    //     HttpHeaders httpHeaders = new HttpHeaders();
    //     httpHeaders.set(HttpHeaders.CONTENT_LOCATION, result.getReturnCode().getMessage());
    //     httpHeaders.set(HttpHeaders.CONTENT_TYPE, exportType.getMediaType());
    //     httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFileName() + "\"");
    //     return new ResponseEntity(result.getExportData(), httpHeaders, getHttpStatus(response));
    // }
    
    // public ResponseEntity buildExportFileResponse(final ExportResult result) {
    //     Response response = new Response(result.getReturnCode());
    //     HttpHeaders httpHeaders = new HttpHeaders();
    //     httpHeaders.set(HttpHeaders.CONTENT_LOCATION, result.getReturnCode().getMessage());
    //     httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
    //     httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFileName() + "\"");
    //     return new ResponseEntity(result.getExportData(), httpHeaders, getHttpStatus(response));
    // }

}
