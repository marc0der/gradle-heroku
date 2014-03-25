package com.wiredforcode

import org.eclipse.jgit.transport.RefSpec
import org.gradle.api.GradleException

class HerokuAppDeployTask extends HerokuTask {

    static final String REF_SPEC = "master:master"

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
        git.push()
            .setRemote(HerokuTask.REMOTE_NAME)
            .setRefSpecs(refSpec)
            .call()
        logger.quiet "Finished pushing to: $HerokuTask.REMOTE_NAME!"
    }
}
