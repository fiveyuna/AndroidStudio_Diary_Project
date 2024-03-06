package ddwu.mobile.finalproject.ma01_20190978;

import java.io.Serializable;

/*
하나의 주소 정보를 저장하기 위한 DTO
Intent 에 저장 가능하게 하기 위하여
Serializable 인터페이스를 구현함
*/

public class RoutineDto implements Serializable {

	private long id;
	private String time;
	private String did;
	private String date;

	public RoutineDto() {
		this.id = 0;
		this.time = "";
		this.did = "";
		this.date = "";
	}

	public RoutineDto(long id, String time, String did, String date) {
		this.id = id;
		this.time = time;
		this.did = did;
		this.date = date;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setTime(String time) { this.time = time; }
	public String getTime() { return time; }
	public String getDid() { return did; }
	public void setDid(String did) { this.did = did; }

	public String getDate() { return date; }
	public void setDate(String date) { this.date = date; }

	@Override
	public String toString() {
		return id + ". " + did + " - " + time;
	}
	
}
