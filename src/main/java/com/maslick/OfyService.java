package com.maslick;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.maslick.model.Campaign;
import com.maslick.model.Counter;

/**
 * Custom Objectify Service that this application should use.
 */
public class OfyService {

    // This static block ensure the entity registration.
    static {
        factory().register(Counter.class);
        factory().register(Campaign.class);
    }

    // Use this static method for getting the Objectify service factory.
    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

    /**
     * Use this static method for getting the Objectify service object in order
     * to make sure the above static block is executed before using Objectify.
     *
     * @return Objectify service object.
     */
    @SuppressWarnings("unused")
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }
}