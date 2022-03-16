SELECT * FROM account;
SELECT * FROM tenmo_user;
SELECT * FROM transfer;
SELECT * FROM transfer_status;
SELECT * FROM transfer_type;

CREATE VIEW transfer_with_desc
AS
SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.account_from, t.account_to, t.amount FROM transfer AS t
	INNER JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id
	INNER JOIN transfer_type AS tt ON t.transfer_type_id = tt.transfer_type_id;
	

SELECT balance FROM account WHERE user_id = 1001;

INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2, 2, 2001, 2002, 100.00);
INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2, 2, 2002, 2001, 20.00);

CREATE VIEW transfer_with_user_id
AS
SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, a.user_id AS user_from, account_to, ax.user_id AS user_to, amount FROM transfer as t
                JOIN account as a ON t.account_from = a.account_id 
				JOIN account as ax ON t.account_to = ax.account_id
				WHERE a.user_id = 1001 OR ax.user_id = 1001;
				
				
SELECT transfer_id, transfer_type_id, transfer_status_id, a.user_id AS user_from, ax.user_id AS user_to, amount FROM transfer_with_user_id
	WHERE a.user_id = 1001 OR ax.user_id = 1001;
	
SELECT * FROM transfer_with_user_id

SELECT t.transfer_id, t.transfer_type_id, tt.transfer_type_desc, t.transfer_status_id, ts.transfer_status_desc, t.account_from, a.user_id AS user_from, t.account_to, ax.user_id AS user_to, t.amount FROM transfer as t
                INNER JOIN account as a ON t.account_from = a.account_id 
				INNER JOIN account as ax ON t.account_to = ax.account_id
				INNER JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id
				INNER JOIN transfer_type AS tt ON t.transfer_type_id = tt.transfer_type_id
				WHERE a.user_id = 1001 OR ax.user_id = 1001;
