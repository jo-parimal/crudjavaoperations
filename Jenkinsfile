pipeline {
    agent any

    environment {
        SSH_BIN = 'C:\\Windows\\System32\\OpenSSH\\ssh.exe'
        SCP_BIN = 'C:\\Windows\\System32\\OpenSSH\\scp.exe'
        EC2_USER = 'ec2-user'
        EC2_IP = '13.221.242.9'
        DEST_PATH = '/home/ec2-user/deploy'
        JAR_NAME = 'crudoperation.jar'
    }

    parameters {
        string(name: 'BRANCH', defaultValue: 'ec2-deploy-branch', description: 'Git branch to build and deploy')
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-creds', 
                    usernameVariable: 'GIT_USER', 
                    passwordVariable: 'GIT_TOKEN'
                )]) {
                    git url: "https://${GIT_USER}:${GIT_TOKEN}@github.com/jo-parimal/crudjavaoperations.git", 
                        branch: "${params.BRANCH}"
                }
            }
        }

        stage('Build with Maven') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Copy JAR to EC2') {
            steps {
                script {
                    def jarFile = findFiles(glob: 'target/*.jar')?.first()?.path
                    if (!jarFile) {
                        error("No JAR file found in target directory.")
                    }

                    withCredentials([file(credentialsId: 'devlogin', variable: 'PEM')]) {
                        // Fix file permissions for PEM
                        bat """
                            icacls "${PEM}" /inheritance:r
                            icacls "${PEM}" /remove:g "Users"
                            icacls "${PEM}" /grant:r "%USERNAME%:R"
                        """

                        // Secure copy to EC2
                        bat "\"${SCP_BIN}\" -i \"${PEM}\" -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null \"${jarFile}\" ${EC2_USER}@${EC2_IP}:${DEST_PATH}/${JAR_NAME}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and SCP to EC2 completed successfully.'
            echo "On EC2, run: cd ${DEST_PATH} && sh ./stop_service.sh && sh ./start_service.sh"
        }
        failure {
            echo 'Build or deploy failed. Check logs above.'
        }
    }
}

