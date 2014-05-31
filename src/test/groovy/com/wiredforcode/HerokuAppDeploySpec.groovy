package com.wiredforcode

import org.eclipse.jgit.lib.SymbolicRef
import org.eclipse.jgit.transport.RefSpec
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.IgnoreRest
import spock.lang.See
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
		def obj = new Object()
		repository.resolve = {
			obj // return object so that resolve is not null
		}
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
	
	void "should fail deployment if no master branch exists locally"() {
		given:
		def appName = "appName"
		
		and: "repo resolve was called to check whether remote branch exists"
		repository.resolve = {
			null
		}
		
		when:
		task.execute([appName: appName])
		
		then:
		thrown(GradleException)
	}
	
	@See('https://github.com/marcoVermeulen/gradle-heroku/issues/2')
	void "should deploy even if heroku remote has not been pushed to yet"() {
		given:
		def appName = "appName"
		
		and: "repo resolve was called to check whether remote branch exists"
		def remoteName
		def resolvedNull = false
		def resolvedNotNull = false
		def obj = new SymbolicRef("refname", null)
		repository.resolve = {
			if (it == "${HerokuTask.REMOTE_NAME}/${HerokuAppDeployTask.LOCAL_BRANCH}") {
				resolvedNull = true
				return null
			} 
			resolvedNotNull = true
			return obj
		}
		
		and: "push command did not set ref specs"
		def refSpecsCalled = false
		pushCommand.setRefSpecs = {
			refSpecsCalled = true
			pushCommand
		}
		
		and: "push command added a ref"
		def added = false
		pushCommand.add = {
			added = true
			pushCommand
		}
		
		and: "push command was called"
		def called = false
		pushCommand.call = {
			called = true
			pushCommand
		}
		
		when:
		task.execute([appName: appName])
		
		then:
		notThrown(Throwable)
		resolvedNull
		resolvedNotNull
		!refSpecsCalled
		added
		called
	}
}
