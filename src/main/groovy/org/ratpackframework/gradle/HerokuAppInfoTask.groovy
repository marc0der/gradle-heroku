package org.ratpackframework.gradle

class HerokuAppInfoTask extends HerokuTask {

    HerokuAppInfoTask() {
        super('Displays comprehensive information about the named application.')
    }

    @Override
    void execute(params) {
        logger.quiet"Getting application ${params.appName}..."
        def app = herokuAPI.getApp(params.appName)
        logger.quiet "================================================================================"
        logger.quiet "Application Info:"
        logger.quiet "================================================================================"
        logger.quiet "                  name : ${app.name}"
        logger.quiet "                domain : ${app.domain?.domain}"
        logger.quiet "                 stack : ${app.stack}"
        logger.quiet "       requested stack : ${app.requestedStack}"
        logger.quiet "            created at : ${app.createdAt}"
        logger.quiet "         create status : ${app.createStatus}"
        logger.quiet "           released at : ${app.releasedAt}"
        logger.quiet "        buildpack desc : ${app.buildpackProvidedDescription}"
        logger.quiet "          git repo url : ${app.getGitUrl()}"
        logger.quiet " repo migration status : ${app.repoMigrateStatus}"
        logger.quiet "               web url : ${app.webUrl}"
        logger.quiet "           owner email : ${app.ownerEmail}"
        logger.quiet "                 dynos : ${app.dynos}"
        logger.quiet "               workers : ${app.workers}"
        logger.quiet "             slug size : ${app.slugSize}"
        logger.quiet "             repo size : ${app.repoSize}"
        logger.quiet "================================================================================"
    }
}
