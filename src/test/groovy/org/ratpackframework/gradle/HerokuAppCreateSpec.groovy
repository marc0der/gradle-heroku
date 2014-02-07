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
    def git = new Expando()
    def repo = new Expando()
    def storedConfig = new Expando()
    def remoteConfig = new Expando()

    def setup(){
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'heroku'

        task = project.tasks.findByName(CREATE_APP_TASK_NAME)
        task.herokuAPI = herokuAPI
        task.git = git
        task.buildRemoteConfig = { storedConfigParam, remoteNameParam ->
            assert storedConfigParam == storedConfig
            remoteConfig
        }

        git.repository = repo
        repo.config  = storedConfig
    }

    void "should declare a valid description"(){
        expect:
        task.description == 'Creates a new application on Heroku.'
    }

    void "should create an app with heroku generated name when name is not provided"() {
        given:
        def cedar = Heroku.Stack.Cedar
        def app = new App().on(cedar)
        task.prepareStoredConfig = { a, b, c -> }

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
        task.prepareStoredConfig = { a, b, c -> }

        when:
        task.execute([appName: appName])

        then:
        1 * herokuAPI.createApp(_) >> app
        app.name == appName
        app.stack == cedar
    }

    void "should set buildpack configuration if present when creating new app"() {
        given:
        def appName = "fast-everglades-6675"
        def buildpack = "http://dl.bintray.com/vermeulen-mp/buildpacks/heroku-buildpack-gradlew.zip"
        def cedar = Heroku.Stack.Cedar
        def app = new App().named(appName).on(cedar)
        task.prepareStoredConfig = { a, b, c -> }

        when:
        task.execute([appName: appName, buildpack: buildpack])

        then:
        herokuAPI.createApp(_) >> app

        and:
        1 * herokuAPI.addConfig(appName, { it.BUILDPACK_URL == buildpack })
    }

    void "should set default buildpack on absence of configuration when creating a new app"() {
        given:
        def defaultBuildpack = "https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git"
        def appName = "fast-everglades-6675"
        def cedar = Heroku.Stack.Cedar
        def app = new App().named(appName).on(cedar)
        task.prepareStoredConfig = { a, b, c -> }

        when:
        task.execute([appName: appName, buildpack: 'null'])

        then:
        herokuAPI.createApp(_) >> app

        and:
        1 * herokuAPI.addConfig(appName, { it.BUILDPACK_URL == defaultBuildpack })
    }

    void "should add remote heroku url to git repository on app creation"() {
        given:
        def appName = "appName"
        def gitUrl = "git@heroku.com:frozen-balls-9999.git"
        def app = Mock(App)

        and:
        def theURI
        def theStoredConfig
        storedConfig.save = {}
        remoteConfig.addURI = { theURI = it }
        remoteConfig.update = { theStoredConfig = it }

        when:
        task.execute([appName:appName, buildpack: null])

        then:
        herokuAPI.createApp(_) >> app
        app.getGitUrl() >> gitUrl

        and:
        theURI.toString() == gitUrl
        theStoredConfig == storedConfig
    }

    void "should set git remote name to heroku on app creation"() {
        given:
        def appName = "appName"
        def app = Mock(App)
        def theRemoteName
        task.prepareStoredConfig = { a, b, c ->
            theRemoteName = b
        }

        when:
        task.execute([appName: appName, buildpack: null])

        then:
        herokuAPI.createApp(_) >> app
        app.getGitUrl() >> ""

        and:
        theRemoteName == "heroku"
    }

}
