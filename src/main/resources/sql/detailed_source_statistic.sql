SELECT
  truncate(details.spent, 2)   AS 'spent',
  truncate(details.revenue, 2) AS 'revenue',
  details.buyerId,
  details.buyer,
  details.date,
  details.type
FROM (SELECT
        sum(expenses.sum)    AS 'spent',
        0                    AS 'revenue',
        buyers.id            AS 'buyerId',
        buyers.name          AS 'buyer',
        expenses.date        AS 'date',
        expenses.description AS 'type'
      FROM expenses
        INNER JOIN buyers ON expenses.buyer_id = buyers.id
      WHERE expenses.sum != 0 AND expenses.date = ? AND buyers.id = ?
      GROUP BY expenses.description
      UNION (SELECT
               sum(source_statistics.spent) AS 'spent',
               0                            AS 'revenue',
               buyers.id                    AS 'buyerId',
               buyers.name                  AS 'buyer',
               source_statistics.date       AS 'date',
               accounts.name                AS 'type'
             FROM source_statistics
               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid
               INNER JOIN buyers ON affiliates.buyer_id = buyers.id
               INNER JOIN accounts ON accounts.account_id = source_statistics.account_id
             WHERE source_statistics.spent != 0 AND source_statistics.date = ? AND buyers.id = ?
             GROUP BY accounts.account_id, accounts.name)
      UNION (SELECT
               sum(source_statistics_today.spent) AS 'spent',
               0                                  AS 'revenue',
               buyers.id                          AS 'buyerId',
               buyers.name                        AS 'buyer',
               source_statistics_today.date       AS 'date',
               accounts.name                      AS 'type'
             FROM source_statistics_today
               INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid
               INNER JOIN buyers ON affiliates.buyer_id = buyers.id
               INNER JOIN accounts ON accounts.account_id = source_statistics_today.account_id
             WHERE
               source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) AND buyers.id = ?
             GROUP BY accounts.account_id, accounts.name)
      UNION (SELECT
               0                                            AS 'spent',
               sum(postback.sum /
                   (SELECT exchange.rate
                    FROM exchange
                    WHERE exchange.id = postback.exchange) *
                   (SELECT exchange.count
                    FROM exchange
                    WHERE exchange.id = postback.exchange)) AS 'revenue',
               buyers.id                                    AS 'buyerId',
               buyers.name                                  AS 'buyer',
               postback.date                                AS 'date',
               adverts.advname                              AS 'type'
             FROM postback
               INNER JOIN affiliates ON affiliates.afid = postback.afid
               INNER JOIN buyers ON affiliates.buyer_id = buyers.id
               INNER JOIN adverts ON adverts.advshortname = postback.advname
               INNER JOIN adv_status ON adv_status.adv_id = adverts.id
             WHERE postback.sum != 0 AND (postback.duplicate != 'FULL' OR postback.duplicate IS NULL) AND
                   adv_status.real_status = 'approved' AND postback.status = adv_status.name AND
                   postback.date = ?
                   AND buyers.id = ?
             GROUP BY adverts.advname)) AS details;