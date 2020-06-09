# Coaching Engine ReadMe
The Coaching Engine is a module for the Agents United platform. It allows developers to build a module that chooses a conversation topic that is most relevant to be discussed given the availble information.
The topic selection algorithm will make this choice based on a topic structure - a tree of topic nodes - and selection parameters that can be added to the topic nodes in that structure. 

**N.B. Wool Web Service needed.**

The Coaching Engine needs access to e.g. an instance of WOOL Web Service (see www.woolplatform.eu) since it retrieves the values for the selection parameters from the Wool Web Service. 

## Install and run the Coaching Engine

### Requirements

#### JAVA

The web service was tested with OpenJDK 11.

https://jdk.java.net/archive/

Download the ZIP file and extract it to a location on your hard disk. For
example:
  C:\apps\jdk-11
Then set environment variable JAVA_HOME. For example:
  C:\apps\jdk-11
And add the bin directory to your path. For example:
  C:\apps\jdk-11\bin

You should now be able to run Java from the command prompt. For example:
> java -version

#### TOMCAT

The web service was tested with Tomcat 8.5.

http://tomcat.apache.org/download-80.cgi

Download the 32-bit/64-bit Windows Service Installer of Tomcat 8.5.
Run the installer and check the following pages:
Choose Components:
- You may want to add Tomcat > Service Startup so Tomcat is started
  automatically when the computer is started.
- Add "Host Manager". This is needed to deploy web applications from the Gradle
  build file.
Configuration:
- At Tomcat Administrator Login fill in a user name (for example "admin") and
  password.
- Set "Roles" to: admin-gui,manager-gui,admin-script,manager-script
  The script roles are also needed for deployment from the build file.
Java Virtual Machine:
- Select the directory to the JDK. For example:
  C:\apps\jdk-11

Configuration of the JDK location, Java options and memory size:
- From the Windows start menu, open Monitor Tomcat. If you get a permission
  error, you may need to open the Tomcat folder in Windows Explorer first:
  C:\Program Files\Apache Software Foundation\Tomcat 8.5
  The Tomcat monitor opens in the notification area of the task bar.
- Open the Tomcat monitor from the task bar.
- Open the tab Java.

You should now be able to open the Tomcat manager at:
http://localhost:8080/


### CONFIGURATION

The web service is configured with this file that needs to be created:
<GITDIR>\CoachingEngine\gradle.properties

You can make a copy of this file and change it:
<GITDIR>\CoachingEngine\gradle.sample.properties

### DEPLOY

Make sure that Tomcat is running.

The web service is deployed with Gradle task cargoRedeployRemote. It requires
the following properties that you need to fill in gradle.properties:
- tomcatDeployPath
- remoteTomcat*

Open a command prompt in:
  <GITDIR>\CoachingEngine
Enter this command:
> .\gradlew build cargoRedeployRemote

If you want to make a clean build and deploy, then enter:
> .\gradlew clean build cargoRedeployRemote

After deploying you can access the Swagger interface at:
http://localhost:8080/servlets/coaching-engine

### USAGE

Also see the Swagger interface that should be available (after deployment) at:
http://localhost:8080/servlets/coaching-engine

First, retrieve an auth token from the Wool Web Service with POST /auth/login. The request body should be a JSON
object like this:

`{
  "user": "user@example.com",
  "password": "p4ssw0rd",
  "tokenExpiration": 1440
}`

The "user" is case-insensitive.
The "tokenExpiration" is optional. It can be a value in minutes or "never".
The default is 1440 minutes (24 hours).

The response is a JSON object like this:

`{
  "user": "user@example.com",
  "token": "eyJhbGciOiJI..."
}`

The "user" in the response may have different case than the case-insensitive
"user" in the request.

Include this "user" and "token" in your calls to the Coaching Engine endpoints. 

## Defining a new topic structure and selection parameters

-- To be added.--

## LICENSE

The Coaching Engine (CE) is licensed under the GNU Lesser General Public License v3.0 (LGPL 3.0).
