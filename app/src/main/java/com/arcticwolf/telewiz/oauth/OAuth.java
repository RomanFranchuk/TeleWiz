package com.arcticwolf.telewiz.oauth;

/**
 * Created by michael on 12/21/16.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "value",
        "expiration",
        "tokenType",
        "refreshToken",
        "scope",
        "additionalInformation",
        "expired",
        "expiresIn"
})
public class OAuth {

    @JsonProperty("value")
    private String value;
    @JsonProperty("expiration")
    private long expiration;
    @JsonProperty("tokenType")
    private String tokenType;
    @JsonProperty("refreshToken")
    private RefreshToken refreshToken;
    @JsonProperty("scope")
    private List<String> scope = null;
    @JsonProperty("additionalInformation")
    private AdditionalInformation additionalInformation;
    @JsonProperty("expired")
    private Boolean expired;
    @JsonProperty("expiresIn")
    private Integer expiresIn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("expiration")
    public long getExpiration() {
        return expiration;
    }

    @JsonProperty("expiration")
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("tokenType")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("refreshToken")
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refreshToken")
    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonProperty("scope")
    public List<String> getScope() {
        return scope;
    }

    @JsonProperty("scope")
    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    @JsonProperty("additionalInformation")
    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    @JsonProperty("additionalInformation")
    public void setAdditionalInformation(AdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @JsonProperty("expired")
    public Boolean getExpired() {
        return expired;
    }

    @JsonProperty("expired")
    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    @JsonProperty("expiresIn")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expiresIn")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
