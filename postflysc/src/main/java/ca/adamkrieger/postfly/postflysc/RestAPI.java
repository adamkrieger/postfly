package ca.adamkrieger.postfly.postflysc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ca.adamkrieger.postfly.postflysc.DBOperations.DBOperationResult;

@RestController
public class RestAPI {
    private static final Logger log = LoggerFactory.getLogger(RestAPI.class);

    private final DBOperations DBOperations;

    @Autowired
    public RestAPI(DBOperations DBOperations) {
        this.DBOperations = DBOperations;
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String health() {
        return "yep";
    }

    @RequestMapping(value = "/migrate", method = RequestMethod.POST)
    public String migrate() throws Exception {
        DBOperationResult result = DBOperations.migrateDB();

        if(!result.success) {
            throw new Exception(result.message);
        }

        return result.message;
    }

    @RequestMapping(value = "/revision", method = RequestMethod.GET)
    public String revision() throws Exception {
        DBOperationResult result = DBOperations.getRevision();

        if(!result.success) {
            throw new Exception(result.message);
        }

        return result.message;
    }
}
