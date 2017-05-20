package chinmay.com.googleplacespoc.POJO;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class AutoCompletePlacesMessageEvent {
	public final int status;
	public final AutoCompleteGooglePlaces response;
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;

	public AutoCompletePlacesMessageEvent(int status,
										  AutoCompleteGooglePlaces response) {
		this.status = status;
		this.response = response;
	}

	public boolean isSuccess(){
		return status == 0;
	}

}
