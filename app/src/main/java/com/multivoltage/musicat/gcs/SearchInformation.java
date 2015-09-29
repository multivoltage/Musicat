/**
 * 
 */
package com.multivoltage.musicat.gcs;

/**
 * @author Rashidi Zin
 * @version 1.0.0
 * @since 1.0.0
 */
public class SearchInformation {

	private float searchTime;
	private String totalResults;
	
	public SearchInformation() {
		
	}
	
	public float getSearchTime() {
		return searchTime;
	}
	
	public void setSearchTime(float searchTime) {
		this.searchTime = searchTime;
	}
	
	public String getTotalResults() {
		return totalResults;
	}
	
	public void setTotalResults(String totalResults) {
		this.totalResults = totalResults;
	}

	@Override
	public String toString(){
		return ""+searchTime+"sec, result: "+totalResults;
	}
}
