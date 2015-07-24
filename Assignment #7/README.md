This is my Assignment #7 deliverable.  This folder contains the full Java application and three screenshots during its execution.

This application diplays weather information for any given US zip code.

To run this application the user must:

1. Enable geonames webservices using their GeoNames account
2. Download (a) GeoNames .jar http://www.geonames.org/source-code/geonames-source-1.1.12.jar
            (b) JDOM .jar http://www.geonames.org/source-code/jdom-1.0.jar
3. Add the .jar files to the build path of the project
4. Enter their username into the dialog box
5. Enter a valid US zip code (e.g. 33473)
6. View output weather observation (i.e. coordinates used, last observation, temperature in degrees celcius, and humidity).


This application uses the GeoNames Java Client.

In a nutshell, the user is prompted for a valid US postal code (zip code), which is used to obtain the nearest longitude and latitude infomration via the findNearbyPostalCode() method.  These coordinates are then used as the input for the 
findNearbyWeather() method.
