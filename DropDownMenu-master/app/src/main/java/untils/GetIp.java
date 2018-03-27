package untils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2018/1/16.
 */

public class GetIp {
    private String localIp = "192.168.191.1";
    private String netIp = "192.168.0.1";

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getNetIp() {
        return netIp;
    }

    public void setNetIp(String netIp) {
        this.netIp = netIp;
    }
}
