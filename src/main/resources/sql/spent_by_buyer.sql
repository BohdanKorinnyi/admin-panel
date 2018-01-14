SELECT
  truncate(sum(result.spent), 2)       AS 'spent',
  result.source,
  result.buyerId,
  result.buyerName,
  DATE_FORMAT(result.date, '%d-%M-%y') AS 'date'
FROM (SELECT
        sum(expenses.sum)    AS 'spent',
        expenses.description AS 'source',
        expenses.buyer_id    AS 'buyerId',
        buyers.name          AS 'buyerName',
        expenses.date        AS 'date'
      FROM expenses
        INNER JOIN buyers ON expenses.buyer_id = buyers.id
      WHERE expenses.sum != 0 AND expenses.date BETWEEN :from AND :to AND
            IF(concat(:sources) IS NULL, TRUE, expenses.description IN (:sources))
            AND IF(concat(:buyers) IS NULL, TRUE, buyers.id IN (:buyers))
      GROUP BY expenses.date, expenses.description, buyers.id
      UNION (SELECT
               sum(source_statistics.spent) AS 'spent',
               accounts.name                AS 'source',
               buyers.id                    AS 'buyerId',
               buyers.name                  AS 'buyerName',
               source_statistics.date       AS 'date'
             FROM source_statistics
               INNER JOIN affiliates ON affiliates.afid = source_statistics.afid
               INNER JOIN buyers ON affiliates.buyer_id = buyers.id
               INNER JOIN accounts ON source_statistics.account_id = accounts.account_id
             WHERE source_statistics.spent != 0 AND source_statistics.date BETWEEN :from AND :to AND
                   IF(concat(:sources) IS NULL, TRUE, accounts.type IN (:sources))
                   AND IF(concat(:buyers) IS NULL, TRUE, buyers.id IN (:buyers))
             GROUP BY source_statistics.date, source_statistics.account_id, buyers.id)
      UNION (SELECT
               sum(source_statistics_today.spent) AS 'spent',
               accounts.name                      AS 'source',
               buyers.id                          AS 'buyerId',
               buyers.name                        AS 'buyerName',
               source_statistics_today.date       AS 'date'
             FROM source_statistics_today
               INNER JOIN affiliates ON affiliates.afid = source_statistics_today.afid
               INNER JOIN buyers ON affiliates.buyer_id = buyers.id
               INNER JOIN accounts ON source_statistics_today.account_id = accounts.account_id
             WHERE source_statistics_today.spent != 0 AND source_statistics_today.date = date(now()) AND
                   source_statistics_today.date BETWEEN :from AND :to AND
                   IF(concat(:sources) IS NULL, TRUE, accounts.type IN (:sources))
                   AND IF(concat(:buyers) IS NULL, TRUE, buyers.id IN (:buyers))
             GROUP BY source_statistics_today.date, source_statistics_today.account_id,
               buyers.id)
     ) AS result
GROUP BY result.date, result.source, result.buyerId
ORDER BY result.date DESC;