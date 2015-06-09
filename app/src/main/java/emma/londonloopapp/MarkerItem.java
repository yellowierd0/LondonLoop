package emma.londonloopapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Emma on 09/06/2015.
 */
public class MarkerItem {

    private long id;
    private SectionItem section;
    private LatLng location;
    private String name;
    private String text;
    private String url;

    public MarkerItem(){

    }
    public MarkerItem(long id, SectionItem section, LatLng location,String name, String text, String url){
        this.id = id;
        this.section = section;
        this.location = location;
        this.name = name;
        this.text = text;
        this.url = url;
    }

    public MarkerItem(long id, SectionItem section, LatLng location, String name){
        this.id = id;
        this.section = section;
        this.location = location;
        this.name = name;
        this.text = "";
        this.url = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SectionItem getSection() {
        return section;
    }

    public void setSection(SectionItem section) {
        this.section = section;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
