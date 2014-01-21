package org.ratpackframework.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuPluginSpec extends Specification {

    final PLUGIN_NAME = 'heroku'

    Project project
    Plugin plugin

    void setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: PLUGIN_NAME
    }

    void "should initialise the heroku config"() {
        given:
        def buildpack = 'http://www.example.org/buildpack'
        def appName = 'fast-everglades-6675'
        def apiKey = 'f19d6641b2891287e1a8a429aa51d8baa1415ac2'

        when:
        project.heroku.buildpack = buildpack
        project.heroku.appName = appName
        project.heroku.apiKey = apiKey

        then:
        project.heroku instanceof HerokuConfig
        project.heroku.buildpack
        project.heroku.appName
        project.heroku.apiKey
    }

    void "should initialise the heroku buildpack task"() {
        expect:
        project.tasks.findByName 'herokuBuildpack'
    }

    void "should initialise the heroku create app task"() {
        expect:
        project.tasks.findByName 'herokuAppCreate'
    }

    void "should initialise the heroku destroy app task"() {
        expect:
        project.tasks.findByName 'herokuDestroyApp'
    }

}
