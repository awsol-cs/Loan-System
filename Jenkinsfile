pipeline {
    agent any
    stages {
        stage('Build FrontEnd'){
            steps {
                dir("FrontEnd"){
                    bat 'grunt prod'
                }
            }
        }
    }
}