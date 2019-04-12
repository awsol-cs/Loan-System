pipeline {
    agent any
    stages {
        stage('Build BackEnd') {
            steps {
                dir("BackEnd\\fineract-provider"){
                    bat '..\\gradlew clean war'
                }
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