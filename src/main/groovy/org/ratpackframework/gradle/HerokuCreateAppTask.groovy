package org.ratpackframework.gradle

import com.heroku.api.App
import com.heroku.api.Heroku
import com.heroku.api.HerokuAPI
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class HerokuCreateAppTask extends DefaultTask {

    @TaskAction
    void action(){
        def apiKey = "${project.heroku.apiKey}"
        def appName = "${project.heroku.appName}"
        def api = new HerokuAPI(apiKey)

        def app
        if(appName){
            app = api.createApp(new App().on(Heroku.Stack.Cedar).named(appName));
        } else {
            app = api.createApp(new App().on(Heroku.Stack.Cedar))
        }

        logger.info "Created application: ${app.name}"
    }

}
