SELECT
  sum(details.spent)   AS 'spent',
  sum(details.revenue) AS 'revenue'
FROM (SELECT
        TRUNCATE(sum(expenses.sum), 2) AS 'spent',
        0                              AS 'revenue'
      FROM expenses
      WHERE expenses.sum != 0 %s
      UNION (SELECT
               TRUNCATE(sum(source_statistics.spent), 2) AS 'spent',
               0                                         AS 'revenue'
             FROM source_statistics
             WHERE source_statistics.spent != 0 %s )
      UNION (
        SELECT
          TRUNCATE(sum(source_statistics_today.spent), 2) AS 'spent',
          0                                               AS 'revenue'
        FROM source_statistics_today
        WHERE source_statistics_today.spent != 0 %s )
      UNION (
        SELECT
          0                                                         AS 'spent',
          TRUNCATE(sum(postback.sum /
                       (SELECT exchange.rate
                        FROM exchange
                        WHERE exchange.id = postback.exchange) *
                       (SELECT exchange.count
                        FROM exchange
                        WHERE exchange.id = postback.exchange)), 2) AS 'revenue'
        FROM postback
          INNER JOIN adverts ON adverts.advshortname = postback.advname
          INNER JOIN adv_status ON adv_status.adv_id = adverts.id
        WHERE adv_status.real_status = 'approved' AND postback.status = adv_status.name AND postback.sum != 0 %s
      )) AS details;