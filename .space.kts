job("Build and run tests") {
    container("maven:3.6.3-openjdk-16") {
        shellScript {
            content = """ 
	            mvn clean install 
           """
        }
    }
}

job("Deploy Heroku") {
    container("maven:3.6.3-openjdk-16") {
        shellScript {
            content = """ 
	            mvn heroku:deploy -DskipTests=true 
           """
        }
    }
}