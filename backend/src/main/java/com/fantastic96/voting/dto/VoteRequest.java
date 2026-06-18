package com.fantastic96.voting.dto;

import java.util.List;

/**
 * 處理前端多選投票請求的 DTO (Data Transfer Object)
 * 使用 Java Record 大幅減少 Boilerplate code (樣板程式碼)
 */
public record VoteRequest(
    String voterName,      // 投票人姓名
    List<Integer> itemIds  // 勾選的投票項目 ID 列表
) {
}