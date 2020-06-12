
package com.finantix.mdl.adbk.core.dto;

import java.util.HashMap;
import java.util.Map;

public class PartyDTO {

    private Boolean draft;
    private Integer id;
    private Boolean personal;
    private String relationshipSubType;
    private String relationshipType;
    private ToItem toItem;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    public String getRelationshipSubType() {
        return relationshipSubType;
    }

    public void setRelationshipSubType(String relationshipSubType) {
        this.relationshipSubType = relationshipSubType;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public ToItem getToItem() {
        return toItem;
    }

    public void setToItem(ToItem toItem) {
        this.toItem = toItem;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    
  
    public String getContent() {
    	return getId()+">>"+getToItem()+">>"+getAdditionalProperties();
    }

}
