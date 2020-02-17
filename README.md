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
   
   If no images are found for the given date, the `url` field will be empty. 

### Running the app
Two environment variables are necessary in order for the application to run properly:
- `BASE_URL`: This is the base URL of the application. Unless this is deployed remotely, this should typically be set to `http://localhost:8080`, which is also the default value.
- `NASA_API_KEY`: Any API key generated on [the NASA website](https://api.nasa.gov/).

#### Running directly
Build and run with Maven from the root directory of the project:
`./mvnw package && java -jar target/marsrover-0.0.1-SNAPSHOT.jar`

#### Running as a Docker container
First build a local Docker image with:
`./mvnw com.google.cloud.tools:jib-maven-plugin:dockerBuild -Dimage=marsrover`

Then run the image with:
`docker run -p 8080:8080 -e "NASA_API_KEY=<API key>" -t marsrover`

### Potential enhancements
1. Make the responses JSONAPI compliant rather than just rendering an isolated JSON object.
