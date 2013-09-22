/**
 * 
 */
package mx.com.recomendare.web.commons.models;

/**
 * @author jerry
 *
 */
public final class UserModelImpl implements UserModel {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String firstName;
	private String lastName;
	private String screenName;
	private String login;
	private String password;
	private int genderId;
	private String genderName;
	private BirthDateModel birthDate;
	private String lastIPAddress;
	private AddressModel address;
	private LocationModel location;
	private PictureModel avatarPicture;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AddressModel getAddress() {
		return address;
	}
	public void setAddress(AddressModel address) {
		this.address = address;
	}
	public LocationModel getLocation() {
		return location;
	}
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	public PictureModel getAvatarPicture() {
		return avatarPicture;
	}
	public void setAvatarPicture(PictureModel avatarPicture) {
		this.avatarPicture = avatarPicture;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public String getGenderName() {
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
	public BirthDateModel getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(BirthDateModel birthDate) {
		this.birthDate = birthDate;
	}
	public String getLastIPAddress() {
		return lastIPAddress;
	}
	public void setLastIPAddress(String lastIPAddress) {
		this.lastIPAddress = lastIPAddress;
	}
	
}//END OF FILE