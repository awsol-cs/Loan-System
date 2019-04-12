pipeline {
    agent none
    stages {
        stage('Build BackEnd') {
            agent {
                customWorkspace '/BackEnd/fineract-provider'
            }
            steps {
                bat '..\\gradlew clean war'
            }
        }
        stage('Build FrontEnd'){
          agent {
              customWorkspace '/FrontEnd'
          }
        	steps {
        		bat 'npm install -g grunt-cli'
    				bat 'npm install -g bower'
    				bat 'bundle install'
    				bat 'bower install'
    				bat 'grunt prod'
        	}
        }
    }
}