pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'spring-boot-app'
        DOCKERFILE_PATH = "${WORKSPACE}/miniassignment/Dockerfile"
    }
   
    stages {
        stage('Checkout') {
            steps {
                echo "Downloading code from git."
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo "Building the project and skipping test cases."
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                echo "Performing unit testing."
                sh 'mvn test'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image and tag it
                    echo 'Build docker image.'
                    sh "docker build -t ${DOCKER_IMAGE} -f ${DOCKERFILE_PATH} ."
                }
            }
        }
        
        // stage('Deploy Application') {
         //    steps {
           //     echo "Deploy application."
           //      sh 'docker run --name spring-boot-app-container -d --link mysql-container:mysql -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/users -p 8080:8080 ${DOCKER_IMAGE}'
          //  }
      //  }
    }
}
