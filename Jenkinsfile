pipeline {
    agent any
    stages {
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'gem install bundler'
                    bat 'bower install'
                    bat 'bundle install'
                    bat 'grunt prod'
                }
            }
        }
    }
}