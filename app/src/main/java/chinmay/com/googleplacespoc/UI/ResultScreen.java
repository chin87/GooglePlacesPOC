package chinmay.com.googleplacespoc.UI;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chinmay.com.googleplacespoc.Communication.WebCommunicator;
import chinmay.com.googleplacespoc.POJO.GetPlacesMessageEvent;
import chinmay.com.googleplacespoc.POJO.GetPlacesResponse;
import chinmay.com.googleplacespoc.R;
import chinmay.com.googleplacespoc.databinding.ActivityResultScreenBinding;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class ResultScreen extends AppCompatActivity implements PhotosAdaptor.IClick, OnMapReadyCallback {

	private ActivityResultScreenBinding activityResultScreenBinding;
	private ArrayList<String> mPhotoUris;
	private PhotosAdaptor photosAdaptor;
	private DownloadManager downloadManager;
	private long downloadReference;
	private SupportMapFragment mapFragment;
	public static final String KEY_LAT = "KEY_LAT";
	public static final String KEY_LNG = "KEY_LNG";
	public static final String KEY_ADDR = "KEY_ADDR";
	private GoogleMap mMap;
	private String latitude;
	private String longitude;
	private String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityResultScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_result_screen);
		latitude = getIntent().getStringExtra(KEY_LAT);
		longitude = getIntent().getStringExtra(KEY_LNG);
		address = getIntent().getStringExtra(KEY_ADDR);
		mPhotoUris = new ArrayList<>();
		photosAdaptor = new PhotosAdaptor(this, mPhotoUris, this);
		LinearLayoutManager layoutManager
				= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		activityResultScreenBinding.rvPhotos.setLayoutManager(layoutManager);
		activityResultScreenBinding.rvPhotos.setAdapter(photosAdaptor);
		FragmentManager myFragmentManager = getSupportFragmentManager();
		mapFragment = (SupportMapFragment) myFragmentManager
				.findFragmentById(R.id.flMapView);
		mapFragment.getMapAsync(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	// This method will be called when a MessageEvent is posted (in the UI thread for Toast)
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(GetPlacesMessageEvent event) {
		if (event.isSuccess()) {
			parsePhotosData(event.response);
		} else {
			Toast.makeText(this, "FAILED TO FETCH PHOTOS", Toast.LENGTH_SHORT).show();
		}
	}

	private void parsePhotosData(GetPlacesResponse response) {
		if (response != null) {
			List<GetPlacesResponse.Result> results = response.getResults();
			if (results != null) {
				mPhotoUris.clear();
				for (GetPlacesResponse.Result result : results) {
					List<GetPlacesResponse.Photo> photos = result.getPhotos();
					if (photos != null) {
						for (GetPlacesResponse.Photo photo : photos) {
							String photoReference = photo.getPhotoReference();
							if (!TextUtils.isEmpty(photoReference)) {
								addItemToPhotoList("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference +
										"&key=" + getResources().getString(R.string.google_maps_api_key));
							}
						}
					}
				}
			}
		}
	}

	private void addItemToPhotoList(final String photoUri) {
		mPhotoUris.add(photoUri);
		photosAdaptor.notifyDataSetChanged();
	}

	@Override
	public void clickedForUrl(String url) {
		if (url != null) {
			downloadData(url);
		}
	}

	private void downloadData(String url) {
		Uri Download_Uri = Uri.parse(url);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime())+".jpg";

		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		request.setAllowedOverRoaming(false);
		request.setTitle("Downloading image");
		request.setDescription("Image: "+formattedDate);
		request.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,formattedDate);

		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, filter);

		downloadReference = downloadManager.enqueue(request);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng currentLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
		mMap.addMarker(new MarkerOptions().position(currentLocation).title(address));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
		WebCommunicator.getPhotosOfLocation(currentLocation);
		googleMap.getUiSettings().setMapToolbarEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			//check if the broadcast message is for our Enqueued download
			long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if(downloadReference == referenceId){
				showSuccesssnackbar();
			}
		}
	};


	private void showSuccesssnackbar(){
		Snackbar snackBar = Snackbar.make(activityResultScreenBinding.searchRoot,
				R.string.str_download_success, Snackbar.LENGTH_SHORT);
		snackBar.show();
	}
}
