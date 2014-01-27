package org.ratpackframework.gradle
import com.heroku.api.App
import com.heroku.api.HerokuAPI
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppListSpec extends Specification {

    final APP_LIST_TASK_NAME = 'herokuAppList'

    Project project
    HerokuAppListTask task
    def herokuAPI = Mock(HerokuAPI)

    void setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'heroku'

        task = project.tasks.findByName(APP_LIST_TASK_NAME)
        task.setHerokuAPI(herokuAPI)
    }

    void "should declare a valid description"() {
        expect:
        task.description == "Lists all Apps available for the current user on Heroku."
    }

    void "should retrieve a list of apps"() {
        given:
        def app = new App()
        def appList = [app]

        when:
        task.execute([:])

        then:
        1 * herokuAPI.listApps() >> appList
    }


}
