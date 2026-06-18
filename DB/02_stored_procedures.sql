DELIMITER //

-- SP: 取得所有投票項目與目前累積票數
CREATE PROCEDURE sp_get_items_with_votes()
BEGIN
    SELECT 
        i.item_id, 
        i.item_name, 
        COUNT(r.record_id) AS vote_count
    FROM vote_item i
    LEFT JOIN vote_record r ON i.item_id = r.item_id
    GROUP BY i.item_id, i.item_name
    ORDER BY i.item_id ASC;
END //

-- SP: 新增投票項目
CREATE PROCEDURE sp_add_item(IN p_item_name VARCHAR(255))
BEGIN
    INSERT INTO vote_item (item_name) VALUES (p_item_name);
END //

-- SP: 刪除投票項目 (包含 Transaction 示範)
CREATE PROCEDURE sp_delete_item(IN p_item_id INT)
BEGIN
    -- 宣告發生 SQL 例外時，自動 Rollback
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;
        -- 1. 先刪除子表(投票紀錄)中的相關資料
        DELETE FROM vote_record WHERE item_id = p_item_id;
        
        -- 2. 再刪除主表(投票項目)中的資料
        DELETE FROM vote_item WHERE item_id = p_item_id;
    COMMIT;
END //

-- SP: 新增投票紀錄
CREATE PROCEDURE sp_add_vote(IN p_voter_name VARCHAR(255), IN p_item_id INT)
BEGIN
    INSERT INTO vote_record (voter_name, item_id) VALUES (p_voter_name, p_item_id);
END //

DELIMITER ;
