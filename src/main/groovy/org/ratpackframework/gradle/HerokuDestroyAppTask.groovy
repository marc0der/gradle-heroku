package org.ratpackframework.gradle

import com.heroku.api.HerokuAPI
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 * User: marco
 * Date: 14/08/13
 * Time: 21:21
 */
class HerokuDestroyAppTask extends DefaultTask {

    @TaskAction
    void destroyApp(){
        def apiKey = "${project.heroku.apiKey}"
        def appName = "${project.heroku.appName}"
        def api = new HerokuAPI(apiKey)

        if(! appName) {
            throw new GradleException("No appName specified.")
        }

        logger.info "Destroying application: $appName"
        api.destroyApp(appName)
        logger.info "Application destroyed: $appName"
    }

}
