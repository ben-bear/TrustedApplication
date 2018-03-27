package poi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/8.
 */

public class HttpResponse implements Serializable {
    Map<Integer, TestObjectPoi> poi = new HashMap<>();
    Map<Integer, String> verifyPoi = new HashMap<>();
    Map<Integer, TestObjectPoi> correctPoi = new HashMap<>();


    public Map<Integer, TestObjectPoi> getCorrectPoi() {
        return correctPoi;
    }

    public void setCorrectPoi(Map<Integer, TestObjectPoi> correctPoi) {
        this.correctPoi = correctPoi;
    }

    public Map<Integer, String> getVerifyPoi() {
        return verifyPoi;
    }

    public void setVPoi(Map<Integer, String> poi) {
        this.verifyPoi = poi;
    }

    public Map<Integer, TestObjectPoi> getPoi() {
        return poi;
    }


    public void setPoi(Map<Integer, TestObjectPoi> poi) {
        this.poi = poi;
    }

    public HttpResponse(Map<Integer, TestObjectPoi> poi) {
        this.poi = poi;
    }

    public HttpResponse(Map<Integer, TestObjectPoi> poi, Map<Integer, String> vPoi) {
        this.poi = poi;
        this.verifyPoi = vPoi;
    }
    public HttpResponse(Map<Integer, TestObjectPoi> Poi,Map<Integer, String> verifyPoi, Map<Integer, TestObjectPoi> correctPoi){
        this.poi = Poi;
        this.verifyPoi = verifyPoi;
        this.correctPoi = correctPoi;
    }
    public HttpResponse() {
    }
}
