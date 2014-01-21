package org.ratpackframework.gradle

import com.heroku.api.App
import com.heroku.api.Heroku
import com.heroku.api.HerokuAPI
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppCreateSpec extends Specification {

    final CREATE_APP_TASK_NAME = 'herokuAppCreate'

    Project project
    HerokuAppCreateTask task

    def herokuAPI = Mock(HerokuAPI)

    def setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'heroku'

        task = project.tasks.findByName(CREATE_APP_TASK_NAME)
        task.herokuAPI = herokuAPI
    }

    void "should add the app create task to the project"() {
        expect:
        task
    }

    void "should declare a valid description"(){
        expect:
        task.description == 'Creates a new application on Heroku.'
    }

    void "should create an unnamed app when name is not provided"() {
        given:
        def cedar = Heroku.Stack.Cedar
        def app = new App().on(cedar)

        when:
        task.execute([:])

        then:
        1 * herokuAPI.createApp(_) >> app
        ! app.name
        app.stack == cedar
    }

    void "should create a named app when name is provided"() {
        given:
        def appName = "fast-everglades-6675"
        def cedar = Heroku.Stack.Cedar
        def app = new App().named(appName).on(cedar)

        when:
        task.execute([appName: appName])

        then:
        1 * herokuAPI.createApp(_) >> app
        app.name == appName
        app.stack == cedar
    }

}
