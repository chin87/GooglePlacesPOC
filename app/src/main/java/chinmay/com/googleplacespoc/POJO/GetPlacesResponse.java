package chinmay.com.googleplacespoc.POJO;

import java.util.List;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class GetPlacesResponse {
	private List<Object> htmlAttributions = null;
	private List<Result> results = null;
	private String status;

	public List<Object> getHtmlAttributions() {
		return htmlAttributions;
	}

	public void setHtmlAttributions(List<Object> htmlAttributions) {
		this.htmlAttributions = htmlAttributions;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public class Result {
		private String icon;
		private String id;
		private String name;
		private List<Photo> photos = null;
		private String placeId;
		private float rating;
		private String reference;
		private String scope;
		private List<String> types = null;
		private String vicinity;

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Photo> getPhotos() {
			return photos;
		}

		public void setPhotos(List<Photo> photos) {
			this.photos = photos;
		}

		public String getPlaceId() {
			return placeId;
		}

		public void setPlaceId(String placeId) {
			this.placeId = placeId;
		}

		public float getRating() {
			return rating;
		}

		public void setRating(float rating) {
			this.rating = rating;
		}

		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public String getScope() {
			return scope;
		}

		public void setScope(String scope) {
			this.scope = scope;
		}

		public List<String> getTypes() {
			return types;
		}

		public void setTypes(List<String> types) {
			this.types = types;
		}

		public String getVicinity() {
			return vicinity;
		}

		public void setVicinity(String vicinity) {
			this.vicinity = vicinity;
		}
	}

	public class Photo {

		private int height;
		private List<String> html_attributions = null;
		private String photo_reference;
		private int width;

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public List<String> getHtmlAttributions() {
			return html_attributions;
		}

		public void setHtmlAttributions(List<String> html_attributions) {
			this.html_attributions = html_attributions;
		}

		public String getPhotoReference() {
			return photo_reference;
		}

		public void setPhotoReference(String photo_reference) {
			this.photo_reference = photo_reference;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}
	}
}

