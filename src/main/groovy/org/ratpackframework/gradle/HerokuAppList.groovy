package org.ratpackframework.gradle

class HerokuAppList extends HerokuApp {

    HerokuAppList(){
        super("Lists all Apps available for the current user on Heroku.")
    }

    @Override
    void executeOnApp(String appName) {


    }
}
