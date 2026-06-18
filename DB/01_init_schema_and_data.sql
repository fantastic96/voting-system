-- 設定連線編碼，避免中文亂碼
SET NAMES utf8mb4;
-- 建立投票項目表 (主表)
CREATE TABLE vote_item (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL
);

-- 建立投票紀錄表 (子表)
CREATE TABLE vote_record (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    voter_name VARCHAR(255) NOT NULL,
    item_id INT NOT NULL,
    vote_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES vote_item(item_id)
);

-- 寫入初始測試資料
INSERT INTO vote_item (item_id, item_name) VALUES (1, '電腦');
INSERT INTO vote_item (item_id, item_name) VALUES (2, '滑鼠');

INSERT INTO vote_record (voter_name, item_id) VALUES ('Leo', 1);
INSERT INTO vote_record (voter_name, item_id) VALUES ('Sandy', 1);
INSERT INTO vote_record (voter_name, item_id) VALUES ('Sandy', 2);
INSERT INTO vote_record (voter_name, item_id) VALUES ('Randy', 2);
INSERT INTO vote_record (voter_name, item_id) VALUES ('RSY', 2);