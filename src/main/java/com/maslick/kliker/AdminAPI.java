package com.maslick.kliker;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.Objectify;
import com.maslick.kliker.model.Campaign;
import com.maslick.kliker.model.Counter;
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
        description = "admin API"
)
public class AdminAPI {
    private static final Logger LOG = Logger.getLogger(AdminAPI.class.getName());
    private static final String username = "admin";
    private static final String password = "admin12345";


    @Data
    @AllArgsConstructor
    public class Message {
        private String status;
        private String message;
    }

    @ApiMethod(name = "getAllCampaigns", path = "campaign/getAll", httpMethod = HttpMethod.GET)
     @SuppressWarnings("unused")
     public List<Campaign> getAllCampaigns(HttpServletRequest req) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

        Objectify ofy = OfyService.ofy();
        List<Campaign> result = ofy.load().type(Campaign.class).list();
        return result;
    }

    @ApiMethod(name = "addCampaign", path = "campaign/add", httpMethod = HttpMethod.POST)
    public Message addCampaign(HttpServletRequest req, Campaign camp) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

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
    public Message editCampaign(HttpServletRequest req, Campaign camp) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

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

    @ApiMethod(name = "deleteCampaign", path = "campaign/delete", httpMethod = HttpMethod.GET)
    @SuppressWarnings("unused")
    public Message deleteCampaign(HttpServletRequest req,
                                  @Named("campaign") String id) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

        Objectify ofy = OfyService.ofy();

        Campaign camp = findCampaignById(Long.parseLong(id));
        if (camp == null) {
            return new Message("Error", "Campaign not find");
        }

        ofy.delete().entity(camp).now();

        return new Message("OK", "Campaign deleted");
    }

    @ApiMethod(name = "findCampaignByPlatform", path = "campaign/findByPlatform", httpMethod = HttpMethod.GET)
    public List<Campaign> findCampaignByPlatform(HttpServletRequest req,
                                                 @Named("platform") @Nullable String platform) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

        Objectify ofy = OfyService.ofy();
        List<String> plat;
        List<Campaign> list = ofy.load().type(Campaign.class).filter("platform", platform).list();
        return list;
    }

    @ApiMethod(name = "counterOnPlatform", path="counter/platform", httpMethod = HttpMethod.GET)
    public Message getCounterOnPlatform(HttpServletRequest req,
                                        @Named("platform") @Nullable String platform) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

        Objectify ofy = OfyService.ofy();
        List<Counter> list = ofy.load().type(Counter.class).filter("platform", platform).list();
        Integer count = list.size();
        return new Message("OK", count.toString());
    }

    @ApiMethod(name = "counterOnPlatformAndCampaign", path="counter", httpMethod = HttpMethod.GET)
    public Message getCounter(HttpServletRequest req,
                              @Named("platform") @Nullable String platform,
                              @Named("campaign") @Nullable String id) throws UnauthorizedException {
        if(!isAuthenticated(req)) {
            throw new UnauthorizedException("Unauthorized");
        }

        Objectify ofy = OfyService.ofy();
        List<Counter> list = ofy.load().type(Counter.class).filter("campaign", Long.parseLong(id)).filter("platform", platform).list();
        Integer count = list.size();
        return new Message("OK", count.toString());
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
            if (values[0].equals(username) && values[1].equals(password)) {
                return true;
            }
        }
        return false;
    }
}
