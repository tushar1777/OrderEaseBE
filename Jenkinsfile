pipeline {
  agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-login')
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
        SONARQUBE_URL = 'http://54.197.189.182:8090/'
        SONARQUBE_SCANNER = 'sq1'
        SONARQUBE_CREDENTIALS = 'jenkins-sonar'
    }
    
  stages {
    stage('Checkout Code') {
      steps {
        git branch: 'main', url: 'https://github.com/tushar1777/OrderEaseBE.git'
      }
    }

    stage('Build and Print Java Version') {
      steps {
        sh 'mvn clean package'
      }
    }
    
    stage('SonarQube Analysis') {
        environment {
            scannerHome = tool 'SonarQubeScanner'
        }
        steps {
            withSonarQubeEnv('sq1') { // This should match the SonarQube installation name in Jenkins
                sh 'mvn sonar:sonar'
            }
        }
    }

    stage('Build Docker Image') {
      steps {
        script {
            sh "docker login  -u shoib -p '&a-?.3XpaU!G8P6'"
            sh "docker build -t shoib/devops-integration ."
            sh "docker push shoib/devops-integration"
          }
        }
    }
  }
}
