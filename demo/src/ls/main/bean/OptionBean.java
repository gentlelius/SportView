package ls.main.bean;

/**
 * Created by admin on 2016/8/26.
 */
public class OptionBean {
    private String title;
    private int icon_url;
    private String content;
    private int title_color;

    public int getTitle_color() {
        return title_color;
    }

    public void setTitle_color(int title_color) {
        this.title_color = title_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(int icon_url) {
        this.icon_url = icon_url;
    }
}
