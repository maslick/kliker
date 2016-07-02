package com.maslick.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * Created by maslick on 02/07/16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counter {
    public HashMap<String, Long> counter;
}
