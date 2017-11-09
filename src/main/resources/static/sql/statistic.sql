SELECT
  count(*)      AS amount,
  sum(spent)    AS spent,
  source_statistics.buyer_id,
  source_statistics.campaign_name,
  accounts.type AS accountType,
  accounts.username,
  buyers.name,
  source_statistics.date
FROM source_statistics
  LEFT JOIN accounts ON accounts.account_id = source_statistics.account_id
  LEFT JOIN buyers ON source_statistics.buyer_id = buyers.id
WHERE source_statistics.spent != 0 % s
GROUP BY source_statistics.buyer_id, source_statistics.date, source_statistics.campaign_id
ORDER BY source_statistics.date;