package com.maslick;

import com.googlecode.objectify.Objectify;
import com.maslick.model.Campaign;
import com.maslick.model.Counter;

import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by maslick on 03/07/16.
 */
@SuppressWarnings("serial")
@WebServlet
public class RedirectServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String platform = req.getParameter("platform");
        String campaign = req.getParameter("campaign");

        PrintWriter out = resp.getWriter();
        if (platform == null || campaign == null) {
            out.println("Id or platform missing");
            return;
        }

        Objectify ofy = OfyService.ofy();
        Campaign camp = ofy.load().type(Campaign.class).id(Long.parseLong(campaign)).now();

        if (camp == null) {
            out.println("Campaign " + campaign + " not found");
            return;
        }

        if (!camp.getPlatform().contains(platform)) {
            out.println("This link doesn't work on your device");
            return;
        }

        Counter counter = new Counter();
        counter.setCampaign(camp.getId());
        counter.setPlatform(platform);
        counter.setTimeOfClick(new Date());
        counter.setIpaddr(req.getRemoteAddr());
        ofy.save().entity(counter).now();
        out.println("platform: " + platform + ", campaign: " + campaign + ". Redirecting to: " + camp.getRedirect_url());

        //resp.sendRedirect(camp.getRedirect_url());

    }
}
