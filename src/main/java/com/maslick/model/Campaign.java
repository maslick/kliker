package com.maslick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by maslick on 02/07/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    private long id;
    private String redirect_url;
    private List<String> platform;
    private Date created;
    private Date updated;
}
