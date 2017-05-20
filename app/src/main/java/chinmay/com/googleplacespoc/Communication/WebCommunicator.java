package chinmay.com.googleplacespoc.Communication;

import android.location.Location;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Executors;

import chinmay.com.googleplacespoc.POJO.AutoCompleteGooglePlaces;
import chinmay.com.googleplacespoc.POJO.AutoCompletePlacesMessageEvent;
import chinmay.com.googleplacespoc.POJO.NearBySearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class WebCommunicator {
	private static final String API_KEY = "AIzaSyC0GFzk0HjRHHkYW6nHWnXAyWZ7DcWQy4M";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/";

	public static void getAutoCompleteResults(String input){
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(PLACES_API_BASE)
				.addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
				.callbackExecutor(Executors.newSingleThreadExecutor())
				.build();
		String encoded = "";
		try {
			encoded = URLEncoder.encode(input, "utf8");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		chinmay.com.googleplacespoc.Communication.ApiService requestInterface = retrofit.create(chinmay.com.googleplacespoc.Communication.ApiService.class);
		Call<AutoCompleteGooglePlaces> call = requestInterface.getAutoCompleteResults(API_KEY,
				encoded);
		call.enqueue(new Callback<AutoCompleteGooglePlaces>() {
			@Override
			public void onResponse(Call<AutoCompleteGooglePlaces> call, Response<AutoCompleteGooglePlaces> response) {
				EventBus.getDefault().post(new AutoCompletePlacesMessageEvent("Success YAY!"));
			}

			@Override
			public void onFailure(Call<AutoCompleteGooglePlaces> call, Throwable t) {
				EventBus.getDefault().post(new AutoCompletePlacesMessageEvent("Fail NAY!"));
			}
		});
	}

	public static void getNearByResults(String input, Location mLastLocation) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(PLACES_API_BASE)
				.addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
				.callbackExecutor(Executors.newSingleThreadExecutor())
				.build();
		String encoded = "";
		try {
			encoded = URLEncoder.encode(input, "utf8");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		ApiService requestInterface = retrofit.create(ApiService.class);
		Call<NearBySearch> call = requestInterface.getNearByResults(mLastLocation.getLatitude() +","+mLastLocation.getLongitude(),
				encoded, API_KEY);
		call.enqueue(new Callback<NearBySearch>() {
				@Override
				public void onResponse(Call<NearBySearch> call, Response<NearBySearch> response) {

				}

				@Override
				public void onFailure(Call<NearBySearch> call, Throwable t) {

				}
			});
	}



	private static Gson getGsonInstance(){
		return new GsonBuilder().setExclusionStrategies(new  ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				return false;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		}).create();
	}
}
