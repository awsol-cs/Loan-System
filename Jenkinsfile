pipeline {
    agent any
    stages {
        stage('Build BackEnd') {
                steps {
                    dir("BackEnd\\fineract-provider"){
                        bat '..\\gradlew clean war'
                    }
                }
            }
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'grunt prod'
                }
            }
        }
    }
}