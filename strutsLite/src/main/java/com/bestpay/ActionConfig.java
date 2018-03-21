package com.bestpay;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒋雨辰 on 2018-03-21.
 */
public class ActionConfig {
    public String className;
    public Map<String, String> result = new HashMap<>();

    public ActionConfig(String className) {
        this.className = className;
    }
}
