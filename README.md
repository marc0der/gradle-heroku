##Heroku Plugin for Gradle

This Gradle plugin allows for the administration and deployment of your Heroku
application from the comfort of your Gradle project.

It allows you to create, destroy or deploy apps.
It also allows you to view the application info or get a list of apps.

In order to use the plugin, update your `build.gradle` file with the following config:

    buildscript {
        repositories {
            ...
            mavenCentral()
            mavenRepo name: 'Bintray Gradle Plugins', url: 'http://dl.bintray.com/vermeulen-mp/gradle-plugins'
        }
        dependencies {
            classpath "org.gradle.api.plugins:gradle-heroku:0.9.2"
        }
    }


    apply plugin: 'heroku'

    ...

    heroku {
        appName = 'some-unique-app-name'

        //get this from heroku
        apiKey = 'my-api-key'
    }

The default buildpack used on app creation will be the one found at: `https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git`

Overriding this default is as easy as adding an additional line to the heroku block:

    heroku {
        ...
        buildpack = 'https://github.com/someaccount/heroku-buildpack-xxx.git'
        ...
    }

Next, you will need to add a `Procfile` to the root of your project.
It contains the following, including a crucial line that bootstraps your application, in this case a fat jar with java.

    ---
    default_process_types:
      web: java -jar -Dport=$PORT build/libs/my-fat.jar

We can now invoke the following tasks:

    # provision the named app on heroku
    $ gradle herokuAppCreate

    # deploy the named app on heroku
    $ gradle herokuAppDeploy

    # destroy the named app on heroku
    $ gradle herokuAppDestroy

    # get app info of the app on heroku
    $ gradle herokuAppInfo

    # get list of apps on heroku
    $ gradle herokuAppList

If you really really need to:

    # install the buildpack artifacts locally into the project
    $ gradle herokuBuildpack

