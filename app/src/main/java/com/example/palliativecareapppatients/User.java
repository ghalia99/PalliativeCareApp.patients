public class User {

    private String firstName;
    private String middleName;
    private String familyName;
    private String address;
    private String mobileNo;
    private String type;

    public User() {
        // Empty constructor needed for Firebase
    }

    public User(String firstName, String middleName, String familyName, String address, String mobileNo, String type) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.familyName = familyName;
        this.address = address;
        this.mobileNo = mobileNo;
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
