package ys.app.pad.widget.autoview;

import java.io.Serializable;
import java.util.List;

import ys.app.pad.itemmodel.NumCardEntityInfo;
import ys.app.pad.model.ServiceInfo;

/**
 * Created by WBJ on 2018/3/30 13:05.
 */

public class NumCardParamBean implements Serializable{
    private String header;
    private String footer;
    private List<NumCardEntityInfo> lis;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public List<NumCardEntityInfo> getLis() {
        return lis;
    }

    public void setLis(List<NumCardEntityInfo> lis) {
        this.lis = lis;
    }
}
