package org.ratpackframework.gradle

import com.heroku.api.HerokuAPI
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class HerokuDestroyAppTask extends DefaultTask {

    @TaskAction
    void destroyApp(){
        def apiKey = "${project.heroku.apiKey}"
        def appName = "${project.heroku.appName}"
        def api = new HerokuAPI(apiKey)

        if(! appName) {
            throw new GradleException("No appName specified.")
        }

        logger.quiet "\nDestroying application: $appName"
        api.destroyApp(appName)
        logger.quiet "\nApplication destroyed!"
    }

}
