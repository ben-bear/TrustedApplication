package untils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Administrator on 2018/1/12.
 */

public class ECSign{

        public static byte[] sign(String src,ECPrivateKey ecPrivateKey){
            byte[] sign = null;
            try {

                //2.执行签名
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());

                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                Signature signature = Signature.getInstance("SHA1withECDSA");
                signature.initSign(privateKey);
                signature.update(src.getBytes());
                byte[] res = signature.sign();
                sign = res;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return sign;
        }

        public static boolean design(String sig1,byte[] sign,ECPublicKey ecPublicKey){
            boolean bool = false;
            try {

                //3.验证签名
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());

                KeyFactory keyFactory = KeyFactory.getInstance("EC");
                Signature signature = Signature.getInstance("SHA1withECDSA");

                keyFactory = KeyFactory.getInstance("EC");
                PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
                signature = Signature.getInstance("SHA1withECDSA");
                signature.initVerify(publicKey);
                signature.update(sig1.getBytes());
                bool = signature.verify(sign);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return bool;
        }


}
