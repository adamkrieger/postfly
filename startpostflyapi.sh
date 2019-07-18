java \
	-DdbUrl=jdbc:postgresql://localhost:5432/ \
	-DdbName=$POSTFLY_DBNAME \
	-DdbUser=$POSTFLY_DBUSER \
	-DdbPass=$POSTFLY_DBPASS \
	-DmigrationDir=filesystem:/scripts/migrations \
	-jar /app/postflysc.jar
