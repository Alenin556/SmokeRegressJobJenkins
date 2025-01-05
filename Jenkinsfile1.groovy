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
        stage('Regress tests') {
            when {
                expression { return params.regress}
            }
            steps {
                bat "mvn -Dgroups=regress verify test"
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
                    to: 'aleninmailbox@gmail.com',
                    subject: "Build #${env.BUILD_NUMBER}",
                    body: "smoke results.",
                    attachFiles: 'allure-report//*')
        }
    }
}