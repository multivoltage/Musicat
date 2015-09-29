/**
 * 
 */
package com.multivoltage.musicat.gcs;

import com.multivoltage.musicat.entity.Image;

import java.io.Serializable;

/**
 * @author Rashidi Zin
 * @version 1.1.0
 * @since 1.0.0
 */
public class Item implements Serializable {

	private String link;
	private Image image;

	public Item(){}

	public void setLink(String link) {
		this.link = link;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getLink() {
		return link;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public String toString(){

		return "width: "+getImage().getWidth()+" ,height: "+getImage().getHeight();
	}

}
