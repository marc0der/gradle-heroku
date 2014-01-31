package org.ratpackframework.gradle
import com.heroku.api.App
import com.heroku.api.Heroku
import org.eclipse.jgit.transport.RemoteConfig
import org.eclipse.jgit.transport.URIish

class HerokuAppCreateTask extends HerokuTask {

    HerokuAppCreateTask(){
        super('Creates a new application on Heroku.')
    }

    @Override
    void execute(params){
        def name = params.appName
        def buildpack = params.buildpack

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

        logger.quiet "Setting buildpack for new application $app.name to $buildpack"

        if(buildpack) herokuAPI.addConfig(app.name, ["BUILDPACK_URL":buildpack])

        def config = git.repository.config
        def url = app.gitUrl
        prepareStoredConfig(config, REMOTE_NAME, url)

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

}
