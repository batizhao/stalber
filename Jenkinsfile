node {

  def build_tag
  def registry_addr = "harbor.pecado.com:8888"
  def maintainer_name = "stalber"
  def admin_image
  def version = "1.0-SNAPSHOT"

  stage('Git Clone') {
    git branch: 'master', credentialsId: 'github', url: 'git@github.com:batizhao/stalber.git'
  }

  stage('Code Test') {
    withMaven(maven: 'maven', jdk: 'jdk8', mavenSettingsConfig: 'maven-settings') {
      sh "mvn clean test -Ptest"
    }
  }

  stage('Build Maven Package') {
      build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
      withMaven(maven: 'maven', jdk: 'jdk11', mavenSettingsConfig: 'maven-settings') {
          sh "mvn clean package -Pdemo -Dmaven.test.skip=true"
      }
    }

    stage('Build Docker Image') {

      dir('stalber-admin') {
        image_name = "${registry_addr}/${maintainer_name}/admin:${version}-${build_tag}"
        admin_image = docker.build(image_name)
      }

    }

    stage('Push Docker Image') {
      docker.withRegistry('https://harbor.pecado.com:8888', 'harbor-auth') {
        admin_image.push()
      }
    }

}