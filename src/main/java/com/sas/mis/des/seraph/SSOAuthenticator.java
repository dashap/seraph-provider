package com.sas.mis.des.seraph;

import com.atlassian.jira.security.login.JiraSeraphAuthenticator;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Extension of DefaultAuthenticator that uses third-party code to determine if a user is logged in,
 * given a HTTPRequest object.
 * Third-party code will typically check for the existence of a special cookie.
 *
 * In SSO scenarios where this authenticator is used, one typically configures Seraph to use an external login page
 * as well:
 *  *  <init-param>
 *    <param-name>login.url</param-name>
 *    <param-value>http://mycompany.com/globallogin?target=${originalurl}</param-value>
 *  </init-param>
 *
 *
 */
public class SSOAuthenticator extends JiraSeraphAuthenticator
{

    //private static final Category log = Category.getInstance(SSOAuthenticator.class);
    private static final Logger log = Logger.getLogger(SSOAuthenticator.class);

    /*
    @Override
    protected Principal getUser(final String s) {
        return new Principal() {
            @Override
            public String getName() {
                return s;
            }
        };
    }

    @Override
    protected boolean authenticate(Principal principal, String s) throws AuthenticatorException {
        return false;
    }
    */

    public Principal getUser(HttpServletRequest request, HttpServletResponse response)
    {
        Principal user = null;

        //Principal user = request.getUserPrincipal();
        // userName = userPrincipal.getName();

        try
        {
            if(request.getSession() != null && request.getSession().getAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY) != null)
            {
                user = (Principal) request.getSession().getAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY);
                String username = user.getName();
                log.info("Session found; user " + username + " is already logged in");
            }
            else {
                user = request.getUserPrincipal();
                String username = user.getName();

                if (null != username) {
                    log.info("Logged in via getUserPrincipal, with User " + username);
                    request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY, user);
                    request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_OUT_KEY, null);
                }
                else
                {
                    if (null != request.getHeader("uid")) {
                        username = request.getHeader("uid");
                        log.info("Logged in via getHeader, with User " + username);
                        request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY, user);
                        request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_OUT_KEY, null);
                    } else {
                        user = new Principal() {
                            @Override
                            public String getName() {

                                return "dashap";
                            }
                        };
                        log.info("Logged in via dashap as last resort");
                        request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_IN_KEY, user);
                        request.getSession().setAttribute(JiraSeraphAuthenticator.LOGGED_OUT_KEY, null);
                    }
                }
            }
                /*
                else
                {
                    //log.info("SSOCookie is null; redirecting");
                    log.info("Remote user is null; redirecting");
                    //user was not found, or not currently valid
                    return null;
                }
                */
        }
        catch (Exception e) // catch class cast exceptions
        {
            log.warn("Exception: " + e, e);
        }
        return user;
    }
}

