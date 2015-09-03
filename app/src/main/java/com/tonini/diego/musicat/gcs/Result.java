/**
 * 
 */
package com.tonini.diego.musicat.gcs;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rashidi Zin
 * @version 1.0.0
 * @since 1.0.0
 */
public class Result implements Serializable{

	private String kind;
	private List<Item> items;
	
	public Result() {
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}


}
