## Mars Rover Sprint Boot Application
This is a simple demo application that collects photos from the [public NASA API](https://api.nasa.gov/) with two server endpoints.

1. `GET <service URL>/photos/` 
    Will download the first image returned by the Mars Rover on each of the dates specified in the dates.txt file. These images will be cached for later queries on the endpoint described below.

2. `GET <service URL>/photos/<date>`
    Will first query the NASA rover API for images on the specified date. Then the application will download the first image returned by the API (if any). Finally, it will return a Photo resource with an image URL of the now-locally-hosted image. Dates must be of one of the following formats, or a parse exception will be thrown:
     - `MM/dd/yy` (ex: 02/26/98)
     - `MMM dd, yyyy` (ex: February 26, 1998)
     - `MMM-dd-yyyy` (ex: Feb-26-1998)
     
     Sample response for a GET on `http://localhost:8080/photos/June 2, 2018`:
     ```
   {
        "date": "2018-06-02",
        "url": "http://localhost:8080/photos/image/2018-06-02.jpeg"
    }

### Instructions for running
Two environment variables are necessary in order for the application to run properly:
- `BASE_URL`: This is the base URL of the application. Unless this is deployed remotely, this should typically be set to `http://localhost:8080`
- `NASA_API_KEY`: Any API key generated on [the NASA website](https://api.nasa.gov/).

