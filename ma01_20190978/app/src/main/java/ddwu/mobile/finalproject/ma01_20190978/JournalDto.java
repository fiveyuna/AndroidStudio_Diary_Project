package ddwu.mobile.finalproject.ma01_20190978;

import java.io.Serializable;


public class JournalDto implements Serializable {

	private long id;
	private String title;
	private String content;
	private String feeling;
	private String date;
	private String location;
	private String image;

	public JournalDto() {
		id = 0;
		title = "";
		content = "";
		feeling = "";
		date = "";
		location = "";
		image = "";
	}

	public JournalDto(long id, String title, String content, String feeling, String date, String location, String image) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.feeling = feeling;
		this.date = date;
		this.location = location;
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFeeling() {
		return feeling;
	}

	public void setFeeling(String feeling) {
		this.feeling = feeling;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "JournalDto{" +
				"id=" + id +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", feeling='" + feeling + '\'' +
				", date='" + date + '\'' +
				", location='" + location + '\'' +
				", image='" + image + '\'' +
				'}';
	}
}
