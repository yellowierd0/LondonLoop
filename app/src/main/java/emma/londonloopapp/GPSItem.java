package emma.londonloopapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Emma on 14/05/2015.
 */
public class GPSItem {

    private SectionItem sectionItem;
    private LatLng latLng;
    private int incr;
    private long id;
    private String note;

    public GPSItem(){

    }

    public GPSItem(int incr, LatLng latLng, SectionItem sectionItem, String note){
        this.incr = incr;
        this.latLng = latLng;
        this.sectionItem = sectionItem;
        this.note = note;
    }

    public GPSItem(long id, int incr, LatLng latLng, SectionItem sectionItem, String note){
        this.id = id;
        this.incr = incr;
        this.latLng = latLng;
        this.sectionItem = sectionItem;
        this.note = note;
    }

    public SectionItem getSectionItem() {
        return sectionItem;
    }

    public void setSectionItem(SectionItem sectionItem) {
        this.sectionItem = sectionItem;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getIncr() {
        return incr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIncr(int incr) {
        this.incr = incr;
    }

    public void setNote(String note) { this.note = note; }

    public String getNote() { return note; }

}
