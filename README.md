# BreadKrumB(ackend)

BreadKrumB is a data layer that sources from Alpaca and Back4App. It replicates watchlist and asset data into a local SQLite instance

The app uses:
### Alpaca
- Free API for market data and creating watchlists
### Back4App
- Free backend as a service, that handles user data, sessions and custom objects
- Back4App stores auxilliary data and serves BreadKrumWeb
  
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
curl "localhost:8080/watchlists/all"
```

### PostWatchlist
Example call
```
curl -X POST "localhost:8080/watchlist" -H "Content-Type: application/json" -d '{"id":"ee73d4ac-7c7a-406d-ab2c-e7d66ce8c21b","name":"TestWatchlist","accountId":"test-account-003","assets":[],"enabled":true,"createdAt":"2023-10-22 18:00:00.0","updatedAt":"2023-10-22 18:00:00.0"}'
```
