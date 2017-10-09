package com.openshift.evg.roadshow.parks.db.postgre;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.openshift.evg.roadshow.parks.model.Park;
import com.openshift.evg.roadshow.parks.model.rowmapper.ParkRowMapper;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmorales on 11/08/16.
 */
@Component
public class PostgreDBConnection {

    private static final String FILENAME = "/nationalparks.json";

    private static final String COLLECTION = "nationalparks";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PostgreDBConnection() {
    }

    @PostConstruct
    public void initConnection() {
        
    }

    /*
     * Load from embedded list of parks using FILENAME
     */
    public void loadParks() {
        System.out.println("[DEBUG] MongoDBConnection.loadParks()");

        try {
        	
        	loadParks(resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX + FILENAME).getInputStream());
       
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
    }

    public void loadParks(String fileLocation) {
        System.out.println("[DEBUG] MongoDBConnection.loadParks(" + fileLocation + ")");

        try {
            loadParks(new FileInputStream(new File(fileLocation)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        }
    }

    public void loadParks(InputStream is) {
        System.out.println("[DEBUG] MongoDBConnection.loadParks(InputStream)");

        List<Document> docs = new ArrayList<Document>();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        try {
            String currentLine = null;
            int i = 1;
            while ((currentLine = in.readLine()) != null) {
                String s = currentLine.toString();
                
                JacksonJsonParser parser = new JacksonJsonParser();
            	Map<String, Object> data = parser.parseMap(s);
                
            	for (Map.Entry<String, Object> entry : data.entrySet()) {
        			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue() + " type : " + entry.getValue().getClass().getName());
        		}
            	
            	
            	Object[] obj = {	data.get("countryCode"),
			            			data.get("countryName"),
			            			data.get("name"),
			            			data.get("toponymName"),
			            			((List)data.get("coordinates")).get(0),
			            			((List)data.get("coordinates")).get(1),
			            			
							};
            	
            	jdbcTemplate.update("INSERT INTO park(country_code, country_name, name, toponym_name, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?);", obj);
            	
            	
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading parks. Return empty list. " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading parks. Return empty list");
            }
        }
    }


    /**
     *
     */
    public void clear() {
        System.out.println("[DEBUG] MongoDBConnection.clear()");
        jdbcTemplate.update("truncate table park");
    	
        
    }


    /**
     * @param parks
     */
    public void init(List<Document> parks) {
        System.out.println("[DEBUG] MongoDBConnection.init(...)");
        
    }

    /**
     * @return
     */
    public long sizeInDB() {
        int size = this.jdbcTemplate.queryForObject("select count(*) from park", Integer.class);
        
        return size;
    }
    
    /**
     * @param parks
     */
    public List<Park> findParksWithin(float lat1, float lon1, float lat2, float lon2) {
    	
    	Object[] obj = {lat1, lat2, lon1, lon2 };
      	return jdbcTemplate.query("select * from park where latitude between ? and ? and longitude ? and ?", obj, new ParkRowMapper());

    }

    

    /**
     * @param parks
     */
    public void insert(List<Document> parks) {
        

    }

    /**
     * @return
     */
    public List<Park> getAll() {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        return jdbcTemplate.query("select * from park", new ParkRowMapper());
    }

    public List<Park> getWithin(float lat1, float lon1, float lat2, float lon2) {
        System.out.println("[DEBUG] MongoDBConnection.getAll()");
        ArrayList<Park> allParksList = new ArrayList<Park>();
        return allParksList;
    }

    /**
     * @param query
     * @return
     */
    public List<Park> getByQuery(BasicDBObject query) {
        System.out.println("[DEBUG] MongoDBConnection.getByQuery()");
        List<Park> parks = new ArrayList<Park>();
        return parks;
    }
}
