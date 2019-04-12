pipeline {
    agent any
    stages {
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'npm install -g grunt-cli'
                    bat 'npm install -g bower'
                    bat 'bundle install'
                    bat 'bower install'
                    bat 'grunt prod'
                }
            }
        }
    }
}