# BreadKrumb Backend

BreadKrumb creates clusters around user selected assets.

The app uses:
### Alpaca
- Free API for market data and creating watchlists
### Back4App
- Free backend as a service, that handles user data, sessions and custom objects
- Back4App stores data and serves the BreadKrumWeb
### Back4App
- Website that reads from Back4App to show watclists and position breakdown
  
## Installation 
Follow the instructions to install [Springboot](https://docs.spring.io/spring-boot/installing.html) 

Then run:

```
./gradlew bootRun
```

## Usage
Make sure you have `curl` installed 

### GetAllWatchlist
```
curl "localhost:8080/watchlists
```

### GetWatchlistById
Change id to id retrieved in Alpaca 
```
curl "localhost:8080/watchlistID?id={id}
```

### PostWatchlist
Example call
```
curl --location 'localhost:8080/watchlists' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Name",
    "assets": [
        {
            "symbol": "S"
        },
        {
            "symbol": "E"
        },
        {
            "symbol": "T"
        }
    ]
}'
```
