package org.ratpackframework.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppDeploySpec extends Specification {

    Project project = ProjectBuilder.builder().build()
    Task task

    void setup(){
        project.apply plugin: 'heroku'
        task = project.tasks.findByName('herokuAppDeploy')
    }

    void "should display a valid description" (){
        expect:
        task.description == "Deploy the application to Heroku."
    }

}
