pipeline {
    agent any
    stages {
        stage('Build BackEnd') {
            steps {
                echo (sh 'pwd')
                sh 'cd ./BackEnd'
                sh './gradlew clean war'
            }
        }
        stage('Build FrontEnd'){
        	steps {
        		sh 'cd ./FrontEnd'
        		sh 'npm install -g grunt-cli'
    				sh 'npm install -g bower'
    				sh 'bundle install'
    				sh 'bower install'
    				sh 'grunt prod'
        	}
        }
    }
}