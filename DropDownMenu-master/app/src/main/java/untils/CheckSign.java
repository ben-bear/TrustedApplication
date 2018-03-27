package untils;

import junit.framework.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import poi.DefinedPoi;
import poi.TestObjectPoi;

/**
 * Created by Administrator on 2017/11/7.
 */

public class CheckSign {

        public static int MAX1 = 90;
        public static int MIN1 = 0;
        public static int MAX2 = 180;
        public static int MIN2 = 0;
        public boolean check(String p) throws UnsupportedEncodingException, NoSuchAlgorithmException {

            String cmpdigest = "";
            double a1 = Double.valueOf(p.substring(0, p.indexOf(".") + 7));
            double a2 = Double.valueOf(p.substring(p.indexOf(".") + 7, p.length()));
            TestObjectPoi cmppoi = new TestObjectPoi(a1, a2);

            cmpdigest = Digest.getDigest(cmppoi);

            Digest digest = new Digest();
            List<String> cmp = digest.get_digestchain();
            if (cmp.contains(cmpdigest)) {
                return true;
            }
            return false;

        }
}
