package com.example;

import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private boolean isAutenticated = false;

    //==========================================================================
    @Autowired
    private MongoCollection users;

    //==========================================================================
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (userDetails == null) {            
            authentication.setAuthenticated(false);
            isAutenticated = false;            
            return;
        }

        if (!userDetails.getPassword().equals(authentication.toString())) {
            authentication.setAuthenticated(false);
            isAutenticated = false;
            return;
        }        

        authentication.setAuthenticated(true);
        isAutenticated = true;

    }

    //==========================================================================
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        UserDetails userDetails;

        try {

            SecurityClient securityClient = users.findOne(
                    "{#: #, #: #}",
                    SecurityClient.USERNAME,
                    username,
                    SecurityClient.PASSWORD,
                    authentication.toString())
                    .as(SecurityClient.class);
            
            System.out.println("mcoostos " + securityClient);

            if (!securityClient.getPassword().equals(authentication.getCredentials().toString())) {
                authentication.setAuthenticated(false);
                isAutenticated = false;
            }

            userDetails = new User(
                    securityClient.getUsername(),
                    securityClient.getPassword(),
                    securityClient.getRoles());

        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return userDetails;
    }

    //==========================================================================
    /*@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication != null) {
            authentication.setAuthenticated(isAutenticated);
            super.authenticate(authentication);
        }

        return authentication;
    }*/

}
