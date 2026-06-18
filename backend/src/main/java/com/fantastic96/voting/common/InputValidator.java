package com.fantastic96.voting.common;

import org.springframework.stereotype.Component;

/**
 * 系統共用的輸入驗證工具 (Input Validator)
 * 作為防呆機制，集中處理所有參數的基礎驗證邏輯，確保進入 Service 層的資料都是合法且乾淨的。
 */
@Component
public class InputValidator {

    /**
     * 驗證字串是否為空，並自動過濾前後空白
     * * @param value 欲驗證的字串
     * @param fieldName 欄位名稱 (用於拋出錯誤時的提示訊息)
     * @return 去除前後空白後的乾淨字串
     * @throws IllegalArgumentException 若字串為 null 或全為空白，將拋出此例外阻斷執行
     */
    public String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }
}