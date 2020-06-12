
package com.finantix.mdl.adbk.core.dto;

import java.util.HashMap;
import java.util.Map;

public class ReviewDTO {

    private String request;
    private String review;
    private Providers providers;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Providers getProviders() {
        return providers;
    }

    public void setProviders(Providers providers) {
        this.providers = providers;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
