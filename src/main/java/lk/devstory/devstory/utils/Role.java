package lk.devstory.devstory.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("admin") ROLE_ADMIN,
    @JsonProperty("user") ROLE_USER
}
