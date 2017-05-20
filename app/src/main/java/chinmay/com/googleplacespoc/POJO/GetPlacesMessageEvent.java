package chinmay.com.googleplacespoc.POJO;

import java.util.List;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class GetPlacesMessageEvent {
	public final int status;
	public final GetPlacesResponse response;
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;

	public GetPlacesMessageEvent(int status,
								 GetPlacesResponse response) {
		this.status = status;
		this.response = response;
	}

	public boolean isSuccess(){
		return status == 0;
	}
}

