SELECT
  sum(details.revenue)                        AS 'revenue',
  sum(details.spent)                          AS 'spent',
  (sum(details.revenue) - sum(details.spent)) AS 'profit',
  month(details.date)                          AS 'date',
  substr(details.date,9)                      AS 'fullDate'
FROM (SELECT
        TRUNCATE(sum(expenses.sum), 2) AS 'spent',
        0                              AS 'revenue',
        date                           AS 'date'
      FROM expenses
        JOIN buyers ON expenses.buyer_id = buyers.id
      WHERE expenses.sum != 0 AND date BETWEEN :from AND :to AND buyers.id = :buyerId
      GROUP BY month(date)
      UNION (SELECT
               TRUNCATE(sum(source_statistics.spent), 2) AS 'spent',
               0                                         AS 'revenue',
               date                                      AS 'date'
             FROM source_statistics
               JOIN affiliates ON source_statistics.afid = affiliates.afid
               JOIN buyers ON affiliates.buyer_id = buyers.id
             WHERE source_statistics.spent != 0 AND date BETWEEN :from AND :to AND buyers.id = :buyerId
             GROUP BY month(date))
      UNION (
        SELECT
          TRUNCATE(sum(source_statistics_today.spent), 2) AS 'spent',
          0                                               AS 'revenue',
          date                                            AS 'date'
        FROM source_statistics_today
               JOIN affiliates ON source_statistics_today.afid = affiliates.afid
               JOIN buyers ON affiliates.buyer_id = buyers.id
        WHERE source_statistics_today.spent != 0 AND date = date(now()) AND buyers.id = :buyerId
        GROUP BY month(date))
      UNION (
        SELECT
          0                                                         AS 'spent',
          TRUNCATE(sum(postback.sum /
                       (SELECT exchange.rate
                        FROM exchange
                        WHERE exchange.id = postback.exchange) *
                       (SELECT exchange.count
                        FROM exchange
                        WHERE exchange.id = postback.exchange)), 2) AS 'revenue',
          postback.date                                             AS 'date'
        FROM postback
          JOIN adverts ON adverts.advshortname = postback.advname
          JOIN adv_status ON adv_status.adv_id = adverts.id
          JOIN affiliates ON postback.afid = affiliates.afid
          JOIN buyers ON affiliates.buyer_id = buyers.id
        WHERE adv_status.real_status = 'approved' AND postback.status = adv_status.name
              AND postback.sum != 0 AND date BETWEEN :from AND :to AND buyers.id = :buyerId
        GROUP BY month(date)
      )
     ) AS details
GROUP BY month(details.date);