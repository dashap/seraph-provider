seraph-provider
===============

Jira SSO Authenticator - works with Jira 6.x 

We use Apache as a ajp/proxy that was setup for sso and mod_ajp, so our url to Jira arrives from the Apache server with the remote user/principal.
In the Jira server.xml we uncommented the ajp section.  

Added to ajp connector in server.xml: 
 tomcatAuthentication="false"
 
 Copied sas folder to WEB-INF/classes/com folder.
 Modified seraph-config.xml:
 
 Commented out existing JiraSeraphAuthenticator line and added:
 
 <authenticator class="com.sas.mis.des.seraph.SSOAuthenticator"/>
Added our URLs for login.url, logout.url, and link.url that were provided by our openAM server administrators.

Examples:

<pre>
<param-name>login.url</param-name>
<param-value>https://swwlogin.sas.com/opensso/UI/Login?realm=/sww&amp;goto=${originalurl};</param-value>

# Hint: use \&amp; instead of &...
<param-name>link.login.url</param-name>
<param-value>https://swwlogin.sas.com/opensso/UI/Login?realm=/sww&amp;goto=${originalurl};</param-value>

<param-name>logout.url</param-name>
<param-value>http://sww.sas.com/logout</param-value>
</pre>

The only issue I see so far is I do not get prompted to go into Administrator mode anymore. I went back to the old way of having a
non-Administrator login for the times I need to work as a regular user.
