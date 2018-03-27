package untils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;

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
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import poi.DefinedPoi;
import poi.HttpRequest;
import poi.HttpResponse;

/**
 * Created by Administrator on 2017/11/7.
 */

public class ThreadClient implements Runnable {
    private Socket socket;
    private Handler mhandler;
    private String name;


    public ThreadClient(Handler handler,String name ) {
        this.mhandler = handler;
        this.name = name;
    }

    @Override
    public void run() {
        Looper.prepare();
        Message msg = Message.obtain();
        msg.what = 0x11;
        Bundle bundle = new Bundle();
        bundle.clear();
//        String SOCKET_IP = "169.254.198.122";
        String SOCKET_IP = new GetIp().getLocalIp();
//        String SOCKET_IP ="192.168.191.1";
        int SCOKET_PORT = 30000;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SOCKET_IP,SCOKET_PORT),5000);
            //在请求体中写入请求类型 distanceRequest 自己的位置myposition
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setRequest("searchContentRequest");
            httpRequest.setSearchContent(name);
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                oos.writeObject(httpRequest);
                oos.flush();

                HttpResponse httpResponse = (HttpResponse) ois.readObject();
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
