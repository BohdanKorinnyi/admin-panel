SELECT
  truncate(sum(details.revenue), 2)                        AS 'revenue',
  truncate(sum(details.spent), 2)                          AS 'spent',
  truncate((sum(details.revenue) - sum(details.spent)), 2) AS 'profit',
  dayname(details.date)                                    AS 'day',
  date(details.date)                                       AS 'date',
  details.buyer                                            AS 'buyer'
FROM (SELECT
        sum(expenses.sum) AS 'spent',
        0                 AS 'revenue',
        expenses.date     AS 'date',
        buyers.name       AS 'buyer'
      FROM expenses
        JOIN buyers ON expenses.buyer_id = buyers.id
      WHERE expenses.sum != 0 AND expenses.date BETWEEN :from AND :to
      GROUP BY buyers.id, date(expenses.date)
      UNION (SELECT
               sum(source_statistics.spent) AS 'spent',
               0                            AS 'revenue',
               source_statistics.date       AS 'date',
               buyers.name                  AS 'buyer'
             FROM source_statistics
               JOIN affiliates ON source_statistics.afid = affiliates.afid
               JOIN buyers ON affiliates.buyer_id = buyers.id
             WHERE source_statistics.spent != 0 AND date BETWEEN :from AND :to
             GROUP BY buyers.id, date(source_statistics.date))
      UNION (
        SELECT
          sum(source_statistics_today.spent) AS 'spent',
          0                                  AS 'revenue',
          date                               AS 'date',
          buyers.name                        AS 'buyer'
        FROM source_statistics_today
          JOIN affiliates ON source_statistics_today.afid = affiliates.afid
          JOIN buyers ON affiliates.buyer_id = buyers.id
        WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) AND month(source_statistics_today.date) = month(:from)
        GROUP BY buyers.id, date(date))
      UNION (
        SELECT
          0                                            AS 'spent',
          sum(postback.sum /
              (SELECT exchange.rate
               FROM exchange
               WHERE exchange.id = postback.exchange) *
              (SELECT exchange.count
               FROM exchange
               WHERE exchange.id = postback.exchange)) AS 'revenue',
          postback.date                                AS 'date',
          buyers.name                                  AS 'buyer'
        FROM postback
          JOIN adverts ON adverts.advshortname = postback.advname
          JOIN adv_status ON adv_status.adv_id = adverts.id
          JOIN affiliates ON postback.afid = affiliates.afid
          JOIN buyers ON affiliates.buyer_id = buyers.id
        WHERE adv_status.real_status = 'approved' AND (postback.duplicate != 'FULL' OR postback.duplicate IS NULL)
         AND postback.status = adv_status.name AND postback.sum != 0 AND date BETWEEN :from AND :to
        GROUP BY buyers.id, date(date)
      )
     ) AS details
GROUP BY details.buyer, date(details.date)
ORDER BY details.date ASC;