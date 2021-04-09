node {

  stage('Git Clone') {
    git branch: 'master', credentialsId: 'github', url: 'git@github.com:batizhao/stalber.git'
  }

  stage('Code Test') {
    withMaven(maven: 'maven', jdk: 'jdk8', mavenSettingsConfig: 'maven-settings') {
      sh "mvn clean test -Ptest"
    }
  }

}