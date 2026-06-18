package com.fantastic96.voting.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全域例外處理器 (Global Exception Handler)
 * 作為系統的「公關部」，負責集中攔截應用程式拋出的例外狀況，
 * 並將其轉換為語意清晰、格式統一的 HTTP 回應給前端，避免伺服器直接暴露底層的 500 錯誤。
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * 攔截無效參數例外 (IllegalArgumentException)
     * 當 InputValidator 或 Service 層防呆驗證失敗並拋出此例外時，將由本方法接手處理。
     * * @param exception 捕獲到的無效參數例外物件
     * @return 回傳 HTTP 400 (Bad Request) 狀態碼，並附帶 JSON 格式的錯誤提示訊息
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                // 將錯誤訊息包裝成乾淨的 JSON 格式，例如：{"message": "voterName is required"}
                .body(Map.of("message", exception.getMessage()));
    }
}