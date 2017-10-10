package com.openshift.evg.roadshow.parks.rest;

import com.mongodb.BasicDBObject;
import com.openshift.evg.roadshow.parks.db.MongoDBConnection;
import com.openshift.evg.roadshow.parks.db.postgre.PostgreDBConnection;
import com.openshift.evg.roadshow.parks.model.Park;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/ws/data")
@RestController
public class Parks {

    //private final MongoDBConnection con;
    private final PostgreDBConnection con;


    @Autowired
    public Parks(PostgreDBConnection connection) {
        this.con = connection;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/load", produces = "application/json")
    public String load() {
        System.out.println("[INFO] load()");
        con.createParksTable();
        con.loadParks();
        return "Items inserted in database: " + con.sizeInDB();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = "application/json")
    public List<Park> getAllParks() {
        System.out.println("[DEBUG] getAllParks");

        return con.getAll();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/within", produces = "application/json")
    public List<Park> findParksWithin(
            @RequestParam("lat1") float lat1,
            @RequestParam("lon1") float lon1,
            @RequestParam("lat2") float lat2,
            @RequestParam("lon2") float lon2) {
        System.out.println("[DEBUG] findParksWithin(" + lat1 + "," + lon1 + "," + lat2 + "," + lon2 + ")");

        // make the query object
        BasicDBObject spatialQuery = new BasicDBObject();

        List<Park> parks = con.findParksWithin(lat1,lon1,lat2,lon2);
        
        return con.getByQuery(spatialQuery);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/centered", produces = "application/json")
    public List<Park> findParksCentered(@RequestParam("lat") float lat, @RequestParam("lon") float lon, @RequestParam("maxDistance") int maxDistance, @RequestParam("minDistance") int minDistance) {


        // TODO: Implement this
        return null;
    }

}
