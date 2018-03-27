package untils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.interfaces.ECPublicKey;
import java.util.List;
import java.util.Map;

import poi.HttpRequest;
import poi.HttpResponse;
import poi.TestObjectPoi;

/**
 * Created by Administrator on 2018/1/14.
 */

public class ThreadCorrect extends Thread {
    private Socket socket;
    private Handler mhandler;
    private List<Integer> correct;


    public ThreadCorrect(Handler handler, List<Integer> correct) {
        this.mhandler = handler;
        this.correct = correct;
    }

    @Override
    public void run() {
        Looper.prepare();
        Message msg = Message.obtain();
        msg.what = 205;
        Bundle bundle = new Bundle();
        bundle.clear();
//        String SOCKET_IP = "169.254.198.122";
     String SOCKET_IP = new GetIp().getLocalIp();
// String SOCKET_IP ="192.168.191.1";
        int SCOKET_PORT = 30000;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SOCKET_IP,SCOKET_PORT),5000);
            //在请求体中写入请求类型 distanceRequest 自己的位置myposition
            HttpRequest httpRequest = new HttpRequest("correctRequest", correct);

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
