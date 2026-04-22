pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Default Maven'
    }

    environment {
        SONAR_PROJECT_KEY = 'tictactoe'
        IMAGE_NAME        = 'tictactoe'
        IMAGE_TAG         = "${BUILD_NUMBER}"
        DOCKERHUB_USER    = 'prathamesh2019'
    }

    stages {

        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build & Package') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv() {
                    sh "mvn clean verify sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY}"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} ."
                sh "docker tag ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_USER}/${IMAGE_NAME}:latest"
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                  usernameVariable: 'DOCKER_USER',
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                sh "docker stop ${IMAGE_NAME} || true"
                sh "docker rm ${IMAGE_NAME} || true"
                sh "docker run -d -p 8080:8080 --name ${IMAGE_NAME} ${DOCKERHUB_USER}/${IMAGE_NAME}:latest"
            }
        }
    }

    post {
        success { echo "Pipeline succeeded! App running at http://localhost:8080" }
        failure { echo "Pipeline failed. Check logs above." }
    }
}
