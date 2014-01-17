package org.ratpackframework.gradle

import com.heroku.api.App
import com.heroku.api.Heroku

class HerokuAppCreateTask extends HerokuTask {

    HerokuAppCreateTask(){
        super('Creates a new application on Heroku.')
    }

    @Override
    void execute(params){
        def name = params.appName

        def app
        if(params.appName){
            logger.quiet "\nUsing name $params.appName to create Cedar Stack."
            app = herokuAPI.createApp(new App().on(Heroku.Stack.Cedar).named(name));
        } else {
            logger.quiet "\nUsing Heroku suggested name to create Cedar Stack."
            app = herokuAPI.createApp(new App().on(Heroku.Stack.Cedar))
        }

        logger.quiet "\nApplication $app.name created!"
    }

}
