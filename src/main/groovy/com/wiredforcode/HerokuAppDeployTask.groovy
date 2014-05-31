package com.wiredforcode

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.RefSpec
import org.gradle.api.GradleException

class HerokuAppDeployTask extends HerokuTask {

	static final String LOCAL_BRANCH = 'master'
	static final String REF_SPEC = "${LOCAL_BRANCH}:master"

    HerokuAppDeployTask(){
        super("Deploy the application to Heroku.")
    }

    @Override
    void execute(Object params) {
        def config = git.repository.config
        def remotes = config.getSubsections("remote")
        if(!remotes.contains(HerokuTask.REMOTE_NAME)){
            throw new GradleException("No remote $HerokuTask.REMOTE_NAME found. First run: \$ gradle herokuAppCreate")
        }

        logger.quiet "Pushing to remote repo: $HerokuTask.REMOTE_NAME."
        logger.quiet "This could take a while... Really."
        def refSpec = new RefSpec(REF_SPEC)
		// if the remote has never been pushed to, we cannot push refspec master:master
		// see https://github.com/marcoVermeulen/gradle-heroku/issues/2
		if (git.repository.resolve("${HerokuTask.REMOTE_NAME}/${LOCAL_BRANCH}") != null) {
	        git.push()
	            .setRemote(HerokuTask.REMOTE_NAME)
	            .setRefSpecs(refSpec)
	            .call()
		} else {
			def localRef = git.repository.resolve(LOCAL_BRANCH)
			if (localRef == null) throw new GradleException("Your app must exist in a Git repository with a master branch.")
			git.push()
				.setRemote(HerokuTask.REMOTE_NAME)
				.add(LOCAL_BRANCH)
				.call()
		}
        logger.quiet "Finished pushing to: $HerokuTask.REMOTE_NAME!"
    }
}
