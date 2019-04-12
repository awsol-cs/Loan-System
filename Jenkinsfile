pipeline {
    agent none
    stages {
        stage('Build BackEnd') {
            agent {
                node {
                    label 'backend-label'
                    customWorkspace '/BackEnd/fineract-provider'
                }
            }
            steps {
                bat '..\\gradlew clean war'
            }
        }
        stage('Build FrontEnd'){
          agent {
              node {
                  label 'frontend-label'
                  customWorkspace '/FrontEnd'
              }
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