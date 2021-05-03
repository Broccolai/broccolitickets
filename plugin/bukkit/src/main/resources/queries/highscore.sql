SELECT COUNT(*) AS `num`
FROM puretickets_ticket
WHERE status = 'CLOSED'
  AND claimer = :uuid
GROUP BY claimer
