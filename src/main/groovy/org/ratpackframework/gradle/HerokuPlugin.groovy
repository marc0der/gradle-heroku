package org.ratpackframework.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class HerokuPlugin implements Plugin<Project> {

    @Override
    void apply(Project project){
        project.extensions.create("heroku", HerokuConfig)
        project.task('herokuBuildpack', type: HerokuBuildpackTask)
        project.task('herokuCreateApp', type: HerokuAppCreateTask)
        project.task('herokuDestroyApp', type: HerokuAppDestroyTask)
        project.task('herokuAppInfo', type: HerokuAppInfoTask)
        project.task('herokuAppList', type: HerokuAppListTask)
    }
}
