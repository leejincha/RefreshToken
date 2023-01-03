package com.example.namoldak.util.GlobalResponse;

import com.example.namoldak.util.GlobalResponse.code.ErrorCode;
import com.example.namoldak.util.GlobalResponse.code.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// 1. 기능 : 최종 응답이 리턴
// 2. 작성자 : 조소영
public class ResponseUtil {

    // 성공 응답 (No Data)
    public static ResponseEntity<?> successResponse(SuccessCode successCode){
        return new ResponseEntity<>(new GlobalResponseDto(successCode), HttpStatus.OK);
    }

    // 성공 응답 (Data) - 오로지 객체값만 반환
    public static <T> ResponseEntity<?> successResponse(T Data){
        return new ResponseEntity<>(Data, HttpStatus.OK);
    }

    // 에러 응답
    public static ResponseEntity<?> errorResponse(ErrorCode errorCode){
        return new ResponseEntity<>(new GlobalResponseDto(errorCode), errorCode.getHttpStatus());
    }
}
