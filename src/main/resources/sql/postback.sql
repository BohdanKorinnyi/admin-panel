SELECT
  postback.date,
  buyers.name,
  buyers.id as buyerId,
  TRUNCATE(sum(postback.sum /
               (SELECT exchange.rate
                FROM exchange
                WHERE exchange.id = postback.exchange)), 2)
    AS amountInUsd,
  postback.advname,
  postback.status
FROM postback
  LEFT JOIN affiliates ON postback.afid = affiliates.afid
  LEFT JOIN buyers ON affiliates.buyer_id = buyers.id
WHERE (postback.duplicate != 'FULL' OR postback.duplicate IS NULL)
      %s
      AND postback.afid <> 1
      %s
      AND
      (postback.status IN
       (SELECT adv_status.name
        FROM adv_status
        WHERE adv_status.real_status = 'approved')
       OR postback.status = ''
       OR postback.status IS NULL)
GROUP BY
  postback.date,
  affiliates.afname,
  postback.advname,
  postback.status;