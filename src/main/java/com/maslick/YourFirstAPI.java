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

import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
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
    public List<Campaign> getAllCampaigns(HttpServletRequest req) {
//        if(!isAuthenticated(req)) {
//            return null;
//        }

        Objectify ofy = OfyService.ofy();
        List<Campaign> result = ofy.load().type(Campaign.class).list();
        return result;
    }

    @ApiMethod(name = "addCampaign", path = "campaign/add", httpMethod = HttpMethod.POST)
    public Message addCampaign(Campaign camp) {
        Objectify ofy = OfyService.ofy();
        Campaign newCamp = new Campaign();
        newCamp.setPlatform(camp.getPlatform());
        newCamp.setRedirect_url(camp.getRedirect_url());
        newCamp.setCreated(new Date());
        newCamp.setUpdated(new Date());

        ofy.save().entity(newCamp).now();

        return new Message("OK", "Campaign added");
    }

    @ApiMethod(name = "editCampaign", path = "campaign/edit", httpMethod = HttpMethod.POST)
    @SuppressWarnings("unused")
    public Message editCampaign(Campaign camp) {
        Objectify ofy = OfyService.ofy();

        Campaign newCamp = findCampaignById(camp.getId());
        if (newCamp == null) {
            return new Message("Error", "Campaign not find");
        }

        newCamp.setPlatform(camp.getPlatform());
        newCamp.setRedirect_url(camp.getRedirect_url());
        newCamp.setUpdated(new Date());

        ofy.save().entity(newCamp).now();

        return new Message("OK", "Campaign updated");
    }


    @ApiMethod(name = "findCampaignByPlatform", path = "campaign/findByPlatform", httpMethod = HttpMethod.GET)
    public List<Campaign> findCampaignByPlatform(@Named("platform") @Nullable String platform) {
        Objectify ofy = OfyService.ofy();
        List<String> plat;
        List<Campaign> list = ofy.load().type(Campaign.class).filter("platform", platform).list();
        return list;
    }

    @ApiMethod(name = "counterOnPlatform", path="counter/platform", httpMethod = HttpMethod.GET)
    public Message getCounterOnPlatform(@Named("platform") @Nullable String platform) {
        Objectify ofy = OfyService.ofy();
        List<Counter> list = ofy.load().type(Counter.class).filter("platform", platform).list();
        Integer count = list.size();
        return new Message("OK", "Count: " + count);
    }

    @ApiMethod(name = "counterOnPlatformAndCampaign", path="counter", httpMethod = HttpMethod.GET)
    public Message getCounter(@Named("platform") @Nullable String platform,
                              @Named("campaign") @Nullable String id) {
        Objectify ofy = OfyService.ofy();
        List<Counter> list = ofy.load().type(Counter.class).filter("campaign", Long.parseLong(id)).filter("platform", platform).list();
        Integer count = list.size();
        return new Message("OK", "Count: " + count);
    }

    @ApiMethod(name = "redirect", path = "redirect", httpMethod = HttpMethod.GET)
    @SuppressWarnings("unused")
    public Message redirect(HttpServletRequest req,
                            @Named("platform") @Nullable String platform,
                            @Named("campaign") @Nullable String id) {

        if (platform == null || id == null) {
            return new Message("Error", "id or platform missing");
        }

        Campaign camp = findCampaignById(Long.parseLong(id));
        if (camp == null) {
            return new Message("Error", "Campaign not find");
        }

        Counter counter = new Counter();

        if (!camp.getPlatform().contains(platform)) {
            return new Message("Error", "This link doesn't work on your device");
        }

        counter.setCampaign(camp.getId());
        counter.setPlatform(platform);
        counter.setTimeOfClick(new Date());
        counter.setIpaddr(req.getRemoteAddr());


        Objectify ofy = OfyService.ofy();
        ofy.save().entity(counter).now();

        return new Message("OK", "platformType: " + platform + ", id: " + id + ". Redirecting to: " + camp.getRedirect_url());
    }

    private Campaign findCampaignById(Long id) {
        Objectify ofy = OfyService.ofy();
        return ofy.load().type(Campaign.class).id(id).now();
    }

    private boolean isAuthenticated(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");
        String[] values;
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));
            // credentials = username:password
            values = credentials.split(":", 2);
            if (values[0].equals("admin") && values[1].equals("admin")) {
                return true;
            }
        }
        return false;
    }

}
