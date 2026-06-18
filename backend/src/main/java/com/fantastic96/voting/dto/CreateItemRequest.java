package com.fantastic96.voting.dto;

/**
 * 接收新增投票項目請求的 DTO
 */
public record CreateItemRequest(
    String itemName   // 準備新增的項目名稱
) {
}