package untils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import junit.framework.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;

import poi.DefinedPoi;
import poi.HttpRequest;
import poi.HttpResponse;
import poi.TestObjectPoi;
import untils.GetIp;
/**
 * Created by Administrator on 2018/1/8.
 */

public class ThreadDistance implements Runnable {
    private Socket socket;
    private Handler mhandler;
    private TestObjectPoi myPosition;
    private int range;
    private ECPublicKey ecPublicKey;

    public ThreadDistance(Handler handler,TestObjectPoi position,int range) {
        this.mhandler = handler;
        this.myPosition = position;
        this.range  = range;
    }

    @Override
    public void run() {
        Looper.prepare();
        Message msg = Message.obtain();
        msg.what = 200;
        Bundle bundle = new Bundle();
        bundle.clear();
//        String SOCKET_IP = "169.254.198.122";
        String SOCKET_IP = new GetIp().getLocalIp();
 //       String SOCKET_IP ="49.123.68.22";
        int SCOKET_PORT = 30000;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SOCKET_IP,SCOKET_PORT),5000);
            //在请求体中写入请求类型 distanceRequest 自己的位置myposition
            HttpRequest httpRequest = new HttpRequest("distanceRequest",myPosition,range);

            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                oos.writeObject(httpRequest);
                oos.flush();

                HttpResponse httpResponse = (HttpResponse) ois.readObject();
                //遍历放回的Response对象中的正确的POI数据
                for (Map.Entry<Integer, TestObjectPoi> entry : httpResponse.getPoi().entrySet()) {
                    System.out.println("key= " + entry.getKey() +
                            " and value= " + entry.getValue().getName());
                }
                msg.obj = httpResponse;
                mhandler.sendMessage(msg);
            }
            catch (Exception e){

            }
            finally {
                try {
                    ois.close();
                }
                catch(Exception ex) {}
                try {
                    oos.close();
                } catch(Exception ex) {}
                try {
                    socket.close();
                } catch(Exception ex) {}
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        Looper.loop();
    }
}
