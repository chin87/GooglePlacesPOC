package chinmay.com.googleplacespoc.UI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import chinmay.com.googleplacespoc.R;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class PhotosAdaptor extends RecyclerView.Adapter<PhotosAdaptor.PhotoListHolder>{

	private Context mContext;
	private ArrayList<String> mPhotoList;
	private IClick iClick;

	public PhotosAdaptor(Context context, ArrayList<String> photoList, IClick iClick){
		mContext = context;
		mPhotoList = photoList;
		this.iClick = iClick;
	}

	@Override
	public PhotoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_item, parent,
				false);
		return new PhotoListHolder(view);
	}

	@Override
	public void onBindViewHolder(PhotoListHolder holder, final int position) {
		Picasso.with(mContext)
				.load(mPhotoList.get(holder.getAdapterPosition()))
				.into(holder.imageView, new ImageLoadedCallback(holder.progressBar) {
					@Override
					public void onSuccess() {
						if (this.progressBar != null) {
							this.progressBar.setVisibility(View.GONE);
						}
					}
				});
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = (String) v.getTag();
				if( iClick != null ){
					iClick.clickedForUrl(url);
				}
			}
		});
		holder.imageView.setTag(mPhotoList.get(holder.getAdapterPosition()));
	}

	@Override
	public int getItemCount() {
		return mPhotoList.size();
	}

	public static class PhotoListHolder extends RecyclerView.ViewHolder {
		final ImageView imageView;
		final ProgressBar progressBar;

		public PhotoListHolder(View view) {
			super(view);
			imageView = (ImageView) view.findViewById(R.id.ivPhoto);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		}

	}

	public interface IClick{
		public void clickedForUrl(String url);
	}

	private class ImageLoadedCallback implements  com.squareup.picasso.Callback {
		ProgressBar progressBar;

		public  ImageLoadedCallback(ProgressBar progBar){
			progressBar = progBar;
		}

		@Override
		public void onSuccess() {

		}

		@Override
		public void onError() {

		}
	}
}
