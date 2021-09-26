job("Build and run tests") { 
   container("maven:3.6.3-openjdk-16-slim") { 
       shellScript { 
           content = """ 
	            mvn clean install 
           """ 
       } 
   } 
}