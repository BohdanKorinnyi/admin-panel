SELECT
  count(*)      AS amount,
  sum(spent)    AS spent,
  source_statistics.afid,
  source_statistics.campaign_name,
  accounts.type AS accountType,
  accounts.username,
  buyers.id AS buyerId,
  buyers.name,
  source_statistics.date
FROM source_statistics
  LEFT JOIN accounts ON accounts.account_id = source_statistics.account_id
  LEFT JOIN affiliates ON affiliates.afid = source_statistics.afid
  LEFT JOIN buyers ON affiliates.buyer_id = buyers.id
WHERE source_statistics.spent != 0 %s
GROUP BY source_statistics.afid, source_statistics.date, source_statistics.campaign_id
ORDER BY source_statistics.date;