package org.ratpackframework.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class HerokuPlugin implements Plugin<Project> {

    @Override
    void apply(Project project){
        project.extensions.create("heroku", HerokuConfig)
        project.task('herokuBuildpack', type: HerokuBuildpack)
        project.task('herokuCreateApp', type: HerokuCreateApp)
        project.task('herokuDestroyApp', type: HerokuDestroyApp)
        project.task('herokuAppInfo', type: HerokuAppInfo)
        project.task('herokuAppList', type: HerokuAppList)
    }
}
