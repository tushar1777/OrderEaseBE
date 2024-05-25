pipeline {
  agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-login')
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
        SONARQUBE_URL = 'http://54.197.189.182:8090/'
        SONARQUBE_SCANNER = 'sq1'
        // SONARQUBE_CREDENTIALS = 'jenkins-sonar'
       SONARQUBE_CREDENTIALS = 'zosh-food-02'
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
                // sh 'mvn clean verify sonar:sonar'
                sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=zosh-food-02 -Dsonar.projectName='zosh-food-02'"
            }
        }
    }

    stage('Build Docker Image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-login', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
          sh "docker login -u \$DOCKERHUB_USERNAME -p \$DOCKERHUB_PASSWORD"
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
