package org.ratpackframework.gradle
import org.eclipse.jgit.lib.PersonIdent
import org.eclipse.jgit.transport.RefSpec
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HerokuAppDeploySpec extends Specification {

    Project project = ProjectBuilder.builder().build()
    HerokuAppDeployTask task
    def git = new Expando()
    def repository = new Expando()
    def storedConfig = new Expando()
    def addCommand = new Expando()
    def commitCommand = new Expando()
    def pushCommand = new Expando()
    def buildLibs = "build/libs" as File

    Set remotes = ["heroku", "origin"]

    void setup(){
        project.apply plugin: 'heroku'
        task = project.tasks.findByName('herokuAppDeploy')
        task.git = git

        storedConfig.getSubsections = { section -> remotes }
        repository.config = storedConfig
        git.repository = repository
        git.add = { addCommand }
        git.commit = { commitCommand }
        git.push = { pushCommand }

        //default command behaviour
        addCommand.addFilepattern = { addCommand }
        addCommand.call = {}
        commitCommand.setAuthor = { commitCommand }
        commitCommand.setMessage = { commitCommand }
        commitCommand.call = {}
        pushCommand.setRemote = { pushCommand }
        pushCommand.setRefSpecs = { pushCommand }
        pushCommand.call = {}

        buildLibs.mkdirs()
    }

    void "should display a valid description" (){
        expect:
        task.description == "Deploy the application to Heroku."
    }

    void "should add build artifacts to git repo"() {
        given:
        def appName = "appName"


        and: "the build folder is added"
        def filePatternToAdd
        addCommand.addFilepattern = {
            filePatternToAdd = it
            addCommand
        }

        and: "the add command is called"
        def called = false
        addCommand.call = {
            called = true
        }

        when:
        task.execute([appName: appName])

        then:
        called
        filePatternToAdd == "build/libs"
    }

    void "should commit build artifacts to git repo"() {
        given:
        def appName = "appName"

        and: "the commit message is set"
        def messageSet = false
        commitCommand.setMessage = {
            assert it == "Commit generated artifact for heroku deployment."
            messageSet = true
            commitCommand
        }

        and: "the author is set"
        def authorSet = false
        commitCommand.setAuthor = { PersonIdent author ->
            assert author instanceof PersonIdent
            author.emailAddress == 'invalid@example.org'
            author.name == 'Gradle Heroku plugin'
            authorSet = true
            commitCommand
        }

        and: "the commit command is called"
        def called = false
        commitCommand.call = {
            called = true
        }

        when:
        task.execute([appName: appName])

        messageSet
        authorSet
        then:
        called
    }

    void "should push to git heroku remote"() {
        given:
        def appName = "appName"

        and: "the remote repo is selected for pushing"
        def remote
        pushCommand.setRemote = {
            remote = it
            pushCommand
        }

        and: "the refspec is set"
        RefSpec refspec
        pushCommand.setRefSpecs = {
            refspec = it
            pushCommand
        }

        and: "the push command is called"
        def called = false
        pushCommand.call = {
            called = true
        }

        when:
        task.execute([appName: appName])

        then:
        remote == 'heroku'
        refspec.toString() == 'master:master'
        called
    }

    void "should not deploy if build artifacts not present"() {
        given:
        def appName = "appName"
        buildLibs.deleteDir()

        when:
        task.execute([appName: appName])

        then:
        thrown(GradleException)
    }

    void "should not deploy if git heroku remote not set"() {
        given:
        def appName = "appName"
        remotes = ["origin"]
        def subsectionsFound = false
        storedConfig.getSubsections = { subsection ->
            assert subsection == "remote"
            subsectionsFound = true
            remotes
        }

        when:
        task.execute([appName: appName])

        then:
        subsectionsFound
        thrown(GradleException)
    }
}
