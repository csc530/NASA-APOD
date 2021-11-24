package space.nasa.spaceapi.models;

import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;

//astronomy picture of the day
public class APOD{
	public static final String[] mediaTypes = {"video", "image"};
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
	private String title;
	//it was a URL but there are some malformed returned URLs from the API and there was no way to validate them
	// before an object is instantiated and crashes the program
	private String url;
	
	public URL getThumbnail(){
		return thumbnail;
	}
	
	public LocalDate getDate(){
		//i need to convert to lacaldate instead of maikng the datatype local date because java.time module "doEsn'T
		// oPeN tO th gson
		//Converts date which opens to gson to LocalDate (which doesn't) so subtract and add and better functions can be performed
		// divide 60 60 24 to convert the epochSecond to epoch Day for the method to work correctly
		return LocalDate.ofEpochDay(date.toInstant().getEpochSecond() / 60 / 60 / 24);
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
		try
		{
			if(url.contains("https://"))
				return new URL(url);
			return new URL("https:"+url);
		}
		catch(MalformedURLException e)
		{
			return null;
		}
	}
	
	public Image getImage(){
		if(mediaType.equals(mediaTypes[0]))
			return new Image(String.valueOf(thumbnail));
		URL imgUrl = (hdUrl == null) ? getUrl() : hdUrl;
		return new Image(imgUrl.toString());
	}
	
	public URL getVideo(){
		return mediaType.equals(mediaTypes[1]) ? null : getUrl();
	}
	
	public String getDateString(){
		return DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(date);
	}
	
	@Override
	public String toString(){
		StringJoiner joiner = new StringJoiner(" ", "", "...");
		String[] s = explanation.split(" ");
		//if they add 5 elements or reach the end of the description string
		for(int i = 0; (i < 5) && (i < s.length); i++)
			joiner.add(s[i]);
		return "%s\n%s\n%s\n".formatted(getDateString(), title, joiner.toString());
	}
}
