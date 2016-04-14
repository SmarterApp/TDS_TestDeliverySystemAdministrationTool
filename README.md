# TDSAdmin

The TDS Administration tool allows authorized users to manage students’ test opportunities. This software provides a web interface that allows for the selection of opportunities by SSID and/or Session ID, and apply a variety of functions to those opportunities. These functions include Extend grace period, Extend expiration date, Reopen, Restore, Reset, Invalidate, and Change segment permeability of test opportunities.

## License ##
This project is licensed under the [AIR Open Source License v1.0](http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf).

## Getting Involved ##
We would be happy to receive feedback on its capabilities, problems, or future enhancements:

* For general questions or discussions, please use the [Forum](http://forum.opentestsystem.org/viewforum.php?f=9).
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
oauth.testreg.client={OAuth test client ID for test registration}
oauth.testreg.client.secret={OAuth client secret for test registration}
oauth.testreg.client.granttype={OAuth grant type for test registration}
oauth.testreg.username={OAuth username for test registration}
oauth.testreg.password={OAuth password for test registration} 

working example:
oauth.access.url=https://<openam-url>/auth/oauth2/access_token?realm=/your_realm
pm.oauth.client.id=pm
pm.oauth.client.secret=OAUTHCLIENTSECRET
pm.oauth.batch.account=test@example.com
pm.oauth.batch.password=<password>
oauth.testreg.client=testreg 
oauth.testreg.client.secret=<secret> 
oauth.testreg.client.granttype=password
oauth.testreg.username=testreg@example.org 
oauth.testreg.password=<password>
```
Add environment variable `-DSB11_CONFIG_DIR` to application server start up as shown in Tomcat (Run Configuration).

### Tomcat (Run Configuration)
Like other SBAC applications, TDSAdmin must be set up with active profiles and program management settings.

* `-Dspring.profiles.active`  - Active profiles should be comma-separated. Typical profiles for the `-Dspring.profiles.active` include:
	* `progman.client.impl.integration`  - Use the integrated program management
	* `progman.client.impl.null`  - Use the program management null implementation
	* `mna.client.integration`  - Use the integrated MnA component
	* `mna.client.null`  - Use the null MnA component
* `-Dprogman.baseUri`  - This URI is the base URI for where the Program Management REST module is deployed.
*  `-Dprogman.locator`  - The locator variable describes which combinations of name and environment (with optional overlay) should be loaded from Program Management.  For example: ```"component1-urls,dev"``` would look up the name component1-urls for the dev environment at the configured REST endpoint.  Multiple lookups can be performed by using a semicolon to delimit the pairs (or triplets with overlay): ```"component1-urls,dev;component1-other,dev"```
*  `-DSB11_CONFIG_DIR`  - Locator string needed to find the TDSAdmin properties to load.
*  `-Djavax.net.ssl.trustStore`  - Location of .jks file which contains security certificates for SSO, Program Management and Permissions URL specified inside the baseuri and Program Management configuration.
*  `-Djavax.net.ssl.trustStorePassword`  - Password string for the keystore.jks file.

```
 Example:
-Dspring.profiles.active=progman.client.impl.integration,mna.client.integration
-Dprogman.baseUri=http://<program-management-url>/rest/ 
-Dprogman.locator='tdsadmin,prod'
-DSB11_CONFIG_DIR=$CATALINA_HOME/resources
-Djavax.net.ssl.trustStore=<filesystem_dir>/saml_keystore.jks
-Djavax.net.ssl.trustStorePassword=xxxxxx
```

## Program Management Properties
Program Management properties need to be set for running TDSAdmin. Example TDSAdmin properties at /tdsadmin/docs/Installation/tdsadmin-progman-config.txt.

#### Database Properties
The following parameters need to be configured inside program management for database.

* `tdsadmin.datasource.url=jdbc:mysql://<url.to.db>:3306/schemaname?useUnicode=yes&characterEncoding=utf8`  - The JDBC URL of the database from which Connections can and should be acquired. useUnicode is required to store unicode characters into the database
* `datasource.username=<db-username>`  -  Username that will be used for the DataSource's default getConnection() method. 
* `encrypt:datasource.password=<db-password>`  - Password that will be used for the DataSource's default getConnection() method.
* `datasource.driverClassName=com.mysql.jdbc.Driver`  - The fully qualified class name of the JDBC driverClass that is expected to provide Connections.
* `datasource.minPoolSize=5`  - Minimum number of Connections a pool will maintain at any given time.
* `datasource.acquireIncrement=5`  - Determines how many connections at a time datasource will try to acquire when the pool is exhausted.
* `datasource.maxPoolSize=20`  - Maximum number of Connections a pool will maintain at any given time.
* `datasource.checkoutTimeout=60000`  - The number of milliseconds a client calling getConnection() will wait for a Connection to be checked-in or acquired when the pool is exhausted. Zero means wait indefinitely. Setting any positive value will cause the getConnection() call to timeout and break with an SQLException after the specified number of milliseconds.
* `datasource.maxConnectionAge=0`  - Seconds, effectively a time to live. A Connection older than maxConnectionAge will be destroyed and purged from the pool. This differs from maxIdleTime in that it refers to absolute age. Even a Connection which has not had much idle time will be purged from the pool if it exceeds maxConnectionAge. Zero means no maximum absolute age is enforced. 
* `datasource.acquireRetryAttempts=5`  - Defines how many times datasource will try to acquire a new Connection from the database before giving up. If this value is less than or equal to zero, datasource will keep trying to fetch a Connection indefinitely.
* `datasource.idleConnectionTestPeriod=14400`  - If this is a number greater than 0, Datasource will test all idle, pooled but unchecked-out connections, every this number of seconds.
* `datasource.testConnectionOnCheckout=false`  - If true, an operation will be performed at every connection checkout to verify that the connection is valid. 
* `datasource.testConnectionOnCheckin=false`  -  If true, an operation will be performed asynchronously at every connection checkin to verify that the connection is valid. 
* `datasource.numHelperThreads=3`  - c3p0 is very asynchronous. Slow JDBC operations are generally performed by helper threads that don't hold contended locks. Spreading these operations over multiple threads can significantly improve performance by allowing multiple operations to be performed simultaneously. 
* `datasource.maxStatements=20000`  - The size of c3p0's global PreparedStatement cache. If both maxStatements and maxStatementsPerConnection are zero, statement caching will not be enabled. If maxStatements is zero but maxStatementsPerConnection is a non-zero value, statement caching will be enabled, but no global limit will be enforced, only the per-connection maximum. maxStatements controls the total number of Statements cached, for all Connections. If set, it should be a fairly large number, as each pooled Connection requires its own, distinct flock of cached statements. As a guide, consider how many distinct PreparedStatements are used frequently in your application, and multiply that number by maxPoolSize to arrive at an appropriate value.
* `datasource.maxStatementsPerConnection=100`  - The number of PreparedStatements c3p0 will cache for a single pooled Connection. If both maxStatements and maxStatementsPerConnection are zero, statement caching will not be enabled. If maxStatementsPerConnection is zero but maxStatements is a non-zero value, statement caching will be enabled, and a global limit enforced, but otherwise no limit will be set on the number of cached statements for a single Connection. If set, maxStatementsPerConnection should be set to about the number distinct PreparedStatements that are used frequently in your application, plus two or three extra so infrequently statements don't force the more common cached statements to be culled. 

#### MNA properties
Following parameters need to be configured inside program management for MNA.	

* `mna.mnaUrl=http://<mna-context-url>/mna-rest/`  - URL of the Monitoring and Alerting client server's rest url
* `mnaServerName=tdsadmin`  -  Used by the mna clients to identify which server is sending the log/metrics/alerts.
* `mnaNodeName=prod`  - Used by the mna clients to identify who is sending the log/metrics/alerts. There is a discrete mnaServerName and a node in case say XXX for server name & node1/node2 in a clustered environment giving the ability to search across clustered nodes by server name or specifically for a given node. Itâ€™s being stored in the db for metric/log/alert, but not displayed.
* `mna.logger.level=INFO`  - Used to control what is logged to the Monitoring and Alerting system. Logging Level values include (ALL - Turn on all logging levels, TRACE, DEBUG, INFO, WARN, ERROR, OFF - Turn off logging). IMPORTANT: In order to maintain an audit log of activity in TDSAdmin, set this to no higher than `INFO`.


#### SSO properties
The following parameters need to be configured inside program management for SSO.	

* `permission.uri=https://<permission-app-context-url>/rest`  - The base URL of the REST api for the Permissions application.
* `tdsadmin.security.profile=prod`  - The name of the environment the application is running in. For a production deployment this will most likely be "prod. (it must match the profile name used to name metadata files).
* `component.name=TDS Admin`  - The name of the component that this tdsadmin deployment represents. This must match the name of the component in Program Management and the name of the component in the Permissions application.
* `tdsadmin.security.idp=https://<idp-url>`  - The URL of the SAML-based identity provider (OpenAM).
* `tdsadmin.webapp.saml.metadata.filename=proctor_sp.xml`  -  OpenAM Metadata file name uploaded for environment and placed inside server directory. 
* `tdsadmin.security.dir=file:////<sp-file-location-folder>`  - Location of the metadata file.
* `tdsadmin.security.saml.keystore.cert=<cert-name>`  - Name of the Keystore cert being used.
* `tdsadmin.security.saml.keystore.pass=<password>`  -  Password for keystore cert.
* `tdsadmin.security.saml.alias=proctor_webapp`  - Alias for identifying web application.
* `oauth.tsb.client=tsb`  - OAuth Client id configured in OAM to allow the SAML bearer workflow to convert a SAML assertion into an OAuth token for the "coordinated web service" call to TSB.
* `oauth.access.url=https://<oauth-url>`  - OAuth URL to OAM to allow the SAML bearer workflow to POST to get an OAuth token for any "machine to machine" calls requiring OAUTH
* `encrypt:oauth.tsb.client.secret=<password>`  - OAuth Client secret/password configured in OAM (under the client id) to allow the SAML bearer workflow to convert a SAML assertion into an OAuth token for the "coordinated web service" call to TSB.
* `encrypt:mna.oauth.client.secret=<password>`  -  OAuth Client secret/password configured in OAM to allow get an OAuth token for the "batch" web service call to MnA.
* `mna.oauth.client.id=mna`  - OAuth Client id configured in OAM to allow get an OAuth token for the "batch" web service call to MnA.
* `encrypt:tdsadmin.oauth.resource.client.secret=<password>`  - OAuth Client secret/password configured in OAM to allow get an OAuth token for the "batch" web service call to core standards.
* `tdsadmin.oauth.resource.client.id=tdsadmin`  - OAuth Client id configured in OAM to allow get an OAuth token for the "batch" web service call to core standards.
* `tdsadmin.oauth.checktoken.endpoint=http://<oauth-url>`  - OAuth URL to OAM to allow the SAML bearer workflow to perform a GET to check that an OAuth token is valid.

#### tdsadmin properties
The following parameters need to be configured inside program management for tdsadmin

* `tdsadmin.IsCheckinSite=false` 
* `tdsadmin.DONOT_Distributed=true` 
* `tdsadmin.ClientQueryString=false`  
* `tdsadmin.EncryptedPassword=true` 
* `tdsadmin.RecordSystemClient=true` 
* `tdsadmin.AdminPassword=SeCrEtPaSsWoRd` 
* `tdsadmin.SqlCommandTimeout=60`  
* `tdsadmin.SessionType=0`  - Type of the testing supported: 0 is online, 1 is paper-based.
* `tdsadmin.DBJndiName=java:/comp/env/jdbc/sessiondb` 
* `tdsadmin.TestRegistrationApplicationUrl=http://<url-to-art-app>:port/rest`  -  URL to ART Application REST context
* `tdsadmin.TDSSessionDBName=session`  - Name of the session schema
* `tdsadmin.Debug.AllowFTP=true` 
* `tdsadmin.StateCode=WI` 
* `tdsadmin.ClientName=SBAC`
* `tdsadmin.IsTrStubSession=true` 
* `logLatencyInterval=55` - Define the seconds of a minute when DB latency is being logged into database table.
* `logLatencyMaxTime=30000` - If any procedure call execution time exceeds the number of milliseconds specified here, It will be logged into the dblatency table of the database.
* `dbLockRetrySleepInterval=116` - Database connection will wait for number of milliseconds specified here before trying to acquire the exclusive resource lock on database again.
* `dbLockRetryAttemptMax=500` - If  database connection will not get the exclusive resource lock, It will retry number of times specified here.
* `EncryptionKey=testKey123456789123456789`  - Encryption key is used for encrypting the cookies and item file path. There is no default value set for this property. It must be set in program management. Minimum length of this key is 24 characters.


## SP Metadata file for SSO
Create metadata file for configuring the SSO. Sample SSO metadata file pointing to localhost is at /tdsadmin/docs/Installation/tdsadmin_local_sp.xml.
Change the entity id and URL according to the environment (local, dev, staging, prod, etc.). Upload this file to OpenAM and place this file inside server file system.
Specify `tdsadmin.webapp.saml.metadata.filename` and `tdsadmin.security.dir` in Program Management for metadata file name and location.
```
Example:
tdsadmin.webapp.saml.metadata.filename=tdsadmin_prod_sp.xml
tdsadmin.security.dir=file:////usr/securitydir
```
 
## Build Order
These are the steps that should be taken in order to build all of the Proctor related artifacts.

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