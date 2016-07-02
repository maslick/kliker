package com.maslick;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.googlecode.objectify.Objectify;
import com.maslick.model.Campaign;
import com.maslick.model.Counter;
import com.maslick.model.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.logging.Logger;


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
    private static final Logger LOG = Logger.getLogger(YourFirstAPI.class.getName());
    private HashMap<String, Long> countCampaign = new HashMap<>();
    private HashMap<String, Long> countPlatform = new HashMap<>();


    public YourFirstAPI() {
        Campaign camp1 = new Campaign();
        camp1.setPlatform(new ArrayList<>(Arrays.asList(PlatformType.android, PlatformType.iphone)));
        camp1.setRedirect_url("google.com");
        camp1.setCreated(new Date());
        camp1.setUpdated(new Date());

        Campaign camp2 = new Campaign();
        camp2.setPlatform(new ArrayList<>(Arrays.asList(PlatformType.android, PlatformType.iphone)));
        camp2.setRedirect_url("yandex.ru");
        camp2.setCreated(new Date());
        camp2.setUpdated(new Date());



        Objectify ofy = OfyService.ofy();
        ofy.save().entity(camp1).now();
        ofy.save().entity(camp2).now();

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
        Objectify ofy = OfyService.ofy();
        List<Campaign> result = ofy.load().type(Campaign.class).list();
        return result;
    }

    @ApiMethod(name = "addCampaign", path = "campaign/add", httpMethod = HttpMethod.POST)
    @SuppressWarnings("unused")
    public Message addCampaign(Campaign camp) {
        camp.setCreated(new Date());
        camp.setUpdated(new Date());
        camps.add(camp);
        Objectify ofy = OfyService.ofy();
        Campaign newCamp = new Campaign();
        newCamp.setPlatform(camp.getPlatform());
        newCamp.setRedirect_url(camp.getRedirect_url());
        newCamp.setCreated(new Date());
        newCamp.setUpdated(new Date());

        ofy.save().entity(newCamp).now();

        return new Message("OK", "Campaign added");
    }

    @ApiMethod(name = "redirect", path = "redirect", httpMethod = HttpMethod.GET)
    @SuppressWarnings("unused")
    public Message redirect(@Named("platform") @Nullable String platform, @Named("id") @Nullable Integer id) {
        if (platform == null || id == null) {
            return new Message("Error", "id or platform missing");
        }

        if (countCampaign.containsKey(id.toString())) {
            countCampaign.put(id.toString(), countCampaign.get(id) + 1);
        } else {
            countCampaign.put(id.toString(), 1L);
        }

        if (countPlatform.containsKey(platform)) {
            countPlatform.put(platform, countPlatform.get(platform) + 1);
        } else {
            countPlatform.put(platform, 1L);
        }

        return new Message("OK", "platformType: " + platform + ", id: " + id);
    }


}
