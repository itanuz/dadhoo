/**
 * 
 */
package com.dadhoo.database.pojo;

/**
 * This class represents a POJO of an Album item
 * @author gaecarme
 *
 */
public class Album {
	private String title;
	private int id;
	private String pictureId;
	private String timestamp;
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}
	public int getId() {
		return id;
	}
	public String getPictureId() {
		return pictureId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	

}
