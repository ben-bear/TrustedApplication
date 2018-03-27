package poi;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/8.
 */

public class TestObjectPoi implements Serializable{
    private static final long serialVersionUID = 1L;
    double latitude;
    double longtitude;
    String name;
    String address;
    int distanceAwayFrom;
    byte[] signature;

    public byte[] getSignature() {
        return signature;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    public int getDistanceAwayFrom() {
        return distanceAwayFrom;
    }
    public void setDistanceAwayFrom(int distanceAwayFrom) {
        this.distanceAwayFrom = distanceAwayFrom;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongtitude() {
        return longtitude;
    }
    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public TestObjectPoi(double latitude, double longtitude){
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
    public TestObjectPoi(String name,String add,double latitude, double longtitude){
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
    public TestObjectPoi(){

    }
}
