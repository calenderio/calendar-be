job("Build and run tests") {
    container(displayName = "Prepare Data", "maven:3.6.3-openjdk-16") {
        shellScript {
            content = """ 
	            mvn clean install 
           """
        }
    }

    container(displayName = "Deploy to Heroku", "maven:3.6.3-openjdk-16") {
        shellScript {
            content = """ 
	            mvn heroku:deploy -DskipTests=true 
           """
        }
    }
}