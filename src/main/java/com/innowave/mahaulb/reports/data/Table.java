package com.innowave.mahaulb.reports.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innowave.mahaulb.reports.manager.Column;

public class Table {
	
	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long identity;

	
	List<Column> columns = new ArrayList<Column>();
	
	private Integer id;

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	String tableName;
	public Table() {
		
	}
	public Table(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getIdentity() {
		return identity;
	}

	public void setIdentity(Long identity) {
		this.identity = identity;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Table)) {
			return false;
		}
		
		Table t2 = (Table) o;
		if(this.getTableName().equals(t2.getTableName())) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return getTableName().hashCode();
	}
	
	
}
