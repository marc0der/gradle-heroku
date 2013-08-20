package org.ratpackframework.gradle

import com.heroku.api.HerokuAPI
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class HerokuApp extends DefaultTask {

    HerokuAPI herokuAPI

    @TaskAction
    void start(){
        prepareAPI()
        def appName = "${project.heroku.appName}"
        executeOnApp(appName)
    }

    private void prepareAPI() {
        def apiKey = "${project.heroku.apiKey}"
        herokuAPI = new HerokuAPI(apiKey)
    }

    abstract void executeOnApp(String appName)

}
