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
public class APOD implements Comparable<APOD>{
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
	private String thumbnail;
	private String title;
	//it was a URL but there are some malformed returned URLs from the API and there was no way to validate them
	// before an object is instantiated and crashes the program
	private String url;
	public static final LocalDate minDate = LocalDate.parse("1995-06-16");
	public static final LocalDate maxDate = LocalDate.now();
	
	@Override
	public boolean equals(Object obj){
		if(obj != null && obj.getClass().equals(this.getClass()))
		{
			APOD apod = (APOD) obj;
			return apod.getDate().isEqual(this.getDate()) && apod.getTitle().equals(this.getTitle());
		}
		return false;
	}
	
	@Override
	public String toString(){
		StringJoiner joiner = new StringJoiner(" ", "", "...");
		String[] s = explanation.split(" ");
		//if they add 5 elements or reach the end of the description string
		for(int i = 0; (i < 5) && (i < s.length); i++)
			joiner.add(s[i]);
		return "%s - %s\n%s\n%s\n".formatted(getDateString(), getMediaType(), title, joiner.toString());
	}
	
	public URL getThumbnail(){
		try {return new URL(thumbnail);}
		catch(MalformedURLException e) {return null;}
	}
	
	public LocalDate getDate(){
		//i need to convert to lacaldate instead of maikng the datatype local date because java.time module "doEsn'T
		// oPeN tO th gson
		//Converts date which opens to gson to LocalDate (which doesn't) so subtract and add and better functions can be performed
		// divide 60 60 24 to convert the epochSecond to epoch Day for the method to work correctly
		return LocalDate.ofEpochDay(date.toInstant().getEpochSecond() / 60 / 60 / 24);
	}
	
	public String getExplanation(){
		return explanation.trim();
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
		return title.trim();
	}
	
	public URL getUrl(){
		try
		{
			if(url.contains("https://"))
				return new URL(url);
			return new URL("https:" + url);
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
	
	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 *
	 * <p>The implementor must ensure {@link Integer#signum
	 * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
	 * all {@code x} and {@code y}.  (This implies that {@code
	 * x.compareTo(y)} must throw an exception if and only if {@code
	 * y.compareTo(x)} throws an exception.)
	 *
	 * <p>The implementor must also ensure that the relation is transitive:
	 * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
	 * {@code x.compareTo(z) > 0}.
	 *
	 * <p>Finally, the implementor must ensure that {@code
	 * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
	 * == signum(y.compareTo(z))}, for all {@code z}.
	 * @param o the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 * 		is less than, equal to, or greater than the specified object.
	 * @throws NullPointerException if the specified object is null
	 * @throws ClassCastException   if the specified object's type prevents it
	 *                              from being compared to this object.
	 * @apiNote It is strongly recommended, but <i>not</i> strictly required that
	 *        {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
	 * 		class that implements the {@code Comparable} interface and violates
	 * 		this condition should clearly indicate this fact.  The recommended
	 * 		language is "Note: this class has a natural ordering that is
	 * 		inconsistent with equals."
	 */
	@Override
	public int compareTo(APOD o){
		return getDate().compareTo(o.getDate());
	}
}
