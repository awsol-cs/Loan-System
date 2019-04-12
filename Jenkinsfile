pipeline {
    agent any
    stages {
        stage('Build BackEnd') {
            steps {
                dir(‘BackEnd’) {bat ‘gradlew.bat clean build’}
            }
        }
        stage('Build FrontEnd'){
        	steps {
        		sh 'cd FrontEnd'
        		sh 'npm install -g grunt-cli'
    				sh 'npm install -g bower'
    				sh 'bundle install'
    				sh 'bower install'
    				sh 'grunt prod'
        	}
        }
    }
}
}