def build()
{
    sh 'mvn clean package -DskipTests=true'
    stash name: 'buildArtifacts', includes: '**/target/*.jar'
}
def test()
{
    sh 'mvn test'
}

def deploy()
{
    sh 'java -jar $WORKSPACE/target/my-app-1.0-SNAPSHOT.jar'
}