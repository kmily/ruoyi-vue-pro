pipeline {
    agent any

    environment {
        NAME = "specialty"
        VERSION = "0.0.1"
        ENVTYPE = "DEV"
        DEVIMGURL = "192.168.10.206:8888"
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

        stage('启动项目') {
            steps {
                sshPublisher(publishers: [sshPublisherDesc(configName: 'saas', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: 'sh /home/start.sh', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
    }
}