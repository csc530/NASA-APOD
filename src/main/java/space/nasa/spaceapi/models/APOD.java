package space.nasa.spaceapi.models;

import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

//astronomy picture of the day
public class APOD{
	private Date date;
	private String explanation;
	@SerializedName("hdurl")
	private URL hdUrl;
	@SerializedName("media_type")
	private String mediaType;
	private String title;
	private URL url;
	
	@Override
	public String toString(){
		return "%s\n%s\n%s\n".formatted(title,
		                                explanation,
		                                DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(date));
	}
}
