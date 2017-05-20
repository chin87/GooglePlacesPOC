package chinmay.com.googleplacespoc.UI;

import android.app.DownloadManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
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
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Uri Download_Uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
		//Restrict the types of networks over which this download may proceed.
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		//Set whether this download may proceed over a roaming connection.
		request.setAllowedOverRoaming(false);
		//Set the title of this download, to be displayed in notifications (if enabled).
		request.setTitle("Downloading image");
		//Set a description of this download, to be displayed in notifications (if enabled)
		request.setDescription("Image");
		//Set the local destination for the downloaded file to a path within the application's external files directory
		request.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"1.jpg");
		downloadReference = downloadManager.enqueue(request);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		LatLng currentLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
		mMap.addMarker(new MarkerOptions().position(currentLocation).title(address));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
		WebCommunicator.getPhotosOfLocation(currentLocation);
	}
}
