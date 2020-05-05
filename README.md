This project was written as a solution for LA1's [programming exercise](https://github.com/hawkescom/marsrover).

## Mars Rover Spring Boot Application
This is a simple demo application that collects photos from the [public NASA API](https://api.nasa.gov/) with two server endpoints.

1. `GET <service URL>/photos/` 
    Will download the first image returned by the Mars Rover on each of the dates specified in the dates.txt file. These images will be cached for later queries on the endpoint described below.

2. `GET <service URL>/photos/<date>`
    Will first query the NASA rover API for images on the specified date. Then the application will download the first image returned by the API (if any). Finally, it will return a Photo resource with an image URL of the now-locally-hosted image. Dates must be of one of the following formats, or a parse exception will be thrown:
     - `MM/dd/yy` (ex: 02/26/98)
     - `MMM dd, yyyy` (ex: February 26, 1998)
     - `MMM-dd-yyyy` (ex: Feb-26-1998)
     - `yyyy-MM-dd` (ex: 1998-02-26)
     
     Dates in the response will always be returned in the normalized format that NASA accepts (yyyy-MM-dd). Sample response for a GET on `http://localhost:8080/photos/June 2, 2018`:
     ```
   {
        "date": "2018-06-02",
        "url": "http://localhost:8080/photos/image/2018-06-02.jpeg"
    }
   ```
   If no images are found for the given date, the `url` field will be an empty string.
   
Separately, there is also a React UI included in this repo that you can build and run to interface with this simple API in a web browser.

### Running the service
#### Prerequisites
If you don't already have them, you will need to install the following:
- Maven
- A Java 11 runtime
- Docker

Two environment variables should be configured in order for the application to run properly:
- `BASE_URL`: This is the base URL of the application. Unless this is deployed remotely, this should typically be set to `http://localhost:8080`, which is also the default value.
- `NASA_API_KEY`: Any API key generated on [the NASA website](https://api.nasa.gov/).

#### Running the JAR directly
Build and run with Maven from the root directory of the project:
`./mvnw package && java -jar target/marsrover-0.0.1-SNAPSHOT.jar`

#### Running as a Docker container
First build a local Docker image with:
`./mvnw com.google.cloud.tools:jib-maven-plugin:dockerBuild -Dimage=marsrover`

Then run the image with:
`docker run -p 8080:8080 -e "NASA_API_KEY=<API key>" -t marsrover`

### Running the UI
#### Prerequisites
If you don't already have them, you will need to install the following:
- yarn
- node

#### Build and run
The UI does not run as part of the container or jar. These are the steps to use it:
1. Make sure you have the backend service running and exposed locally on port 8080 (either of the directions above for running as a jar or running in a container should do the trick).
2. `cd` to the ui directory (`src/ui`), and run `yarn install` to install the necessary node modules. Grab a Snickers.
3. Run `yarn start`. This should compile assets and then launch a browser tab at `http://localhost:3000`.
4. Have fun exploring NASA Mars rover images!

### Areas for improvement
1. Make the responses JSONAPI compliant rather than just rendering an isolated JSON object.
2. Better error handling. I'm no Spring or Java expert (yet), but it can't be healthy to just throw almost every exception up the stack. There are some exceptions (such as `DateParseException`) that should be rendered as client errors, for example.
3. Integration tests. Not familiar with the best frameworks for that. Maybe Selenium if I get the client code working? But even lower layer tests that may require mocking out the NASA client, in ways I don't yet know how to do in the Java world, could be valuable.
4. Probably goes without saying, but would be better to store images on a third party service like GCS or AWS rather than loading up our container.

... And given how long I've been out of the Java/OOP world, probably lots more.
