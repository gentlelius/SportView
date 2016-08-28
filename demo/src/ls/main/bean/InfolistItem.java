package ls.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/8/23.
 */
public class InfolistItem implements Serializable{
    /**
     * id : 1
     * mUser : {"unick":"OKSPORT","age":0,"sex":"男","adress":"HBUT","icon_url":"null"}
     * content : 第一条说说，没有图片，哈哈哈哈哈
     * time : 1471846994538
     * like_count : 0
     * comment_count : 0
     */

    private int id;
    /**
     * unick : OKSPORT
     * age : 0
     * sex : 男
     * adress : HBUT
     * icon_url : null
     */

    private MUserBean mUser;
    private String content;
    private String time;
    private int like_count;
    private int comment_count;

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }

    private List<String> img_list;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MUserBean getMUser() {
        return mUser;
    }

    public void setMUser(MUserBean mUser) {
        this.mUser = mUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public static class MUserBean implements Serializable {
        private String unick;
        private int age;
        private String sex;
        private String adress;
        private String icon_url;

        public String getUnick() {
            return unick;
        }

        public void setUnick(String unick) {
            this.unick = unick;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof InfolistItem) {
            return this.id == ((InfolistItem) o).id;
        }
        return super.equals(o);
    }

}
