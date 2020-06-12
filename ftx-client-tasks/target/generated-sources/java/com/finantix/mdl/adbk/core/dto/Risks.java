
package com.finantix.mdl.adbk.core.dto;

import java.util.HashMap;
import java.util.Map;

public class Risks {

    private String riskOverride;
    private Result result;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getRiskOverride() {
        return riskOverride;
    }

    public void setRiskOverride(String riskOverride) {
        this.riskOverride = riskOverride;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
