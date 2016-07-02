package com.maslick;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.maslick.model.Campaign;
import com.maslick.model.Mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/

@Api (
        name = "linky",
        version = "v1",
        description = "linky API"
)
public class YourFirstAPI {
    private List<Campaign> camps;

    @ApiMethod(
            path = "helloworld",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    @SuppressWarnings("unused")
    public Campaign helloworld() {
        Campaign camp = new Campaign();
        camp.setId(1);
        camp.setPlatform(new ArrayList<>(Arrays.asList(Mobile.android, Mobile.iphone)));
        camp.setRedirect_url("google.com");
        camp.setCreated(new Date());

        return camp;
    }

}
