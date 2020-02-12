## Mars Rover Sprint Boot Application

This is a simple demo application that collects photos from the [public NASA API](https://api.nasa.gov/) with two server endpoints.

1. `GET <service URL>/photos/` 
    Will download a single randomly chosen image collected by the Mars Rover on each of the dates specified in the dates.txt file.

2. `GET <service URL>/photos/<date>`
    Will render a single randomly chosen image URL from the date specified in the `date` URL parameter. Dates must be of one of the following formats, or a parse exception will be thrown:
     - `MM/dd/yy` (ex: 02/26/98)
     - `MMM dd, yyyy` (ex: February 26, 1998)
     - `MMM-dd-yyyy` (ex: Feb-26-1998)
