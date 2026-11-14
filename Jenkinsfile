pipeline {
    agent any

    parameters {
        booleanParam(name: 'DEPLOY_TO_AWS', defaultValue: false, description: 'Cria/inicia a EC2 e publica o user-service.')
    }

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        AWS_PAGER = ''
        SOURCE_DIR = '/workspace/user-service'
    }

    stages {
        stage('Validar acesso AWS') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) { sh 'aws sts get-caller-identity' }
            }
        }

        stage('Compilar e testar') {
            steps {
                sh '/workspace/jenkins/scripts/maven-verify.sh user-service "$SOURCE_DIR"'
            }
        }

        stage('Garantir EC2') {
            when { expression { params.DEPLOY_TO_AWS } }
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) { sh '/workspace/jenkins/scripts/ensure-ec2.sh user-service > .ec2.env' }
            }
        }

        stage('Publicar') {
            when { expression { params.DEPLOY_TO_AWS } }
            steps {
                sshagent(credentials: ['ec2-deploy-key']) {
                    sh '. ./.ec2.env && cd "$SOURCE_DIR" && /workspace/jenkins/scripts/deploy-compose.sh user-service "$PUBLIC_IP" "http://localhost:3000"'
                }
            }
        }
    }
}
