package com.wiredforcode

import com.heroku.api.HerokuAPI
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppDestroySpec extends Specification {

    final DESTROY_APP_TASK_NAME = 'herokuAppDestroy'

    Project project
    HerokuAppDestroyTask task

    def herokuAPI = Mock(HerokuAPI)

    void setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'heroku'

        task = project.tasks.findByName(DESTROY_APP_TASK_NAME)
        task.herokuAPI = herokuAPI
    }

    void "should declare a valid description"(){
        expect:
        task.description == 'Destroy the application on Heroku.'
    }

    void "should destroy an application on name provided"() {
        given:
        def appName = "fast-everglades-6675"

        when:
        task.execute([appName: appName])

        then:
        1 * herokuAPI.destroyApp(appName)
    }

    void "should not destroy an application on no name provided"() {
        when:
        task.execute([:])

        then:
        herokuAPI.destroyApp(_) >> { new GradleException('kapow!') }
        thrown(GradleException)
    }

}
