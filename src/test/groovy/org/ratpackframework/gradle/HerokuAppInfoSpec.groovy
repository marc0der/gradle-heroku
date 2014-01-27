package org.ratpackframework.gradle

import com.heroku.api.App
import com.heroku.api.Domain
import com.heroku.api.HerokuAPI
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppInfoSpec extends Specification {

    final APP_INFO_TASK_NAME = 'herokuAppInfo'
    final APP_NAME = "fast-everglades-6675"

    Project project
    HerokuAppInfoTask task
    def herokuAPI = Mock(HerokuAPI)
    def app = Mock(App)
    def command = [appName: APP_NAME]

    void setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'heroku'

        task = project.tasks.findByName(APP_INFO_TASK_NAME)
        task.herokuAPI = herokuAPI
    }

    void "should declare a valid description"(){
        expect:
        task.description == 'Displays comprehensive information about the named application.'
    }

    void "should retrieve the application from the heroku api"() {
        when:
        task.execute(command)

        then:
        1 * herokuAPI.getApp(APP_NAME) >> app
    }

    void "should get the app name from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getName() >> APP_NAME
    }

    void "should get the app domain from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getDomain() >> new Domain(domain: "fast-everglades-6675.herokuapp.com")
    }

    void "should get the app stack from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getStack() >> com.heroku.api.Heroku.Stack.Cedar
    }

    void "should get the requested app stack from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getRequestedStack() >> "cedar"
    }

    void "should get the created at date from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getCreatedAt() >> "2013/08/17 07:18:37 -0700"
    }

    void "should get the create status from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getCreateStatus() >> "complete"
    }

    void "should get the released at date from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getReleasedAt() >> "2013/08/17 07:18:38 -0700"
    }

    void "should get the buildpack description from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getBuildpackProvidedDescription() >> "Ratpack buildpack"
    }

    void "should get the git repo url from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getGitUrl() >> "git@heroku.com:fast-everglades-6675.git"
    }

    void "should get the repo migration status from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getRepoMigrateStatus() >> "complete"
    }

    void "should get the web url from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getWebUrl() >> "http://fast-everglades-6675.herokuapp.com/"
    }

    void "should get the owner email from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getOwnerEmail() >> "some@dude.com"
    }

    void "should get the number of dynos from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getDynos() >> 1
    }

    void "should get the number of workers from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getWorkers() >> 1
    }

    void "should get the slug size from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getSlugSize() >> 10000
    }

    void "should get the repo size from heroku"() {
        when:
        task.execute(command)

        then:
        herokuAPI.getApp(APP_NAME) >> app
        1 * app.getRepoSize() >> 5000
    }

}