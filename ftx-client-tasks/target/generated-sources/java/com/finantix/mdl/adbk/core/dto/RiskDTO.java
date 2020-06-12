
package com.finantix.mdl.adbk.core.dto;

import java.util.HashMap;
import java.util.Map;

public class RiskDTO {

    private String review;
    private Risks risks;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Risks getRisks() {
        return risks;
    }

    public void setRisks(Risks risks) {
        this.risks = risks;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
