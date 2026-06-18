package com.fantastic96.voting.dto;

/**
 * 回傳給前端的投票項目與目前票數 DTO
 */
public record VoteItemResponse(
    int itemId,       // 項目 ID
    String itemName,  // 項目名稱
    long voteCount    // 目前累計票數
) {
}