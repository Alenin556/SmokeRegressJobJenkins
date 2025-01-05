pipeline {
    agent any

    tools {
        jdk "jdk-17"
        maven "maven 3.9.6"
    }

    parameters {
        // добавляем чекбоксы к запуску с параметрами
        // по дефолту будет запущена сборка со смоук тестами
        booleanParam(defaultValue: true, description: "run smoke tests", name: 'smoke')
        booleanParam(defaultValue: false, description: "run regress tests", name: 'regress')
    }

    stages {
        stage('Build') {
            steps {
                // Забираем репо c гитхаб
                git branch: 'master', credentialsId: 'git', url: 'https://github.com/Alenin556/SmokeRegressJobJenkins.git'
            }
        }
        stage('Smoke tests') {
            when {
                //если установлен чекбокс в параметрах запуска то будет запущен скрипт
                expression { return params.smoke}
            }
            steps {
                // Выполняем команду mvn тест
                bat "mvn -Dgroups=smoke verify test"
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
        }
    }
}