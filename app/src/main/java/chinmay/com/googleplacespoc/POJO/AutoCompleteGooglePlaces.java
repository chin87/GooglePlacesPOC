package chinmay.com.googleplacespoc.POJO;

import java.util.List;

/**
 * Created by chinmaydeshpande on 20/05/17.
 */

public class AutoCompleteGooglePlaces {
	private List<Predictions> predictions;

	private String status;

	public void setPredictions(List<Predictions> predictions) {
		this.predictions = predictions;
	}

	public List<Predictions> getPredictions() {
		return this.predictions;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public class Predictions {
		private String description;

		private String id;

		private List<Matched_substrings> matched_substrings;

		private String place_id;

		private String reference;

		private Structured_formatting structured_formatting;

		private List<Terms> terms;

		private List<String> types;

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}

		public void setMatched_substrings(List<Matched_substrings> matched_substrings) {
			this.matched_substrings = matched_substrings;
		}

		public List<Matched_substrings> getMatched_substrings() {
			return this.matched_substrings;
		}

		public void setPlace_id(String place_id) {
			this.place_id = place_id;
		}

		public String getPlace_id() {
			return this.place_id;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public String getReference() {
			return this.reference;
		}

		public void setStructured_formatting(Structured_formatting structured_formatting) {
			this.structured_formatting = structured_formatting;
		}

		public Structured_formatting getStructured_formatting() {
			return this.structured_formatting;
		}

		public void setTerms(List<Terms> terms) {
			this.terms = terms;
		}

		public List<Terms> getTerms() {
			return this.terms;
		}

		public void setTypes(List<String> types) {
			this.types = types;
		}

		public List<String> getTypes() {
			return this.types;
		}
	}

	public class Matched_substrings {
		private int length;

		private int offset;

		public void setLength(int length) {
			this.length = length;
		}

		public int getLength() {
			return this.length;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getOffset() {
			return this.offset;
		}
	}

	public class Main_text_matched_substrings {
		private int length;

		private int offset;

		public void setLength(int length) {
			this.length = length;
		}

		public int getLength() {
			return this.length;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getOffset() {
			return this.offset;
		}
	}

	public class Structured_formatting {
		private String main_text;

		private List<Main_text_matched_substrings> main_text_matched_substrings;

		private String secondary_text;

		public void setMain_text(String main_text) {
			this.main_text = main_text;
		}

		public String getMain_text() {
			return this.main_text;
		}

		public void setMain_text_matched_substrings(List<Main_text_matched_substrings> main_text_matched_substrings) {
			this.main_text_matched_substrings = main_text_matched_substrings;
		}

		public List<Main_text_matched_substrings> getMain_text_matched_substrings() {
			return this.main_text_matched_substrings;
		}

		public void setSecondary_text(String secondary_text) {
			this.secondary_text = secondary_text;
		}

		public String getSecondary_text() {
			return this.secondary_text;
		}
	}

	public class Terms {
		private int offset;

		private String value;

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getOffset() {
			return this.offset;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

}
