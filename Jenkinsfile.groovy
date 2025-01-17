pipeline {
    agent any

    tools {
        jdk "jdk-17"
        maven "maven 3.9.6"
    }


    stages {
        stage('Build') {
            steps {
                // Забираем репо c гитхаб
                git branch: 'master', credentialsId: 'git', url: 'https://github.com/Alenin556/SmokeRegressJobJenkins.git'
            }
        }
        stage('Smoke tests') {
            steps {
                bat "mvn -Dgroups=smoke verify test"
            }
        }
    }

    post {
        // триггер always генерирует отчет всегда при любых обстоятельствах и ошибках на раних стадиях
        always {
            // базовые настройки для генерации отчета (тригер и путь до результатов)
            allure ([
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
            ])
            emailext(
                    mimeType: 'text/html',
                    subject: '$PROJECT_NAME #$BUILD_NUMBER $BUILD_STATUS Test Execution Summary',
                    to: 'aleninmailbox@gmail.com',
                    recipientProviders: [requestor()],
                    body:  ''' regress ${SCRIPT, template = "managed:AllureReportEmail"}''',
                    attachLog: false)
        }
    }
}