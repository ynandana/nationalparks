package com.openshift.evg.roadshow.parks.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.openshift.evg.roadshow.parks.model.Park;
import com.openshift.evg.roadshow.parks.model.Coordinates;

public class ParkRowMapper implements RowMapper<Park> {

	public Park mapRow(ResultSet rs, int rowNum) throws SQLException {
		Park park = new Park();
		
		park.setId(rs.getInt("id"));
		park.setCountryCode(rs.getString("country_code"));
		park.setCountryName(rs.getString("country_name"));
		park.setName(rs.getString("name"));
		park.setToponymName(rs.getString("toponym_name"));
		park.setLatitude(rs.getString("latitude"));
		park.setLongitude(rs.getString("longitude"));
		park.setCountryCode(rs.getString("country_code"));
		
		Coordinates pos = new Coordinates(rs.getString("latitude"), rs.getString("longitude"));
		
		park.setPosition(pos);
		
		return park;
	}

}
