package two.one.contactfetch.entities;

public class Device {
    private long id;
    private String Imei;

    public Device(long id, String imei) {
        this.id = id;
        Imei = imei;
    }

    public Device(String imei) {
        Imei = imei;
    }

    public Device() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }
}
