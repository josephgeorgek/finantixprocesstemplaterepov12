
package com.finantix.mdl.adbk.core.dto;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private String currentRisk;
    private String defaultRisk;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCurrentRisk() {
        return currentRisk;
    }

    public void setCurrentRisk(String currentRisk) {
        this.currentRisk = currentRisk;
    }

    public String getDefaultRisk() {
        return defaultRisk;
    }

    public void setDefaultRisk(String defaultRisk) {
        this.defaultRisk = defaultRisk;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
