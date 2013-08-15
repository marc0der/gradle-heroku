package org.ratpackframework.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class HerokuBuildpack extends DefaultTask {

    @TaskAction
    void install(){
        def target = "${project.buildDir}/download"

        logger.quiet "\nDownloading buildpack from: ${project.heroku.buildpack} to: ${target}"

        def zipFile = downloadTo(target)

        logger.quiet "\nCopy exploded files into: ${project.projectDir}"

        project.copy {
            from project.zipTree(zipFile)
            into project.projectDir
        }
    }

    def downloadTo(String location){
        def url = project.heroku.buildpack

        if(! url) throw new GradleException("URL not set!")

        createDirectory(location)
        def fileName = constructFilename(location, url)
        streamURLToFile(url, fileName)
    }

    private createDirectory(String directory) {
        new File(directory).mkdirs()
    }

    private constructFilename(String directory, String url){
        "${directory}/${url.tokenize("/")[-1]}".toString()
    }

    private streamURLToFile(String url, String fileName){
        def out = new BufferedOutputStream(new FileOutputStream(fileName))
        out << new URL(url).openStream()
        out.close()
        new File(fileName)
    }


}
