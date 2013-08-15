package org.ratpackframework.gradle

import org.gradle.api.GradleException

class HerokuDestroyApp extends HerokuApp {

    @Override
    void executeOnApp(String appName){
        if(!appName) throw new GradleException("No appName specified.")

        logger.quiet "\nDestroying application: $appName"
        herokuAPI.destroyApp(appName)
        logger.quiet "\nApplication destroyed!"
    }

}
