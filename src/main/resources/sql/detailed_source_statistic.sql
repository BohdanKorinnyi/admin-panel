SELECT
  TRUNCATE(expenses.sum, 2) AS 'spent',
  0                         AS 'revenue',
  buyers.id                 AS 'buyerId',
  buyers.name               AS 'buyer',
  expenses.date             AS 'date',
  expenses_type.name        AS 'type'
FROM expenses
  INNER JOIN buyers ON expenses.buyer_id = buyers.id
  INNER JOIN expenses_type ON expenses.type_id = expenses_type.id
WHERE expenses.sum != 0 AND expenses.date = ? AND buyers.id = ?
UNION (SELECT
         TRUNCATE(source_statistics.spent, 2) AS 'spent',
         0                                    AS 'revenue',
         buyers.id                            AS 'buyerId',
         buyers.name                          AS 'buyer',
         source_statistics.date               AS 'date',
         accounts.type                        AS 'type'
       FROM source_statistics
         INNER JOIN affiliates ON affiliates.afid = source_statistics.afid
         INNER JOIN buyers ON affiliates.buyer_id = buyers.id
         INNER JOIN accounts ON accounts.account_id = source_statistics.account_id
       WHERE source_statistics.spent != 0 AND source_statistics.date = ? AND buyers.id = ?)
UNION (SELECT
         TRUNCATE(source_statistics_today.spent, 2) AS 'spent',
         0                                          AS 'revenue',
         buyers.id                                  AS 'buyerId',
         buyers.name                                AS 'buyer',
         source_statistics_today.date               AS 'date',
         accounts.type                              AS 'type'
       FROM source_statistics_today
         INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid
         INNER JOIN buyers ON affiliates.buyer_id = buyers.id
         INNER JOIN accounts ON accounts.account_id = source_statistics_today.account_id
       WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = ? AND buyers.id = ?)
UNION (SELECT
         0                                                         AS 'spent',
         TRUNCATE(sum(postback.sum /
                      (SELECT exchange.rate
                       FROM exchange
                       WHERE exchange.id = postback.exchange) *
                      (SELECT exchange.count
                       FROM exchange
                       WHERE exchange.id = postback.exchange)), 2) AS 'revenue',
         buyers.id                                                 AS 'buyerId',
         buyers.name                                               AS 'buyer',
         postback.date                                             AS 'date',
         adverts.advname                                           AS 'type'
       FROM postback
         INNER JOIN affiliates ON affiliates.afid = postback.afid
         INNER JOIN buyers ON affiliates.buyer_id = buyers.id
         INNER JOIN adverts ON adverts.advshortname = postback.advname
         INNER JOIN adv_status ON adv_status.adv_id = adverts.id
       WHERE postback.sum != 0 AND (postback.duplicate != 'FULL' OR postback.duplicate IS NULL) AND
             adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.date = ? AND
             buyers.id = ?);