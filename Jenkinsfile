pipeline {
    agent any

    environment {
        NAME = "specialty"
        VERSION = "0.0.1"
        ENVTYPE = "DEV"
        DEVIMGURL = "192.168.10.205:8888"
    }

    stages {
        stage('拉取代码') {
            steps {
                echo "${ref}分支开始构建"
                checkout scmGit(branches: [[name: "${ref}"]], extensions: [], userRemoteConfigs: [[credentialsId: 'gitee', url: 'https://gitee.com/jianghewangluo/specialty.git']])
            }
        }

        stage('编译代码') {
            steps {
                sh "mvn clean package"
            }
        }

        stage('打包') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'harbor', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker build -t ${DEVIMGURL}/library/${NAME}:${VERSION} ./yudao-server/"
                    sh "echo ${password} | docker login  -u ${username}   --password-stdin http://${DEVIMGURL}"
                    sh "docker push ${DEVIMGURL}/library/${NAME}:${VERSION}"
                }
            }
        }
    }
}