package com.maslick.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by maslick on 02/07/16.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counter {
    @Id
    private Long id;
    private Long campaign;
    private String platform;
    private Date timeOfClick;
    private String ipaddr;
}
