package chinmay.com.googleplacespoc.Communication;

import chinmay.com.googleplacespoc.POJO.AutoCompleteGooglePlaces;
import chinmay.com.googleplacespoc.POJO.GetPlacesResponse;
import chinmay.com.googleplacespoc.POJO.NearBySearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public interface ApiService {


	@GET("autocomplete/json")
	Call<AutoCompleteGooglePlaces> getAutoCompleteResults(@Query("key") String API_KEY,
														  @Query("input") String encode);

	@GET("nearbysearch/json?radius=5000000&type=cities,regions,address,street_address,country,locality," +
			"administrative_area_level_3,airport,cafe,hospital,administrative_area_level_1," +
			"administrative_area_level_2,administrative_area_level_4,administrative_area_level_5," +
			"museum,doctor,police,restaurant,subway_station,gas_station,hindu_temple,atm")
	Call<NearBySearch> getNearByResults(@Query("location") String lat,
										@Query("keyword") String keyword,
										@Query("key") String key);

	@GET("nearbysearch/json")
	Call<GetPlacesResponse> getPlaceDetails(@Query("location") String location,
											@Query("radius") int radius,
											@Query("key") String key);
}
