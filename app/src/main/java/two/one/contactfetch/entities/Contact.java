package two.one.contactfetch.entities;

public class Contact {
    private long id;
    private String nom;
    private String telephone;
    private Device device;

    public Contact(long id, String nom, String telephone, Device device) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.device = device;
    }

    public Contact(String nom, String telephone, Device device) {
        this.nom = nom;
        this.telephone = telephone;
        this.device = device;
    }

    public Contact() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}

