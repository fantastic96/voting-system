package com.fantastic96.voting.repository;

import com.fantastic96.voting.dto.VoteItemResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投票系統的資料存取層 (DAO / Repository)
 * ⭐ 全面採用 Stored Procedure (預存程序) 進行資料庫操作
 * 確保高併發環境下的效能與資料一致性，並將商業邏輯與底層資料庫完美解耦
 */
@Repository
public class VotingRepository {

    // 注入 Spring JDBC 工具，用於執行參數化 SQL
    private final JdbcTemplate jdbcTemplate;

    public VotingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 獲取所有投票項目及累計票數
     * 呼叫 SP: sp_get_items_with_votes()
     * @return 包含 VoteItemResponse 的列表
     */
    public List<VoteItemResponse> findItemsWithVotes() {
        return jdbcTemplate.query(
                "CALL sp_get_items_with_votes()",
                // 使用 Lambda 實作 RowMapper，將資料庫 ResultSet 映射為 Java Record
                (rs, rowNum) -> new VoteItemResponse(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getLong("vote_count")
                )
        );
    }

    /**
     * 新增投票項目
     * 呼叫 SP: sp_add_item(?)
     */
    public void addItem(String itemName) {
        jdbcTemplate.update("CALL sp_add_item(?)", itemName);
    }

    /**
     * 刪除特定投票項目
     * 呼叫 SP: sp_delete_item(?)，由資料庫層處理相關投票紀錄的連動刪除
     */
    public void deleteItem(int itemId) {
        jdbcTemplate.update("CALL sp_delete_item(?)", itemId);
    }

    /**
     * 新增一筆投票紀錄
     * 呼叫 SP: sp_add_vote(?, ?)，由資料庫層確保關聯完整性與防呆
     */
    public void addVote(String voterName, int itemId) {
        jdbcTemplate.update("CALL sp_add_vote(?, ?)", voterName, itemId);
    }
}