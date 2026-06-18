package com.fantastic96.voting.service;

import com.fantastic96.voting.common.InputValidator;
import com.fantastic96.voting.dto.VoteItemResponse;
import com.fantastic96.voting.repository.VotingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 投票系統的業務邏輯層 (Service)
 * 負責處理核心商業邏輯、資料防呆驗證，以及控制資料庫交易 (Transaction)
 */
@Service
public class VotingService {

    private final VotingRepository votingRepository;
    private final InputValidator inputValidator;

    // 透過建構子注入 Repository 與 Validator，方便後續進行單元測試 (Unit Test)
    public VotingService(VotingRepository votingRepository, InputValidator inputValidator) {
        this.votingRepository = votingRepository;
        this.inputValidator = inputValidator;
    }

    /**
     * 獲取所有投票項目及目前的累計票數
     */
    public List<VoteItemResponse> getItems() {
        return votingRepository.findItemsWithVotes();
    }

    /**
     * 新增投票項目
     * @param itemName 傳入的項目名稱
     */
    public void addItem(String itemName) {
        // 先進行防呆驗證，確保 itemName 不是 null 或全空白，再寫入資料庫
        votingRepository.addItem(inputValidator.requireText(itemName, "itemName"));
    }

    /**
     * 刪除特定投票項目
     * @param itemId 欲刪除的項目 ID
     */
    public void deleteItem(int itemId) {
        votingRepository.deleteItem(itemId);
    }

    /**
     * ⭐ 處理使用者多選投票的批次業務邏輯
     * @param voterName 投票人姓名
     * @param itemIds 被勾選的投票項目 ID 列表
     */
    @Transactional // 確保多筆投票具有 ACID 特性：若其中一筆發生例外，將全部 Rollback 避免資料不一致
    public void vote(String voterName, List<Integer> itemIds) {
        // 1. 驗證並清理投票人名稱 (例如去除頭尾空白)
        String normalizedVoterName = inputValidator.requireText(voterName, "voterName");
        
        // 2. 防呆：確保前端有傳入投票項目
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("itemIds is required");
        }

        // 3. 使用 Java 8 Stream API 進行處理
        itemIds.stream()
                .distinct() // 過濾掉陣列中重複的 ID，防止同一使用者在單次請求中對同項目重複灌票
                .forEach(itemId -> votingRepository.addVote(normalizedVoterName, itemId));
    }
}