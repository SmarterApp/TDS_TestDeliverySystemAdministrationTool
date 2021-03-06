# Welcome to the TDS Administration Tool (TDSAdmin)

The TDS Administration tool allows authorized users to manage students’ test opportunities. This software provides a web interface that allows for the selection of opportunities by SSID and/or Session ID, and apply a variety of functions to those opportunities. These functions include Extend grace period, Extend expiration date, Reopen, Restore, Reset, Invalidate, and Change segment permeability of test opportunities.

## License ##
This project is licensed under the [AIR Open Source License v1.0](http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf).

## Getting Involved ##
We would be happy to receive feedback on its capabilities, problems, or future enhancements:

* For general questions or discussions, please use the [Forum](http://forum.smarterbalanced.org/viewforum.php?f=9).
* Feel free to **Fork** this project and develop your changes!

## Module Overview

### tdsadmin
The tdsadmin module contains the tdsadmin UI and REST APIs.

## Setup
In general, building the code and deploying the WAR file is a good first step.  The TDSAdmin application, however, has a number of other steps that need to be performed in order to fully setup the system.

### Config Folder
Within the file system of the deployment (local file system if running locally or within Tomcat file directories), create a configuration folder structure as follows:
```
{CONFIG-FOLDER-NAME}/progman/
example: /my-app-config/progman/
``` 
Within the deepest folder ('/progman/'), place a file named 'pm-client-security.properties' with the following contents:

```
#security props
oauth.access.url={the URL of OAuth2 access token provider}
pm.oauth.client.id={Client ID for program management client, can be shared amongst all client users or application/consumer specific values}
pm.oauth.client.secret={Password for program management client, can be shared amongst all client users or application/consumer specific values}
pm.oauth.batch.account={Account name or email for OAuth2 batch}
pm.oauth.batch.password={OAuth2 batch password}

working example:
oauth.access.url=https://<openam-url>/auth/oauth2/access_token?realm=/your_realm
pm.oauth.client.id=pm
pm.oauth.client.secret=OAUTHCLIENTSECRET
pm.oauth.batch.account=test@example.com
pm.oauth.batch.password=<password>
```
Add environment variable `-DSB11_CONFIG_DIR` to application server start up as shown in Tomcat (Run Configuration).

### Tomcat (Run Configuration)
Like other SBAC applications, TDSAdmin must be set up with active profiles and Program Management settings.

* `-Dspring.profiles.active`  - Active profiles should be comma-separated. Typical profiles for the `-Dspring.profiles.active` include:
	* `progman.client.impl.integration`  - Use the integrated program management
	* `progman.client.impl.null`  - Use the program management null implementation
	* `mna.client.integration`  - Use the integrated MnA component
	* `mna.client.null`  - Use the null MnA component
* `-Dprogman.baseUri`  - This URI is the base URI for where the Program Management REST module is deployed.
*  `-Dprogman.locator`  - The locator variable describes which combinations of name and environment (with optional overlay) should be loaded from Program Management.  For example: ```"component1-urls,dev"``` would look up the name component1-urls for the dev environment at the configured REST endpoint.  Multiple lookups can be performed by using a semicolon to delimit the pairs (or triplets with overlay): ```"component1-urls,dev;component1-other,dev"```
*  `-DSB11_CONFIG_DIR`  - Locator string needed to find the TDSAdmin properties to load. (Directory containing 'progman' folder)
*  `-Djavax.net.ssl.trustStore`  - Location of .jks file which contains security certificates for SSO, Program Management and Permissions URL specified inside the baseuri and Program Management configuration.
*  `-Djavax.net.ssl.trustStorePassword`  - Password string for the keystore.jks file.

```
 Example:
-Dspring.profiles.active=progman.client.impl.integration,mna.client.integration
-Dprogman.baseUri=http://<program-management-url>/rest/ 
-Dprogman.locator='tdsadmin,prod'
-DSB11_CONFIG_DIR=$CATALINA_HOME/{CONFIG-FOLDER-NAME}
-Djavax.net.ssl.trustStore=<filesystem_dir>/saml_keystore.jks
-Djavax.net.ssl.trustStorePassword=xxxxxx
```

## Program Management Properties
Program Management properties need to be set for running the TDS Admin app. Example TDS Admin properties inside the docs/Installation/tdsadmin-progman-config.txt file. All Program Management properties defined below belong in a single TDS Admin configuration.

#### Database Properties
The following parameters need to be added into a Program Management TDSAdmin configuration for database access:

* `tdsadmin.datasource.url=jdbc:mysql://<url.to.db>:3306/schemaname?useUnicode=true&characterEncoding=utf8&useFastDateParsing=false` - The JDBC URL of the database from which connections can and should be acquired. useUnicode is required to store unicode characters into the database. Turning off useFastDateParsing allows the column values of DateTime type to be read, given the underlying database is MySQL.
* `datasource.username=<db-username>`  -  Username that will be used for the DataSource's default getConnection() method. 
* `datasource.password=<db-password>`  - Password that will be used for the DataSource's default getConnection() method.
* `datasource.driverClassName=com.mysql.jdbc.Driver`  - The fully qualified class name of the JDBC driverClass that is expected to provide Connections.
* `datasource.minPoolSize=5`  - Minimum number of Connections a pool will maintain at any given time.
* `datasource.acquireIncrement=5`  - Determines how many connections at a time datasource will try to acquire when the pool is exhausted.
* `datasource.maxPoolSize=20`  - Maximum number of Connections a pool will maintain at any given time.
* `datasource.checkoutTimeout=60000`  - The number of milliseconds a client calling getConnection() will wait for a Connection to be checked-in or acquired when the pool is exhausted. Zero means wait indefinitely. Setting any positive value will cause the getConnection() call to timeout and break with an SQLException after the specified number of milliseconds.
* `datasource.maxConnectionAge=0`  - Seconds, effectively a time to live. A Connection older than maxConnectionAge will be destroyed and purged from the pool. This differs from maxIdleTime in that it refers to absolute age. Even a Connection which has not had much idle time will be purged from the pool if it exceeds maxConnectionAge. Zero means no maximum absolute age is enforced. 
* `datasource.acquireRetryAttempts=5`  - Defines how many times datasource will try to acquire a new Connection from the database before giving up. If this value is less than or equal to zero, datasource will keep trying to fetch a Connection indefinitely.
* `datasource.idleConnectionTestPeriod=14400`  - If this is a number greater than 0, Datasource will test all idle, pooled but unchecked-out connections, every this number of seconds.

#### MNA properties
The following parameters need to be added into a Program Management TDSAdmin configuration for integration with MNA:

* `mna.mnaUrl=http://<mna-context-url>/rest/`  - URL of the Monitoring and Alerting client server's REST context.
* `mnaServerName=tdsadmin`  -  Used by the MNA clients to identify which server is sending the log/metrics/alerts.
* `mnaNodeName=prod`  - Used by the MNA clients to identify who is sending the log/metrics/alerts. There is a discrete mnaServerName and an mnaNodeName to provide the ability to search across clustered nodes by server name or specifically for a given node. It's being stored in the db for metric/log/alert, but not displayed.
* `mna.logger.level=INFO`  - Used to control what is logged to the Monitoring and Alerting system. Logging Level values include (ALL - Turn on all logging levels, TRACE, DEBUG, INFO, WARN, ERROR, OFF - Turn off logging). IMPORTANT: The audit log of TDSAdmin activity is provided at the `INFO` level. Setting this value to anything higher will prevent successful actions from being logged in MNA.
* `mna.oauth.batch.account=<mna-client-user>`
* `mna.oauth.batch.password=<password>`
* `mna.oauth.client.id=mna`  - OAuth Client id configured in OAM to allow get an OAuth token for the "batch" web service call to MnA.
* `mna.oauth.client.secret=<password>`  -  OAuth Client secret/password configured in OAM to allow get an OAuth token for the "batch" web service call to MnA.

#### Permissions properties
The following parameters need to be added into a Program Management TDSAdmin configuration for integration with Permissions:

* `permission.uri=https://<permission-app-context-url>/rest`  - The base URL of the REST API for the Permissions application.
* `component.name=TDS Admin`  - The name of the component that this tdsadmin deployment represents. This must match the name of the component in Program Management and the name of the component in the Permissions application.

#### SSO properties
The following parameters need to be added into a Program Management TDSAdmin configuration for integration with SSO:

* `tdsadmin.security.profile=prod`  - The name of the environment the application is running in. For a production deployment this will most likely be "prod. (it must match the profile name used to name metadata files).
* `tdsadmin.security.idp=https://<idp-url>`  - The URL of the SAML-based identity provider (OpenAM).
* `tdsadmin.webapp.saml.metadata.filename=tdsadmin_prod_sp.xml`  -  OpenAM Metadata file name uploaded for environment and placed inside server directory. 
* `tdsadmin.security.dir=file:/path/to/<sp-file-location-folder>`  - Location of the metadata file.
* `tdsadmin.security.saml.keystore.cert=<cert-name>`  - Name of the Keystore cert being used.
* `tdsadmin.security.saml.keystore.pass=<password>`  -  Password for keystore cert.
* `tdsadmin.security.saml.alias=tdsadmin_webapp`  - Alias for identifying web application.
* `oauth.access.url=https://<oauth-url>`  - OAuth URL to OAM to allow the SAML bearer workflow to POST to get an OAuth token for any "machine to machine" calls requiring OAUTH
* `tdsadmin.oauth.resource.client.secret=<password>`  - OAuth Client secret/password configured in OAM to allow get an OAuth token for the "batch" web service call to core standards.
* `tdsadmin.oauth.resource.client.id=tdsadmin`  - OAuth Client id configured in OAM to allow get an OAuth token for the "batch" web service call to core standards.
* `tdsadmin.oauth.checktoken.endpoint=http://<oauth-url>`  - OAuth URL to OAM to allow the SAML bearer workflow to perform a GET to check that an OAuth token is valid.


#### ART (testreg) properties
The following parameters need to be added into a Program Management TDSAdmin configuration for accessing the ART API:

* `oauth.testreg.client.id=art` - OAuth test client ID for ART
* `oauth.testreg.client.secret=<client-secret>` - OAuth client secret for ART
* `oauth.testreg.client.granttype=password` - OAuth grant type for ART, should be password
* `oauth.testreg.username=<email>` - OAuth username for test registration
* `oauth.testreg.password=<password>` - OAuth password for test registration

#### TDS Admin properties
The following parameters need to be added into a Program Management TDSAdmin configuration for TDS Admin itself to function:

* `tdsadmin.sessionTimeoutMinutes=15` - Number of minutes before the login session times out. Default is 15. A negative value here will force no timeout.
* `tdsadmin.TestRegistrationApplicationUrl=http://<url-to-art-app>:port/rest`  -  URL to ART Application REST context
* `tdsadmin.TDSSessionDBName=session`  - Name of the TDS session schema


## SP Metadata file for SSO
Create metadata file for configuring the SSO. Directions for creating an SP (service provider) Metadata file are available at http://docs.spring.io/spring-security-saml/docs/current/reference/html/configuration-metadata.html. Change the entity id and URL according to the environment (local, dev, staging, prod, etc.). Upload this file to OpenAM and place this file inside the server's file system.
Specify `tdsadmin.webapp.saml.metadata.filename` and `tdsadmin.security.dir` in Program Management for metadata file name and location.
```
Example:
tdsadmin.webapp.saml.metadata.filename=tdsadmin_prod_sp.xml
tdsadmin.security.dir=file:/path/to/usr/securitydir
```
 
## Build Order
These are the steps that should be taken in order to build all of the TDSAdmin related artifacts.

### Pre-Dependencies
* Tomcat 6 or higher
* Maven (mvn) version 3.X or higher installed
* Java 7
* Access to sharedmultijardev repository
* Access to tdsdlldev repository
* Access to sb11-shared-build repository
* Access to sb11-shared-code repository
* Access to sb11-security repository
* Access to sb11-program-management repository
* Access to sb11-monitoring-alerting-client repository

### Build order

If building all components from scratch the following build order is needed:

* sharedmultijar
* tdsdll
* SharedBuild
* SharedCode
* MonitoringAndAlertingClient
* ProgramManagementClient
* tdsadmin

## Dependencies
TDSAdmin has a number of direct dependencies that are necessary for it to function.  These dependencies are already built into the Maven POM files.

### Compile Time Dependencies
* sb11-shared-security
* spring-security-core
* xercesImpl
* prog-mgmnt-client
* prog-mgmnt-client-null-impl
* monitoring-alerting.client-null-impl
* monitoring-alerting.client
* sb11-shared-code
* c3p0
* shared-db
* tds-dll-api
* tds-dll-mysql
* myfaces-impl
* primefaces
* spring-context
* spring-jdbc
* mysql-connector-java
* spring-webmvc
* slf4j-api
* javax.inject
* servlet-api
* jsp-api

### Test Dependencies
* hamcrest-all
* junit
* mockito-core
* json-path-assert
* spring-test

### Runtime Dependencies
* jcl-over-slf4j