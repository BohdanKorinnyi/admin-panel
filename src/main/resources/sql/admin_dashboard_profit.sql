SELECT
  truncate(sum(details.spent),2)                         AS 'spent',
  truncate(sum(details.revenue),2)                        AS 'revenue',
  details.buyer                               AS 'buyer',
  details.buyerId                             AS 'buyerId',
  (sum(details.revenue) - sum(details.spent)) AS 'profit'
FROM (SELECT
        sum(expenses.sum) AS 'spent',
        0                              AS 'revenue',
        buyers.id                      AS 'buyerId',
        buyers.name                    AS 'buyer'
      FROM expenses
        INNER JOIN buyers ON expenses.buyer_id = buyers.id
        INNER JOIN expenses_type ON expenses.type_id = expenses_type.id
      WHERE expenses.sum != 0 %s
      GROUP BY buyers.id, expenses.date
      UNION (SELECT
               sum(source_statistics.spent) AS 'spent',
               0                                         AS 'revenue',
               buyers.id                                 AS 'buyerId',
               buyers.name                               AS 'buyer'
             FROM source_statistics
               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid
               INNER JOIN buyers ON affiliates.buyer_id = buyers.id
               INNER JOIN accounts ON accounts.account_id = source_statistics.account_id
             WHERE source_statistics.spent != 0 %s
             GROUP BY buyers.id, source_statistics.date, accounts.type)
      UNION (
        SELECT
          sum(source_statistics_today.spent) AS 'spent',
          0                                               AS 'revenue',
          buyers.id                                       AS 'buyerId',
          buyers.name                                     AS 'buyer'
        FROM source_statistics_today
          INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid
          INNER JOIN buyers ON affiliates.buyer_id = buyers.id
          INNER JOIN accounts ON accounts.account_id = source_statistics_today.account_id
        WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) %s
        GROUP BY buyers.id, source_statistics_today.date, accounts.type
        ORDER BY buyers.name ASC, DATE DESC)
      UNION (
        SELECT
          0                                                         AS 'spent',
          sum(postback.sum /
                       (SELECT exchange.rate
                        FROM exchange
                        WHERE exchange.id = postback.exchange) *
                       (SELECT exchange.count
                        FROM exchange
                        WHERE exchange.id = postback.exchange)) AS 'revenue',
          buyers.id                                                 AS 'buyerId',
          buyers.name                                               AS 'buyer'
        FROM postback
          INNER JOIN affiliates ON affiliates.afid = postback.afid
          INNER JOIN buyers ON affiliates.buyer_id = buyers.id
          INNER JOIN adverts ON adverts.advshortname = postback.advname
          INNER JOIN adv_status ON adv_status.adv_id = adverts.id
        WHERE adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 %s
        GROUP BY affiliates.buyer_id, postback.date
      )) AS details
GROUP BY buyerId
ORDER BY revenue DESC;