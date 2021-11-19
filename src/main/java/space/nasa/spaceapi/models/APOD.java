package space.nasa.spaceapi.models;

import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;

import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

//astronomy picture of the day
public class APOD{
	private Date date;
	private String explanation;
	@SerializedName("hdurl")
	private URL hdUrl;
	// will either be "video" or "image"
	@SerializedName("media_type")
	private String mediaType;
	private String copyright;
	@SerializedName("thumbnail_url")
	private URL thumbnail;
	
	public static final String[] mediaTypes= {"video","image"} ;
	public URL getThumbnail(){
		return thumbnail;
	}
	
	public APOD(){}
	
	public LocalDate getDate(){
		//Converts date which opens to gson to LocalDate (which doesn't) so subtract and add and better functions can be performed
		// divide 60 60 24 to convert the epochSecond to epoch Day for the method to work correctly
		return LocalDate.ofEpochDay(date.toInstant().getEpochSecond()/60/60/24);
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
		if(mediaType.equals(mediaTypes[0]))
			return new Image(String.valueOf(thumbnail));
		URL imgUrl = (hdUrl == null) ? url : hdUrl;
		return new Image(imgUrl.toString());
	}
	public URL getVideo(){
		return mediaType.equals(mediaTypes[1]) ? null : url;
	}
	public String getDateString(){
		return		                                DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(date);
	}
	@Override
	public String toString(){
		return "%s\n%s\n%s\n".formatted(title,
		                                explanation,
		                                getDateString());
	}
}
