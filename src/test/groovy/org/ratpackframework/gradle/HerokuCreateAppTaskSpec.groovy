package org.ratpackframework.gradle

import spock.lang.Specification

/**
 * User: marco
 * Date: 15/08/13
 * Time: 08:18
 */
class HerokuCreateAppTaskSpec extends Specification{

    HerokuCreateAppTask task

    void setup(){
        task = new HerokuCreateAppTask()
    }

}
