package org.ratpackframework.gradle

import org.gradle.api.GradleException

class HerokuAppDestroyTask extends HerokuTask {

    HerokuAppDestroyTask(){
        super('Destroy the application on Heroku.')
    }

    @Override
    void execute(params){
        def name = params.appName
        if(!name) throw new GradleException("No appName specified.")

        logger.quiet "\nDestroying application: $name"
        herokuAPI.destroyApp(name)
        logger.quiet "\nApplication destroyed!"
    }

}
