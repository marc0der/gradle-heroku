package org.ratpackframework.gradle

import com.heroku.api.HerokuAPI
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * User: marco
 * Date: 15/08/13
 * Time: 09:59
 */
abstract class HerokuApp extends DefaultTask {

    HerokuAPI herokuAPI

    @TaskAction
    void start(){
        def apiKey = "${project.heroku.apiKey}"
        herokuAPI = new HerokuAPI(apiKey)

        def appName = "${project.heroku.appName}"
        executeOnApp(appName)
    }

    abstract void executeOnApp(String appName)

}
