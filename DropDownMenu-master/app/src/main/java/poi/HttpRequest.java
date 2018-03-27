package poi;

        import junit.framework.Test;

        import java.io.Serializable;
        import java.security.interfaces.ECPrivateKey;
        import java.security.interfaces.ECPublicKey;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

/**
 * Created by Administrator on 2018/1/8.
 */

public class HttpRequest implements Serializable{
    //查询类型
    String request;
    //自己的位置
    TestObjectPoi myPostion;
    //测试用的 没什么用
    Map<Integer,TestObjectPoi> poi = new HashMap<>();
    //加入是距离查询，需要传入查询的范围
    int range;
    ECPublicKey ecPublicKey;
    //纠错表
    List<Integer> listOfCorrect = new ArrayList<>();
    //地图中的查询条件
    String searchContent;

    public String getSearchContent() {
        return searchContent;
    }
    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }
    public List<Integer> getListOfCorrect() {
        return listOfCorrect;
    }
    public void setListOfCorrect(List<Integer> listOfCorrect) {
        this.listOfCorrect = listOfCorrect;
    }
    public void setEcPublicKey(ECPublicKey ecPrivateKey) {
        this.ecPublicKey = ecPrivateKey;
    }
    public ECPublicKey getEcPublicKey() {
        return ecPublicKey;
    }
    public int getRange() {
        return range;
    }
    public void setRange(int range) {
        this.range = range;
    }

    public String getRequest() {
        return request;
    }

    public Map<Integer, TestObjectPoi> getPoi() {
        return poi;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setPoi(Map<Integer, TestObjectPoi> poi) {
        this.poi = poi;
    }

    public void setMypostion(TestObjectPoi mypostion) {
        this.myPostion = mypostion;
    }

    public TestObjectPoi getMypostion() {
        return myPostion;
    }

    public HttpRequest(String request ,TestObjectPoi myPostion,int range,ECPublicKey ecPublicKey){
        this.request = request;
        this.poi = poi;
        this.range = range;
        this.ecPublicKey = ecPublicKey;
    }
    public HttpRequest (String request ,TestObjectPoi mypostion,int range) {
        this.request = request;
        this.myPostion = mypostion;
        this.range = range;

    }
    public HttpRequest(String request,List<Integer> listOfCorrect){
        this.request = request;
        this.listOfCorrect = listOfCorrect;
    }
    public HttpRequest(String request ,TestObjectPoi mypostion){
        this.request = request;
        this.myPostion = mypostion;
    }
    public HttpRequest(String request){
        this.request = request;
    }
    public HttpRequest(){

    }
}
