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
        expect:
        project.heroku instanceof HerokuConfig
        project.heroku.buildpack
        project.heroku.appName
        project.heroku.apiKey
    }

    void "should initialise the heroku buildpack task"() {
        expect:
        project.tasks.findByName 'heroku'
    }

    void "should initialise the heroku create app task"() {
        expect:
        project.tasks.findByName 'herokuCreateApp'
    }

    void "should initialise the heroku destroy app task"() {
        expect:
        project.tasks.findByName 'herokuDestroyApp'
    }

}
