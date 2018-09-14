package com.innowave.mahaulb.reports.manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.stereotype.Service;

import com.innowave.mahaulb.reports.data.Table;
import com.innowave.mahaulb.reports.data.Ulb;
import com.innowave.mahaulb.reports.util.ReportException;

@Service
public class Manager {
	@Autowired
	DataSource dataSource;
	
	public List<String> getColumns(String tableName){
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		tableMetadataContext.setTableName(tableName);
	    tableMetadataContext.processMetaData(dataSource, Collections.<String>emptyList(), new String[0]);
	    
	    return tableMetadataContext.getTableColumns();
	}
	
	public List<String> getAllTables(String schema){
		try {
			Connection connection = dataSource.getConnection();
			DatabaseMetaData meta = connection.getMetaData();
			String sc = meta.getUserName();
			ResultSet rs = connection.createStatement().executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='"+schema+"'");
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(rs.getString("table_name"));
			  
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ReportException();
		}
	}
	
	public List<String> getAllSchemas(){
		List<String> schemas = new ArrayList<>();
		try {
			Connection connection = dataSource.getConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs  = meta.getSchemas();
			while (rs.next()) {
				String tableSchema = rs.getString(1);  
				schemas.add(tableSchema);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ReportException();
		}
		return schemas;
	}
	
	public List<Table> getTablesForSchema(String schema){
		try {
			Connection connection = dataSource.getConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs = connection.createStatement().executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='"+schema+"'");
			
			List<Table> tables = new ArrayList<Table>();
			int count = 0;
			while (rs.next()) {
				count++;
				Table table = new Table();
				table.setId(count);
				table.setTableName(rs.getString("table_name"));
				tables.add(table);
			  
			}
			return tables;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ReportException();
		}
	}
	
	public List<Table> getTablesForSchema(String schema, List<String> tabs){
		try {
			Connection connection = dataSource.getConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs = connection.createStatement().executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='"+schema+"'");
			
			List<Table> tables = new ArrayList<Table>();
			int count = 0;
			while (rs.next()) {
					String tN = rs.getString("table_name");
					if(tabs.contains(tN)) {
						count++;
						Table table = new Table();
						table.setId(count);
						table.setTableName(tN);
						tables.add(table);
					}
				
			  
			}
			return tables;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ReportException();
		}
	}
	
	public List<Column> getColumns2(String tableName) throws SQLException{
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		DatabaseMetaData meta = dataSource.getConnection().getMetaData();
		ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from "+tableName+" where 1<0");
		ResultSetMetaData rsmd=resultSet.getMetaData();
		List<Column> columns = new ArrayList<Column>();
		
		for(int i=1;i<=rsmd.getColumnCount();i++) {
			String field = resultSet.getMetaData().getColumnName(i);
			String title = field;
			String dt = resultSet.getMetaData().getColumnTypeName(i);
			String dataType = Util.getDynamicReportsDatatype(dt);
			Column column = new Column(title, field, dataType);
			columns.add(column);
		}
		
	    return columns;
	}
	
	public List<Column> getColumns2(String tableName, String schemaName) throws SQLException{
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		DatabaseMetaData meta = dataSource.getConnection().getMetaData();
		ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from \""+schemaName+"\".\""+tableName+"\" where 1<0");
		ResultSetMetaData rsmd=resultSet.getMetaData();
		List<Column> columns = new ArrayList<Column>();
		
		for(int i=1;i<=rsmd.getColumnCount();i++) {
			String field = resultSet.getMetaData().getColumnName(i);
			String title = field;
			String dt = resultSet.getMetaData().getColumnTypeName(i);
			String dataType = Util.getDynamicReportsDatatype(dt);
			Column column = new Column(title, field, dataType);
			columns.add(column);
		}
		
	    return columns;
	}
	
	public List<String> getColumnsMetaData(String tableName) throws SQLException{
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		DatabaseMetaData meta = dataSource.getConnection().getMetaData();
		ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from "+tableName+" where 1<0");
		ResultSetMetaData rsmd=resultSet.getMetaData();
		for(int i=1;i<=rsmd.getColumnCount();i++) {
			 System.out.println(resultSet.getMetaData().getColumnName(i));
		        System.out.println(resultSet.getMetaData().getColumnTypeName(i));
		}
		tableMetadataContext.setTableName(tableName);
	    tableMetadataContext.processMetaData(dataSource, Collections.<String>emptyList(), new String[0]);
	    return tableMetadataContext.getTableColumns();
	}
	
	public List<String> getColumnData(String schemaName, String tableName, String columnName) throws SQLException{
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		Connection con = dataSource.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from \""+schemaName+"\".\""+tableName+"\"");
		List<String> names = new ArrayList<String>();
		while (resultSet.next()) {
			names.add(resultSet.getString(columnName));
		  
		}
		con.close();
		return names;
		
	}
	
	public List<Ulb> getAllULBs() throws SQLException{
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		Connection con = dataSource.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from \"receipt\".\"tm_ulb\"");
		List<Ulb> names = new ArrayList<Ulb>();
		while (resultSet.next()) {
			Integer id = resultSet.getInt("ulb_id");
			String name = resultSet.getString("ulb_name_en");
			Ulb ulb = new Ulb();
			ulb.setUlbId(id);
			ulb.setUlbName(name);
			names.add(ulb);
		}
		con.close();
		return names;
	}
	
	public String getUlbName(Integer ulbId) throws SQLException{
		TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
		Connection con = dataSource.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from \"receipt\".\"tm_ulb\" where ulb_id = "+ulbId);
		List<Ulb> names = new ArrayList<Ulb>();
		while (resultSet.next()) {
		
			String name = resultSet.getString("ulb_name_en");
			return name;
		}
		con.close();
		return null;
	}

}
