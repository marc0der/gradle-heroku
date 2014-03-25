package com.wiredforcode

import com.heroku.api.App
import com.heroku.api.Heroku
import org.eclipse.jgit.transport.RemoteConfig
import org.eclipse.jgit.transport.URIish

class HerokuAppCreateTask extends HerokuTask {

    final static String DEFAULT_BUILDPACK_URL = "https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git"

    HerokuAppCreateTask(){
        super('Creates a new application on Heroku.')
    }

    @Override
    void execute(params){
        def name = params.appName

        App app
        if(params.appName){
            logger.quiet "\nUsing name $params.appName to create Cedar Stack."
            app = herokuAPI.createApp(new App().on(Heroku.Stack.Cedar).named(name));

        } else {
            logger.quiet "\nUsing Heroku suggested name to create Cedar Stack."
            app = herokuAPI.createApp(new App().on(Heroku.Stack.Cedar))
            logger.quiet "\nPlease add the following appName line to the heroku block in build.gradle:\n\n"
            logger.quiet "  heroku {"
            logger.quiet "      ..."
            logger.quiet "      appName = '${app.name}'"
            logger.quiet "      ..."
            logger.quiet "  }"
        }

        def buildpackURL = !params.buildpack ? DEFAULT_BUILDPACK_URL : params.buildpack
        logger.quiet "Setting buildpack for new application $app.name to $buildpackURL"
        configureBuildpack(app.name, buildpackURL)

        def config = git.repository.config
        def url = app.gitUrl
        prepareStoredConfig(config, HerokuTask.REMOTE_NAME, url)

        logger.quiet "\nApplication $app.name created!"
    }

    def prepareStoredConfig = { storedConfig, remoteName, remoteURI ->
        prepareRemoteConfig(storedConfig, remoteName, remoteURI)
        storedConfig.save()
    }

    def prepareRemoteConfig = { storedConfig, remoteName, remoteURI ->
        def remoteConfig = buildRemoteConfig(storedConfig, remoteName)
        def uri = new URIish(remoteURI)
        remoteConfig.addURI(uri)
        remoteConfig.update(storedConfig)
    }

    def buildRemoteConfig = { storedConfig, remoteName ->
        new RemoteConfig(storedConfig, remoteName)
    }

    def configureBuildpack(String appName, buildpackURL){
        herokuAPI.addConfig(appName, ["BUILDPACK_URL":buildpackURL])
    }

}
