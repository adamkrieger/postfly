java \
	-DdbUrl=jdbc:postgresql://localhost:5432/ \
	-DdbName=$POSTGRES_DB \
	-DdbUser=$POSTGRES_USER \
	-DdbPass=$POSTGRES_PASSWORD \
	-DmigrationDir=filesystem:/scripts/migrations \
	-jar /app/postflysc.jar
