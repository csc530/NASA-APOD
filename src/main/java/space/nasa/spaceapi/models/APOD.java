package space.nasa.spaceapi.models;

import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;

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
	private String copyright;
	public Date getDate(){
		return date;
	}
	
	public String getExplanation(){
		return explanation;
	}
	
	public URL getHdUrl(){
		return hdUrl;
	}
	
	public String getMediaType(){
		return mediaType;
	}
	
	public String getCopyright(){
		return copyright;
	}
	
	public String getTitle(){
		return title;
	}
	
	public URL getUrl(){
		return url;
	}
	
	private String title;
	private URL url;
	public Image getImage(){
		URL imgUrl  = hdUrl==null ? url : hdUrl;
		return new Image(imgUrl.toString());
	}
	public String getDateString(){
		return		                                DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(date);
	}
	@Override
	public String toString(){
		return "%s\n%s\n%s\n".formatted(title,
		                                explanation,
		                                DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(date));
	}
}
