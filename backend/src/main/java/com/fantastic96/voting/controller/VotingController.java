package com.fantastic96.voting.controller;

import com.fantastic96.voting.dto.CreateItemRequest;
import com.fantastic96.voting.dto.VoteItemResponse;
import com.fantastic96.voting.dto.VoteRequest;
import com.fantastic96.voting.service.VotingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 投票系統的 API 控制層 (Controller)
 * 負責接收前端的 HTTP 請求，並將任務派發給 Service 層處理
 */
@CrossOrigin(origins = "http://localhost:5173") // 限制僅允許本機前端 Vue 專案進行跨網域請求，提升資安防護
@RestController
@RequestMapping("/api")
public class VotingController {

    private final VotingService votingService;

    // 使用建構子注入 (Constructor Injection) 依賴，為 Spring Boot 官方推薦的標準作法
    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    /**
     * 取得所有投票項目與目前的累計票數
     * HTTP Method: GET /api/items
     */
    @GetMapping("/items")
    public List<VoteItemResponse> getItems() {
        return votingService.getItems();
    }

    /**
     * 新增候選投票項目
     * HTTP Method: POST /api/items
     */
    @PostMapping("/items")
    public void addItem(@RequestBody CreateItemRequest request) {
        // 使用 Record 語法自動生成的 getter (request.itemName()) 取得資料
        votingService.addItem(request.itemName());
    }

    /**
     * 刪除特定投票項目
     * HTTP Method: DELETE /api/items/{id}
     */
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable int id) {
        votingService.deleteItem(id);
    }

    /**
     * ⭐ 處理使用者多選投票的請求
     * HTTP Method: POST /api/votes
     */
    @PostMapping("/votes")
    public void vote(@RequestBody VoteRequest request) {
        // 取得 Record 內的 voterName 與 itemIds 陣列，並交由 Service 處理批次投票
        votingService.vote(request.voterName(), request.itemIds());
    }
}