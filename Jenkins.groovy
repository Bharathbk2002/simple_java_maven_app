properties([
    parameters([
        choice(
            choices: ['dev', 'prod'],
            name: 'Select_environment',
            description: 'Choose the environment for deployment'
        )
        string(
            name: 'Lastname',
            defaultValue: 'Kumar N R',
            description: 'Enter your lastname'
        )
    ])
])
node('devServer')
{
    def NAME='Bharath'
    tool name: 'my-maven', type: 'maven'
    try
    {
    stage('build') 
    {
        def file = load 'script.groovy'
        file.greet()
        sh 'mvn clean package -DskipTests=true' 
        echo "hello ${Name}"
        stash name: 'buildArtifacts', includes: '**/target/*.jar'
    }
    stage('test')
    {
        parallel(
            testA:{
                
                    echo "This is test A"
                    sh 'mvn test'
            },
            testB:{
                
                    echo "This is test B"
                    sh 'mvn test'
            }
        )
        archiveArtifacts artifacts: '**/target/*.jar', followSymlinks: false
    }
    stage('deploy_dev')
    {
        if(params.Select_environment=='dev')
        {
            
                unstash 'buildArtifacts'
                echo "Deploying the application..."
                sh 'java -jar $WORKSPACE/target/my-app-1.0-SNAPSHOT.jar'

        }
    }
    stage('deploy_prod')
    {
        if(params.Select_environment=='prod')
        {
            node('prodServer')
            {
            timeout(time: 5, unit: 'DAYS') {
            input 'Deployment Approved'
            }
            
                unstash 'buildArtifacts'
                echo "Deploying the application..."
                sh 'java -jar $WORKSPACE/target/my-app-1.0-SNAPSHOT.jar'
            }
        }
    }
    }
    catch(Exception e)
    {
        currentBuild.result = 'FAILURE'
        throw e
    }

}
