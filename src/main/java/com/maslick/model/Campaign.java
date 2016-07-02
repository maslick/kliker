package com.maslick.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
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
@Entity
public class Campaign {
    @Id
    private Long id;
    private String redirect_url;

    @Index
    private List<String> platform;
    private Date created;
    private Date updated;
}
