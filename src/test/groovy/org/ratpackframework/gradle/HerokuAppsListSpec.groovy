package org.ratpackframework.gradle

import com.heroku.api.HerokuAPI
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppsListSpec extends Specification {

    final APP_LIST_TASK_NAME = 'herokuAppList'

    Project project
    HerokuAppList task
    HerokuAPI herokuAPI = Mock()

    void setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'heroku'
        task = project.tasks.findByName(APP_LIST_TASK_NAME)
        task.herokuAPI = herokuAPI
    }

    void "should add the app list task to the project"() {
        expect:
        task
    }

    void "should declare a valid description"() {
        expect:
        task.description == "Lists all Apps available for the current user on Heroku."
    }


}
