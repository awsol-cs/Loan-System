pipeline {
    agent any
    stages {
        stage('Build BackEnd') {
            steps {
                bat 'cd BackEnd'
                bat 'gradlew clean war'
            }
        }
        stage('Build FrontEnd'){
        	steps {
        		bat 'cd FrontEnd'
        		bat 'npm install -g grunt-cli'
    				bat 'npm install -g bower'
    				bat 'bundle install'
    				bat 'bower install'
    				bat 'grunt prod'
        	}
        }
    }
}