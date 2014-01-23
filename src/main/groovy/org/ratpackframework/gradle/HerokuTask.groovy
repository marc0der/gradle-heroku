package org.ratpackframework.gradle
import com.heroku.api.HerokuAPI
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class HerokuTask extends DefaultTask {

    HerokuAPI herokuAPI

    HerokuTask(){}

    HerokuTask(String description){
        this.description = description
    }

    @TaskAction
    void start(){
        prepareAPI()
        def appName = "${project.heroku.appName}"
        execute([appName:appName])
    }

    private void prepareAPI() {
        def apiKey = "${project.heroku.apiKey}"
        herokuAPI = new HerokuAPI(apiKey)
    }

    abstract void execute(params)

}
