pipeline {
  agent any
    environment {
        DOCKERHUB_PASSWORD = credentials('dockerhub-password')
        DOCKERHUB_USERNAME = credentials('dockerhub-username')
        SONARQUBE_URL = 'http://54.197.189.182:8090/'
        SONARQUBE_SCANNER = 'sq1'
        SONARQUBE_CREDENTIALS = 'jenkins-sonar'
    }
    
  stages {
    stage('Checkout Code') {
      steps {
        git branch: 'jenkinsfile', url: 'https://github.com/tushar1777/OrderEaseBE.git'
      }
    }

    stage('Clean Install Test and Build') {
      steps {
        sh 'mvn clean install'
        sh 'mvn test'
        sh 'mvn clean package'
      }
    }
    
    stage('SonarQube Analysis') {
        environment {
            scannerHome = tool 'SonarQubeScanner'
        }
        steps {
          withSonarQubeEnv('sq1') {
            sh 'mvn sonar:sonar'
          }
        }
    }

    stage('Build Docker Image') {
      steps {
        script {
          sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
          sh "docker build -t shoib/devops-integration ."
          sh "docker push shoib/devops-integration"
        }
      }
    }
  }
  post {
      always {
            cleanWs()
        }
    }
}
