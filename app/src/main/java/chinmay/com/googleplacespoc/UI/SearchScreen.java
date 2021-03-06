package chinmay.com.googleplacespoc.UI;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import chinmay.com.googleplacespoc.Communication.WebCommunicator;
import chinmay.com.googleplacespoc.POJO.AutoCompleteGooglePlaces;
import chinmay.com.googleplacespoc.POJO.AutoCompletePlacesMessageEvent;
import chinmay.com.googleplacespoc.R;
import chinmay.com.googleplacespoc.databinding.ActivitySearchScreenBinding;

public class SearchScreen extends AppCompatActivity implements AdapterView.OnItemClickListener{

	private ActivitySearchScreenBinding activitySearchScreenBinding;
	private ArrayList<PlaceObject> resultList;
	private static final String _TAG = SearchScreen.class.getSimpleName();
	private Location mLastLocation;
	private GooglePlacesAutocompleteAdapter googlePlacesAutocompleteAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activitySearchScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_screen);
		resultList = new ArrayList<>();
		googlePlacesAutocompleteAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.list_item);
		activitySearchScreenBinding.autoCompleteTextView.setAdapter(googlePlacesAutocompleteAdapter);
		activitySearchScreenBinding.autoCompleteTextView.setOnItemClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	private class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {

		public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			if(resultList != null) {
				return resultList.size();
			}
			return 0;
		}

		@Override
		public String getItem(int index) {
			if (resultList != null && index < resultList.size()) {
				if (!resultList.get(index).getPlaceVicinity().equals("")) {
					return resultList.get(index).getPlaceName() + ", " + resultList.get(index).getPlaceVicinity();
				} else {
					return resultList.get(index).getPlaceName();
				}
			}
			return "";
		}

		public PlaceObject getSpecificItem(int index){
			if( index < resultList.size()  ){
				return resultList.get(index);
			}
			return null;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			PlaceObject placeObject = getSpecificItem(position);
			if( convertView != null ){
				convertView.setTag(placeObject);
			}
			View v = super.getView(position, convertView, parent);
			v.setTag(placeObject);
			return v;
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(final CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						autocomplete(constraint.toString());
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}

					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	class PlaceObject {
		public String getPlaceName() {
			return placeName;
		}

		public void setPlaceName(String placeName) {
			this.placeName = placeName;
		}

		private String placeName;

		public String getPlaceVicinity() {
			return placeVicinity;
		}

		public void setPlaceVicinity(String placeVicinity) {
			this.placeVicinity = placeVicinity;
		}

		private String placeVicinity;

		public String getPlaceId() {
			return placeId;
		}

		public void setPlaceId(String placeId) {
			this.placeId = placeId;
		}

		private String placeId;

		public double getLat() {
			return lat;
		}

		public double getLng() {
			return lng;
		}

		private double lat;
		private double lng;

		public PlaceObject(String pN, String pVicinity, String pID, double lat, double lng) {
			placeName = pN;
			placeVicinity = pVicinity;
			placeId = pID;
			this.lat = lat;
			this.lng = lng;
		}

	}

	public void autocomplete(String input) {
		WebCommunicator.getAutoCompleteResults(input);
		//WebCommunicator.getNearByResults(input, mLastLocation);
	}

	public void dataReceived(AutoCompleteGooglePlaces autoCompleteGooglePlaces) {
		try {
			/*NearBySearch nearByResults = null;
			if(nearByResults != null &&
					nearByResults.getResults() != null && nearByResults.getResults().size() > 0 ) {
				List<NearBySearch.Results> results = nearByResults.getResults();
				for (int j = 0; j < results.size(); j++) {
					if (j > 4) {
						break;
					} else {
						double lat = 0;
						double lng = 0;
						NearBySearch.Geometry geometry = results.get(j).getGeometry();
						if (geometry != null) {
							NearBySearch.Location location = geometry.getLocation();
							if (location != null) {
								lat = Double.valueOf(location.getLat());
								lng = Double.valueOf(location.getLng());
							}
						}
						PlaceObject pOBject1 = new PlaceObject(results.get(j).getName(),
								results.get(j).getVicinity(),
								results.get(j).getPlace_id(),
								lat, lng);
						resultList.add(pOBject1);
					}
				}
			}*/

			if(autoCompleteGooglePlaces != null &&
					autoCompleteGooglePlaces.getPredictions() != null && autoCompleteGooglePlaces.getPredictions().size() > 0 ) {
				List<AutoCompleteGooglePlaces.Predictions> predictions = autoCompleteGooglePlaces.getPredictions();
				if( predictions != null && predictions.size() > 0){
					resultList.clear();
				}
				for (int i = 0; i < predictions.size(); i++) {
					AutoCompleteGooglePlaces.Predictions prediction = predictions.get(i);
					PlaceObject pOBject = new PlaceObject(prediction.getDescription(),
							"",
							prediction.getPlace_id(),
							0, 0 );
					resultList.add(pOBject);
				}
			}
			// Assign the data to the FilterResults
			if (resultList != null && resultList.size() > 0) {
				googlePlacesAutocompleteAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			Log.e(_TAG, "Cannot process JSON results", e);
		}

	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		String strNew = "";
		LatLng latLng = null;
		try {
			PlaceObject placeObject = (PlaceObject)view.getTag();
			if( placeObject.getLat() != 0 ||
					placeObject.getLng() != 0 ) {
				latLng = new LatLng(placeObject.getLat(), placeObject.getLng());
			}
			strNew = (String) adapterView.getItemAtPosition(position);
			if( latLng == null ) {
				latLng = getLatLngFromAddress(strNew);
			}
			if( latLng == null ) {
				showFailedsnackbar();
				return;
			}
			Intent intent = new Intent(this, ResultScreen.class);
			intent.putExtra(ResultScreen.KEY_LAT, ""+latLng.latitude );
			intent.putExtra(ResultScreen.KEY_LNG, ""+latLng.longitude );
			intent.putExtra(ResultScreen.KEY_ADDR, strNew );
			startActivity(intent);
		}catch(Exception e) {
			showFailedsnackbar();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	// This method will be called when a MessageEvent is posted (in the UI thread for Toast)
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(AutoCompletePlacesMessageEvent event) {
		if (event.isSuccess()) {
			dataReceived(event.response);
		} else {
			Toast.makeText(this, "FAILED TO FETCH DATA", Toast.LENGTH_SHORT).show();
		}
	}

	private LatLng getLatLngFromAddress( String address) {
		LatLng latLong = null;
		try {
			Geocoder gc = new Geocoder(this);
			int no_of_try = 0;
			List<Address> addressList = new ArrayList<>();
			do{
				addressList = gc.getFromLocationName(address.toString(), 5);
				no_of_try++;
			}while( no_of_try <  3 && addressList.size() == 0);
			if (addressList.size() > 0) {
				double lat = (double) (addressList.get(0).getLatitude());
				double lon = (double) (addressList.get(0).getLongitude());
				latLong = new LatLng(lat, lon);
			}
		}catch( Exception e){
			e.printStackTrace();
		}
		return latLong;
	}

	private void showFailedsnackbar(){
		Snackbar snackBar = Snackbar.make(activitySearchScreenBinding.searchRoot,
				R.string.str_failed, Snackbar.LENGTH_SHORT);
		snackBar.show();
	}
}
