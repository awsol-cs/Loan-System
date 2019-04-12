pipeline {
    agent none
    stages {
        stage('Build BackEnd') {
            agent {
                node {
                    label 'master'
                    customWorkspace '/BackEnd/fineract-provider'
                }
            }
            steps {
                bat '..\\gradlew clean war'
            }
        }
        stage('Build FrontEnd'){
            steps {
                bat 'npm install -g grunt-cli'
                bat 'npm install -g bower'
                bat 'FrontEnd\\bundle install'
                bat 'FrontEnd\\bower install'
                bat 'FrontEnd\\grunt prod'
            }
        }
    }
}