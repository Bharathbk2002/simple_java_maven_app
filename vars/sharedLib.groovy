def build()
{
    sh 'mvn clean package -DskipTests=true'
    stash name: 'buildArtifacts', includes: '**/target/*.jar'
}
return this