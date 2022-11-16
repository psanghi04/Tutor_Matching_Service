public class Store {
    private String storeName;
    private Tutor owner;
    private String address;

    public Store(String storeName, Tutor owner, String address) {
        this.storeName = storeName;
        this.owner = owner;
        this.address = address;
    }

    public String getStoreName() {
        return storeName;
    }

    public Tutor getOwner() {
        return owner;
    }

    public String getAddress() {
        return address;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setOwner(Tutor owner) {
        this.owner = owner;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
