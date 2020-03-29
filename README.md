## Ticket booking application

Ticket booking application is a prototype of an application to make reservations on screenings in cinema.

### How to start
In order to run Ticket booking application two gradle tasks need to be started: *gradlew build* and then *gradlew bootRun*.

### Demos
1. Shell script to build and run the application (file *build-run.sh*) is in *src/main/resources* directory.
2. During an application start it's initially populated with test data set.
This data set is read from *src/main/resources/data.csv* file. 
3. To see basic use case scenario, run shell script *src/main/resources/use-case-scenario.sh*. 
The script is using **jq** library in order to make work with JSON's easier.
Library is available under [jq github page](https://stedolan.github.io/jq).

### Continuous integration
In order to provide continuous integration I have added integration with Travis CI. 

### Additional assumptions

#### Screenings
* The system lists movies available after given time by user. 
Listing contains screenings starting from the rounded given hour 
and ending with the end of the day. 
Example: When user gives time '2020-01-04 15:30' (formatted with ISO-8601 standard)
the system lists all screenings starting with 15:00 till the end of the day
i.e. 23:59.

#### Reservations
* All seats chosen by user in single reservation have to be next to each other.
* If user wants to book *n* seats in the middle of the row 
and there were only *n+1* seats left user can make this reservation 
despite breaking the rule where there cannot be single place left over in a row
between two already reserved places. 
Example: Room in cinema has 5 seats in every row. In the first row 
there are already 3 seats reserved: 1, 2 and 5. User needs only one ticket.
According to main rule user would not be able to pick only one seat 
and successfully reserve ticket in the first row. But for last remaining seat
mentioned exception has been made. 

#### Code extra notes
* Project structure is divided into two domains - screenings and reservations. 
Both of them contains their domain entities, eg.
*pl.pandziak.ticketbookingapp.domain.screening.Screening.java* 
and *pl.pandziak.ticketbookingapp.domain.reservation.Reservation.java*.
In real-life project I would differentiate domain and data repository models (they should be separate classes), 
but for the sake of simplicity I decided not to do it in this project. 
This assumption affects code - domain classes are public instead of package-scoped (as it should be).
* In project exists one additional functionality that wasn't included in project requirements. 
To be specific - the reservation cancellation. 