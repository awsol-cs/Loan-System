pipeline {
    agent any
    stages {
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'npm install -g grunt-cli'
                    bat 'npm install -g bower'
                    sh 'bundle install'
                    sh 'bower install'
                    sh 'grunt prod'
                }
            }
        }
    }
}