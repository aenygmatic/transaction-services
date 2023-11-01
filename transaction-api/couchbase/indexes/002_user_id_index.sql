CREATE INDEX `user_id_on_transactions`
ON `transactions` (userId, _class)
WHERE _class = "com.virtualbank.transaction.service.model.Transaction"
USING GSI