seraph-provider
===============

Jira SSO Authenticator using Apache and not using cookie elements in the authenticator to allow Apache to deal with it instead - works with Jira 6.x 

We use Apache as a ajp/proxy that was setup for sso and mod_ajp, so our url to Jira arrives from the Apache server with the remote user/principal.  In apache we excluded jira from basic authentication via a LocationMatch check and added following stanza that proxies over to the Jira server:

<pre>
 #JIRA AJP Proxy config
  ProxyPreserveHost On
  &lt;Location /apps/jira&gt;
    Satisfy Any
    Allow from all
    RewriteEngine On
    RewriteCond %{HTTP_HOST} !yourapacheserver.com
    RewriteRule (.*) http://yourapacheserver.com%{REQUEST_URI} [R=307]
    ProxyPass ajp://yourjiraserver.com:8009/apps/jira                   # in server.xml we set path="/apps/jira"
    ProxyPassReverse ajp://yourjiraserver.com:8009/apps/jira
  &lt;/Location&gt;
</pre>

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
&lt;param-name&gt;login.url&lt;/param-name&gt;
&lt;param-value&gt;https://youropenamserver.com/opensso/UI/Login?realm=/sww&amp;goto=${originalurl};&lt;/param-value&gt;

# Hint: we had to use '&amp;' instead of & with goto= since xml does not like ampersands...
&lt;param-name&gt;link.login.url&lt;/param-name&gt;
&lt;param-value&gt;https://youropenamserver.com/opensso/UI/Login?realm=/sww&amp;goto=${originalurl};&lt;/param-value&gt;

&lt;param-name&gt;logout.url&lt;/param-name&gt;
&lt;param-value&gt;http://youropenamserver.com/logout&lt;/param-value&gt;
</pre>

<h2>Known Issues</h2>
The only issue I see so far is I do not get prompted to go into Administrator mode anymore. I went back to the old way of having a
non-Administrator login for the times I need to work as a regular user.

<h2>Logging</h2>Added to Jira's WEB-INF/classes/log4j.properties: log4j.logger.com.sas.mis.des.seraph.SSOAuthenticator = INFO, console, filelog log4j.additivity.com.sas.mis.des.seraph.SSOAuthenticator = false

