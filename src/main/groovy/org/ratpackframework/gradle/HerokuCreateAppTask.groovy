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
            logger.quiet "\nUsing name $appName to create Cedar Stack."
            app = api.createApp(new App().on(Heroku.Stack.Cedar).named(appName));
        } else {
            logger.quiet "\nUsing Heroku suggested name to create Cedar Stack."
            app = api.createApp(new App().on(Heroku.Stack.Cedar))
        }

        logger.quiet "\nApplication created!"
    }

}
