package org.ratpackframework.gradle

import com.heroku.api.App

class HerokuAppListTask extends HerokuTask {

    HerokuAppListTask(){
        super("Lists all Apps available for the current user on Heroku.")
    }

    @Override
    void execute(params) {
        List appList = herokuAPI.listApps()

        logger.quiet "================================================================================"
        logger.quiet "Application List:"
        logger.quiet "================================================================================"

        appList.each { App app ->
            logger.quiet(app.name)
        }

        logger.quiet "================================================================================"

    }
}
