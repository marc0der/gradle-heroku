##Heroku Plugin for Gradle

This Gradle plugin allows for the administration and deployment of your Heroku
application from the comfort of your Gradle project.

It allows you to install local buildpacks, provision, destroy or deploy an instance and view the application info.

In order to use the plugin, update your `build.gradle` file with the following config:

	buildscript {
		repositories {
			...
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

		//for standalone gradlew project
		buildpack = 'https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git'
    }


After doing so, we can now invoke the following tasks:

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

