package com.innowave.mahaulb.reports.data;

public enum JoinType {
	
	NONE("NONE"), JOIN("JOIN"), LEFT_JOIN("LEFT_JOIN"), FULL_JOIN("FULL_JOIN"), RIGHT_JOIN("RIGHT_JOIN"), CROSS_JOIN("CROSS_JOIN");

	private String type;
	private JoinType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
	
}
