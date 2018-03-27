package poi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class DefinedPoi implements Serializable {
    private static final long serialVersionUID = 1L;

    double latitude;
    double longtitude;
    String name;
    String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public DefinedPoi(double latitude, double longtitude){
        this.latitude = latitude;
        this.longtitude = longtitude;
    }
    public DefinedPoi(){

    }
    public DefinedPoi(double latitude,double longtitude,String name,String info){
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
        this.info = info;

    }
    public static List<DefinedPoi> getDenfinedPoi(){
        List<DefinedPoi> allPoi = new ArrayList<DefinedPoi>();
        allPoi.add(new DefinedPoi(22.641801,101.643801));
        allPoi.add(new DefinedPoi(25.531401,122.643801));
        allPoi.add(new DefinedPoi(27.231401,113.643801));
        allPoi.add(new DefinedPoi(27.536401,110.643801));
        allPoi.add(new DefinedPoi(27.736331,130.643801));
        allPoi.add(new DefinedPoi(28.14381,140.643801));
        allPoi.add(new DefinedPoi(28.943801,110.643801));
        allPoi.add(new DefinedPoi(30.141801,113.643801));
        allPoi.add(new DefinedPoi(39.106941,116.397972));
        allPoi.add(new DefinedPoi(39.906901,115.397972));
        allPoi.add(new DefinedPoi(49.106941,112.397972));

        return allPoi;
    }
}
