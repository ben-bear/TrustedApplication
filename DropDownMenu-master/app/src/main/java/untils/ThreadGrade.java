package untils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poi.DefinedPoi;
import poi.HttpRequest;
import poi.HttpResponse;
import poi.TestObjectPoi;

/**
 * Created by Administrator on 2017/12/19.
 * 智能排序中的线程,用于智能排序，距离最近排序等
 */

public class ThreadGrade implements Runnable {
    private Socket socket;
    private Handler mhandler;
    private int postion;

    Map<Integer,TestObjectPoi> httpRequestModel = new HashMap<>();

    TestObjectPoi tPoi = new TestObjectPoi();
    public ThreadGrade(Handler handler,int postion) {
        this.mhandler = handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        Message msg = Message.obtain();
        msg.what = 300;
        Bundle bundle = new Bundle();
        bundle.clear();
        String SOCKET_IP = new GetIp().getLocalIp();
 //       String SOCKET_IP ="49.123.68.22";
        int SCOKET_PORT = 30000;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SOCKET_IP,SCOKET_PORT),5000);
            /**
             * 第一个参数是查询类型 第二个参数是自己位置信息，保存在HttpRequest中
             */
            HttpRequest httpRequest = new HttpRequest("gradeRequest",new TestObjectPoi(12.000001,10.111111));

            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                //发送查询请求
                oos.writeObject(httpRequest);
                oos.flush();

                HttpResponse httpResponse = (HttpResponse) ois.readObject();
                for (Map.Entry<Integer, TestObjectPoi> entry : httpResponse.getPoi().entrySet()) {
                    System.out.println("key= " + entry.getKey() +
                            " and value= " + entry.getValue().getName());
                }

                msg.obj = httpResponse;
                mhandler.sendMessage(msg);
            }
            catch (Exception e){
                e.printStackTrace();
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
