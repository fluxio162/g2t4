package TimeManager.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The Controller for our Restservice. Here we put and get the JSon Object.
 * We need our Service to get and put the object.
 */
@RestController
public class RestObjectController {

    @Autowired
    private RestService service;

    @GetMapping("/rest")
    public List<RestObject> getObjects() {
        return service.getRestObjects();
    }

    @PostMapping("/rest")
    private RestObject sendData(@RequestBody RestObject restObject) {
        return service.getRestObject(restObject.getTimeflipMac(), restObject.getContent());
    }


}
