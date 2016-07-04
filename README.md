*kliKer*
------------------------------------
[Live demo](https://klikerpiker.appspot.com) (username: admin, password: admin12345, backend url: leave blank or use https://klikerpiker.appspot.com)


Admin API:

```
/_ah/api/linky/v1/campaign/getAll                        -- get a list of campaigns (GET)

/_ah/api/linky/v1/campaign/add                           -- add a new campaign (POST)

/_ah/api/linky/v1/campaign/edit                          -- edit campaign (POST)

/_ah/api/linky/v1/campaign/delete?campaign={id}          -- delete campaign (GET)    

/_ah/api/linky/v1/campaign/findByPlatform?platform={pl}  -- find all campaigns for a given platform (GET)  

/_ah/api/linky/v1/counter/platform?platform={pl}         -- get number of clicks from specified platform (GET)

/_ah/api/linky/v1/counter?platform={pl}&campaign={cn}    -- get number of clicks with the given campaign and platform (GET)

```

Tracker API:
```
/_ah/spi/redirectos?platform={pl}&campaign={cn}          -- redirect endpoint(GET)

```

**Local testing**
```
$ mvn appengine:devserver
```


**Deploy to GAE**
* Edit src/main/webapp/appengine-web.xml and set app name 
```
<application>klikerpiker</application>
```

* Edit set.account.sh and set your GAE account and project name and then run: 
```
$ ./set.account.sh
```
 
* Deploy
```
$ mvn appengine:update
```

## FRONTEND (optional)
* Build the [project](https://github.com/maslick/kliker-ui)
* Copy the contents of dist/ directory to src/main/webapp/frontend
* If you're testing on a dev machine, disable CORS to be able to talk to the backend

```
Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --user-data-dir=test --unsafely-treat-insecure-origin-as-secure=http://localhost:8080 --disable-web-security
```


