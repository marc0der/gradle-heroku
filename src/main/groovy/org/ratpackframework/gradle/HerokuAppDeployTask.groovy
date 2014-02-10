package org.ratpackframework.gradle

import org.eclipse.jgit.transport.RefSpec
import org.gradle.api.GradleException

class HerokuAppDeployTask extends HerokuTask {

    static final String DEPLOYABLE_ARTIFACT_FOLDER = "build/libs"
    static final String REF_SPEC = "master:master"

    HerokuAppDeployTask(){
        super("Deploy the application to Heroku.")
    }

    @Override
    void execute(Object params) {
        def config = git.repository.config
        def remotes = config.getSubsections("remote")
        if(!remotes.contains(REMOTE_NAME)){
            throw new GradleException("No remote $REMOTE_NAME found. First run: \$ gradle herokuAppCreate")
        }

        logger.quiet "Pushing to remote repo: $REMOTE_NAME."
        logger.quiet "This could take a while... Really."
        def refSpec = new RefSpec(REF_SPEC)
        git.push()
            .setRemote(REMOTE_NAME)
            .setRefSpecs(refSpec)
            .call()
        logger.quiet "Finished pushing to: $REMOTE_NAME!"
    }
}
