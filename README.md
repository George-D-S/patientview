patientview
===========


Building a deployable WAR file & setting up a database
======================================================

- remove the submodule for filters that points to git@git.solidstategroup.com:patient_view_filters.git in the .gitmodules file
- create a <filter>src/main/filters/${env}-filters.properties</filter> for your profile
- install the db using patientview/src/sql/mysql/patientview.sql
- run the maven build command:

`mvn clean package -P<profile-name>`

The war file will be built to the maven target directory.


Running locally using Intellij Idea
===================================

- Setup a maven run configuration with the following goals:

`clean compile war:inplace tomcat7:run`

- Select the localhost profile

- Supply the following VM parameters to the run configuration runner tab to allocate enough memory and to allow the JSPs to compile:

`-Xmx512m -XX:MaxPermSize=128m -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false`

This will clear down any temporary files (as specified by the maven clean plugin and .gitignore file).
Build the exploded war over the main/src/webapp directory.
Starts an embedded Tomcat 7 server and runs the webapp.

NOTE: The build is still dependent of Tomcat due to datasource and configuration so you cannot use Jetty.


Debugging JSPs using Intellij Idea
==================================

- Not possible using the embedded tomcat maven plugin
- Build the webapp using maven:

`clean compile war:inplace`

- Create a local Tomcat 7 run configuration and run the webapp directory as an exploded war artifact.
- Make sure you turn off any "before launch" options so not to interfere with the maven output.


JPA annotations in Intellij Idea
================================

- From the settings menu, go to Compiler - Annotation Processors
- Enable annotation processing
- Add an annotation processor: org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor
- Turn on processing for the patientview module: generated sources directory name: target/generated-sources
- Make sure you have set the generated-sources directory to be on the classpath for the project

Notes on using Git submodules
=============================

http://chrisjean.com/2009/04/20/git-submodules-adding-using-removing-and-updating/

`git submodule add git@mygithost:billboard lib/billboard`

`git submodule init`

`git submodule update`

I've found it necessary to perform a git pull when in the directory of the submodule to update the submodule files locally.

`git submodule rm lib/billboard`


Notes on using Git submodules in Jenkins
========================================

To update submodules when building setup this execute shell pre step:

`git submodule foreach git checkout master`
`git submodule foreach git pull`


Notes on Multi-tenant
=====================

Logging in
==========

- Users land at www.patientview.org
- Tenancy generic landing page invites uses to login
- We will extract user login details out of the user table into a tenant schema
- The spring security success handler will check the user's tenancies.  Single tenancy uses will be forwarded directly to the tenancy landing page,  e.g. www.patientview.org/rpv
- Multi-tenant users will hit www.patientview.org/launchpad which will allow them to select a tenancy to be redirected to.
- Should the user have no tenancy the user will not even be able to log in

Using a tenancy
===============

- Once the user arrives at a tenancy their session will be noted as associated with that tenancy.  We will have new methods to get the current user role for the tenancy.
- User can change tenancy via the tenancy switcher or by logging out.
- Should the user try to manually change their URL to view data from a separate tenancy they will either get a not authorised error HTTP 403 (if the user doesn't have a role for that tenancy) or they will get a HTTP 404 response.
- All secure links/URLs in the application must be prefixed by the tenancy context, e.g. www.patientview.org/rpv/patient/patient_details.do
- Each tenancy will have a spring security configuration to secure URLs with roles, e.g. /rpv/* requires ROLE_RPV_PATIENT or RPV_ROLE_UNITADMIN
- The links in the templates will be directed to the tenancy context automatically using PatientViewLinkTag that overrides via `<html:link>`

The tenancy servlet filter
==========================

- We will implement a custom tenancy servlet filter that appears after the spring security filter in the stack.
- This will create a virtual tenancy context without need for rewriting anything in struts or the JSPs
- The filter will strip off tenancy context and forward down the filter chain
- It will NOT check the tenancy requested matches the tenancy selected in the user's session to control tenancy switching.
- This filter has no responsibility for security - that is ALL handled by spring security.


Enhancements to the spring security configuration
=================================================

- Requests that don't start with a valid tenancy context will need to be dropped and considered an attempt to bypass spring security
- The following should pass through:
- /**/*.css
- /**/*.js
- /images/**/*
- /login.jsp
- /newsView.do?id=xyz
- /disclaimer.do
- /help.do
- /index.do
- /infoLinks.do

Securing features per tenancy
=============================

- There will be no security to stop users accessing "hidden" tenancy specific features
- Templates will have tenancy specific content by extending the PatientViewPresentTag to allow conditional templates e.g. `<logic:present tenancy="rpv">`

Writing JSP templates
=====================

- CSS, there will be a common base CSS for all tenancies, if necessary we can have custom .css files for each tenancies.
- The application will not supply different HTML markup per tenancy.

Forum tenancy filtering
=======================

- Todo

Administration Area
====================
- Superadmin users can now be setup per tenancy.  A per tenancy superadmin implements the "specialityadmin" role described in the spec.

Tasks for upgrading to JPA
==========================

- Refactor Hibernate code from Utils and Action classes into Manager and Dao interfaces
- Move all classes managed by JPA into model package
- Replace hibernate xml mappings with JPA annotations
- Add id pk columns to tables
- Review service level transactions are sufficient
- Write test cases for each new Dao and Manager method

- Review patients - it mixes JDBC and hibernate
- Review how adding a PK to model classes will break all the forms - in particular the edit screens