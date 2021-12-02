# Advanced OOP - Assignment 2 By Christofer Cousins

## [🌌API, NASA's astronomy picture of the day🌌](https://api.nasa.gov/index.html#browseAPI.apod)

### Important things to note

- A common acronym used is APOD, A.P.O.D., and apod which all mean astronomy picture of the day
- When using the random spinner to return APODs the console will blow up with errors even though nothing bad is
  technically happening and the method throws the error please ignored
    - The error is that a JavaFX control is being updated outside the JFX Thread, yet it still updates so 🤷
- Furthermore, there is a possibility of at 2 daemon threads to be left running so please mindful if you close the
  application with `x` or exit buttons
    - This will occur if you close the app using the button and not the stop in the IDE. If you close the app while
      calling for multiple APODs that have not yet been returned to listview the 2 daemon threads will be left. One
      querying NASA's API and the other updating the no longer active progress/loading bar.
- Many of the explanations for the APODs vary in length, but are all very long, so please resize the window (stage)
  accordingly to view it all. There was no elegant way to get it to all fit but did my best show the most text for most
  of the APODs.
- If Maven dependencies don't load please try opening theMaven panel (`shift + shift` type "`maven"`) and click the 
  refresh button
- If no explanation or note appears for an APOD it's because there is none and just a graphic, this common of 
  queries of old APODs, before 2002

### API INFORMATION

Information about the API can be found on [NASA's website](https://api.nasa.gov/index.html#browseAPI.apod).  
More detailed information can be found in their [GitHub repository](https://github.com/nasa/apod-api#docs-)  
If you need my key it can be found in my uri sting in
the [API class](/src/main/java/space/nasa/spaceapi/utilities/API.java).   
Example query: https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY

### Be Aware

__The maximum amount of API queries an hour is 1000 requests. Please be mindful if you make a lot of requests and
suddenly errors occur.__  
I have not done stress test for reaching those limits yet😄.

#### API Response Validation

if you would like to validate the information my app displays from the api you can search the results using my key 
with url, https://api.nasa.gov/planetary/apod?thumbs=true&api_key=1rp568Tl7gR9976UiFzaPbedFvxnBFFYbdqxXazV   

- for multiple results add on `&start_date=` _**start_date from app**_ `&end_date=` **_end_date from app_**
  - dates formatted as `YYYY-MM-DD`
- A single APOD `&date=` **_date from app_**
I recommend to search within firefox for pretty and legibleJSON formatting