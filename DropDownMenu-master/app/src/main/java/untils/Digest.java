package untils;

import junit.framework.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import poi.DefinedPoi;
import poi.TestObjectPoi;

/**
 * Created by Administrator on 2017/11/7.
 */

public class Digest {
    public static int MAX1 = 90;
    public static int MIN1 = 0;
    public static int MAX2 = 180;
    public static int MIN2 = 0;
    List<String> digestchain = new ArrayList<String>();
    public static String get_Udigest(Double value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String digest = String.valueOf(value);
        int count = (int) (MAX2-value-1);

        for(int i=0;i<count;i++){
            digest = MD5.md5(digest);
        }
        return digest;
    }
    public static String get_Ldigest(Double value) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String digest = String.valueOf(value);

        int count = (int) (value-MIN2-1);

        for(int i=0;i<count;i++){
            digest = MD5.md5(digest);
        }
        return digest;
    }
    public static String getDigest(TestObjectPoi poi) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String digest_x1 = "";
        String digest_x2 = "";
        String digest_y1 = "";
        String digest_y2 = "";
        digest_x1 = Digest.get_Udigest(poi.getLatitude());
        digest_x2 = Digest.get_Ldigest(poi.getLatitude());
        digest_y1 = Digest.get_Udigest(poi.getLongtitude());
        digest_y2 = Digest.get_Ldigest(poi.getLongtitude());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(digest_x1).append(digest_x2).
                append(digest_y1).append(digest_y2);
        String digest_temp = stringBuffer.toString();
        return digest_temp;
    }

    public List<String> get_digestchain() {
        digestchain.add("f1ea979d3dca9d45fdc676e9d2fb8cf7cbc6fdcb4e8178d2d040568d1a689daf101.6438011bafe440a8e46e0e52959bf05a66b9e0");
        digestchain.add("3731a8fa6f5cb09ca662e8453f6baf08c0fb5978cf50a368f61e23ab2610aaa7122.64380135396c24d58a081b75d0ec8a8a7137b4");
        digestchain.add("4cc3b5d31147ca8d7bbce2bcb1b49b16b44089fec8391c6aef86e5331a54d433113.6438013ac98c45b5f7fef67c07da61e3ece615");
        digestchain.add("280436aef3f31c70d8b1a1f88db10cac36b6b5ec7e918fa504f34eb08af80f7c110.6438012de4fa63c48bc7f7c4e5e042d6193312");
        digestchain.add("62bd8baef47afeeb9630c7b32c4c52c5f25c25cf47e83d8c2379d5cd991b1b49130.643801546098a9e99adcf7217b292461391ab5");
        digestchain.add("c22f4532a5f2565ca061a58e1b46042e631942e88dfb3ffbf0fc2bf5de99e622140.643801ec0460bca31de9922bb50268740c462a");
        digestchain.add("abf4144feeaefd34f3846c1d66cb35360d6c4accacdd2629e109262ba5aa4db6110.6438012de4fa63c48bc7f7c4e5e042d6193312");
        digestchain.add("d92b2d56365bad518be71d700bee844ad5c42d699809d0d99c36928571ba5cf1113.6438013ac98c45b5f7fef67c07da61e3ece615");
        digestchain.add("35ada471624a1af4aa5aeab6669471df9dcb68bec5cc60edf60e3265a376f081116.3979729af34da06c7f2947a4647912e9f7dfa3");
        digestchain.add("667e0e423cbdbd41d214131ee384d765b4e80c0408ab78d00760d06f7bcd93f0115.3979727470d828651c1c3082fe11a166e15075");
        digestchain.add("7a66225ab37e32fb31a48c772356a05d1f2f950f0675972b871e86627c6aec48112.3979724772ccaf5f393be87d986e2b69c02eab");
        return digestchain;
    }


}
