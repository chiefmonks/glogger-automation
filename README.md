# Auto G Version 2.0.0.
- The program runs on background which will not affect your work.
- Right now it only works as happy path, no validation etc.
- Used at your own risk.


## Configuration
The configuration is located on config.properties under the program home directory.

>### Details
1.	http.proxyHost=<proxy host>
2.	http.proxyPort=<proxy port>
3.	url=<Site url>
4.	username=Firstname.Lastname
5.	password=<password>
6.	sub-group=<user sub group>
7.	office-location=<user office location>
8.	time-out=60

>### time-buffer
- Time buffer is the number of minutes added to your total work hours before logout.
- Default is 5 minutes.

>### Fix Time Schedule (dynamic-schedule=false)
- Fix time schedule will execute actions using fix time settings which values on format HH:mm.
- Required configs:

>>#### fix-login
- Login time
- Ex. value fix-login=09:00 which will execute login at 9AM.

>>#### fix-meal
- Meal time
- Ex. value fix-meal=12:00 which will execute meal at 12PM.

>>#### fix-clockin
- Clock in time after meal
- Ex. value fix-clockin=12:30 which will execute clockin at 12:30PM.

>>#### fix-logout
- Logout time
- Ex. value fix-logout=18:00 which will execute logout at 6PM.


>### Dynamic Schedule (dynamic-schedule=true)
- Dynamic schedule will login upon execution of application
- The login time will be set to application startup time.
- Required configs:

>>#### meal-time
- Meal Time is the number of hours added to login time to set as your lunch break.
- Ex. login is 9am, you put 4 as meal-time, your lunch break will execute at (9+4) = 1pm.

>>#### meal-duration
- Meal duration is the number of minutes lunch break is set before clocking in again.
- Default value is 60 minutes or 1hr from your meal time.


## Prerequisites
- JDK 1.7 or later

## Run
- Execute or double click auto-G-2.0.0-jar-with-dependencies.jar.
