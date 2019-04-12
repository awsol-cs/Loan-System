pipeline {
    agent any
    stages {
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'sh bower install'
                    bat 'ah bundle install'
                    bat 'sh grunt prod'
                }
            }
        }
    }
}