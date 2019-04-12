pipeline {
    agent any
    stages {
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'npm install -g grunt-cli'
                    bat 'npm install -g bower'
                    bat 'gem install bundler'
                    bat 'bower install'
                    bat 'bundle install'
                    bat 'grunt prod'
                }
            }
        }
    }
}