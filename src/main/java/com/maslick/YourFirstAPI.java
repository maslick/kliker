package com.maslick;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.maslick.model.Campaign;
import com.maslick.model.Counter;
import com.maslick.model.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;


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
    private List<Campaign> camps = new ArrayList<>();

    public YourFirstAPI() {
        Campaign camp1 = new Campaign();
        camp1.setId(1);
        camp1.setPlatform(new ArrayList<>(Arrays.asList(PlatformType.android, PlatformType.iphone)));
        camp1.setRedirect_url("google.com");
        camp1.setCreated(new Date());
        camp1.setUpdated(new Date());

        Campaign camp2 = new Campaign();
        camp2.setId(2);
        camp2.setPlatform(new ArrayList<>(Arrays.asList(PlatformType.android, PlatformType.iphone)));
        camp2.setRedirect_url("yandex.ru");
        camp2.setCreated(new Date());
        camp2.setUpdated(new Date());

        camps.add(camp1);
        camps.add(camp2);
    }


    @Data
    @AllArgsConstructor
    public class Message {
        private String status;
        private String message;
    }

    @ApiMethod(name = "getAllCampaigns", path = "campaign/getAll", httpMethod = HttpMethod.GET)
    @SuppressWarnings("unused")
    public List<Campaign> getAllCampaigns() {
        return camps;
    }

    @ApiMethod(name = "addCampaign", path = "campaign/add", httpMethod = HttpMethod.POST)
    @SuppressWarnings("unused")
    public Message addCampaign(Campaign camp) {
        camp.setCreated(new Date());
        camp.setUpdated(new Date());
        camps.add(camp);
        return new Message("OK", "Campaign added");
    }

    @ApiMethod(name = "redirect", path = "redirect", httpMethod = HttpMethod.GET)
    @SuppressWarnings("unused")
    public Message redirect(@Named("platform") @Nullable String platform, @Named("id") @Nullable Integer id) {
        if (platform == null || id == null) {
            return new Message("Error", "id or platform missing");
        }

        return new Message("OK", "platformType: " + platform + ", id: " + id);
    }


}
