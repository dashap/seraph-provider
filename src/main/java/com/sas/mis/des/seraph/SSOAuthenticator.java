package com.sas.mis.des.seraph;

import com.atlassian.jira.security.login.JiraSeraphAuthenticator;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public class SSOAuthenticator extends JiraSeraphAuthenticator
{
    private static final Logger log = Logger.getLogger(SSOAuthenticator.class);

    public Principal getUser(HttpServletRequest request, HttpServletResponse response)
    {
        Principal user = null;

        try
        {
            if(request.getSession() != null && request.getSession().getAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY) != null)
            {
                user = (Principal) request.getSession().getAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY);
                String username = user.getName();
                log.info("Session found; user " + username + " is already logged in");
            }
            else 
            {
                user = request.getUserPrincipal();
                String username = user.getName();

                if (null != username) 
                {
                    log.info("Logged in using SSO via getUserPrincipal, with User " + username);
                    request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY, user);
                    request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_OUT_KEY, null);
                }
                else
                {
                    log.info("Did not login using SSO because user unknown");
                }
            }
        }
        catch (Exception e) // catch class cast exceptions
        {
            log.warn("Exception: " + e, e);
        }
        return user;
    }
}

