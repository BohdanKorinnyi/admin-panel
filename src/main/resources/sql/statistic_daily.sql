SELECT
  count(*)      AS amount,
  sum(spent)    AS spent,
  source_statistics_today.campaign_name,
  accounts.type AS accountType,
  accounts.username,
  buyers.name,
  buyers.id     AS buyerId,
  source_statistics_today.date
FROM source_statistics_today
  LEFT JOIN accounts ON accounts.account_id = source_statistics_today.account_id
  LEFT JOIN buyers ON accounts.buyer_id = buyers.id
WHERE spent != 0 %s
GROUP BY buyers.id, source_statistics_today.date, source_statistics_today.campaign_id
ORDER BY date;
