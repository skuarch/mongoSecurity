package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public class SecurityClient {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ROLES = "roles";

    @JsonProperty(USERNAME)
    private String username;

    @JsonProperty(PASSWORD)
    private String password;

    @JsonProperty(ROLES)
    @JsonSerialize(contentUsing = GrantedAuthoritySerializer.class)
    @JsonDeserialize(contentUsing = GrantedAuthorityDeserializer.class)
    private List<GrantedAuthority> roles;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<GrantedAuthority> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", password)
                .append("roles", roles)
                .toString();
    }

}
