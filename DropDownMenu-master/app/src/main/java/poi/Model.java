package poi;

/************************************************
 * author:       火中燕.
 * email:        wangzhong0116@foxmail.com.
 * version:      1.0.1.
 * date:         2016/12/9 20:52.
 * description:
 ***********************************************/
public class Model {
    public String name;
    public int iconRes;

    public Model(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}