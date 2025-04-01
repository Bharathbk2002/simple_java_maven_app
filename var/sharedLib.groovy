def call()
{
    sh 'mvn clean package -DskipTests=true'
    stash name: 'buildArtifacts', includes: '**/target/*.jar'
}
