package entity;

public class Address {
	int addr_id;
	private String address_name;
    private String place_name;
    private String road_address_name;
    private String category_name;
    private double lat;         // y
    private double lng;         // x
    
    private boolean notify;
    
	public int getAddr_id() {
		return addr_id;
	}
	public void setAddr_id(int addr_id) {
		this.addr_id = addr_id;
	}
	public String getAddress_name() {
		return address_name;
	}
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}
	public String getPlace_name() {
		return place_name;
	}
	public void setPlace_name(String place_name) {
		this.place_name = place_name;
	}
	public String getRoad_address_name() {
		return road_address_name;
	}
	public void setRoad_address_name(String road_address_name) {
		this.road_address_name = road_address_name;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public boolean isNotify() {
		return notify;
	}
	public void setNotify(boolean notify) {
		this.notify = notify;
	}
	@Override
	public String toString() {
		return "Address [addr_id=" + addr_id + ", address_name=" + address_name + ", place_name=" + place_name
				+ ", road_address_name=" + road_address_name + ", category_name=" + category_name + ", lat=" + lat
				+ ", lng=" + lng + "]";
	}
    
    
}
