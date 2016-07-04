package com.maslick.kliker.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by maslick on 02/07/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Counter {
    @Id
    private Long id;
    @Index
    private Long campaign;
    @Index
    private String platform;
    private Date timeOfClick;
    private String ipaddr;
}
