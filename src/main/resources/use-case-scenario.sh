#!/bin/sh
echo ''
echo '>>> GET ALL SCREENINGS AFTER GIVEN DATE'
ECHO '>>> DATE: 2020-04-13 13:00'
curl http://127.0.0.1:8080/screenings?from=2020-04-13T13:00 | jq
screeningId=`curl http://127.0.0.1:8080/screenings?from=2020-04-13T13:00 | jq -r '.screenings[0].screeningId'`
echo ''
echo '>>> SHOW DETALIS ABOUT SCREENING WITH ID '"$screeningId"
curl http://127.0.0.1:8080/screenings/${screeningId} | jq
echo ''
echo '>>> MAKE RESERVATION'
curl -H "Content-Type: application/json" -d '{"userName":"Jan","userSurname":"Kowalski","tickets":{"tickets":[{"row":2,"column":4,"ticketType":"ADULT"},{"row":2,"column":5,"ticketType":"CHILD"}]},"screeningId":"'"$screeningId"'"}' http://127.0.0.1:8080/reservations | jq





