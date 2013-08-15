##Heroku Plugin for Gradle

This Gradle plugin allows for the administration and deployment of your Heroku
application from the comfort of your Gradle project.

It allows you to install local buildpacks, provision or destroy a new instance and view the application info.

In order to use the plugin, update your `build.gradle` file with the following config:

    apply plugin: 'groovy'

    ...

    heroku {
    	appName = 'some-unique-app-name'
    	apiKey = 'my-api-key'

    	//for a ratpack application
    	buildpack = 'http://dl.bintray.com/vermeulen-mp/heroku-buildpack-ratpack/heroku-buildpack-ratpack.zip'
    }


After doing so, we can now invoke the following tasks:

    # install the buildpack artifacts locally into the project
    $ gradle herokuBuildpack

    # provision the named app on heroku
    $ gradle herokuCreateApp

    # destroy the named app on heroku
    $ gradle herokuDestroyApp


More features will be added in time to come...


