package space.nasa.spaceapi.models;

import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * Represents an object from the NASA's Astronomy Picture Of The Day API
 */
public class APOD implements Comparable<APOD>{
	/**
	 * The possible media types for an astronomy picture of the day
	 * video or image
	 */
	public static final String[] mediaTypes = {"video", "image"};
	/**
	 * The minimum date an astronomy picture of the day can be found
	 * 1995-06-16, June 16, 1995
	 */
	public static final LocalDate minDate = LocalDate.parse("1995-06-16");
	/**
	 * The most recent date an astronomy picture opf the day can be found
	 * this will always return today's date
	 */
	public static final LocalDate maxDate = LocalDate.now();
	private Date date;
	private String explanation;
	@SerializedName("hdurl")
	private URL hdUrl;
	//it will either be "video" or "image"
	@SerializedName("media_type")
	private String mediaType;
	private String copyright;
	@SerializedName("thumbnail_url")
	private String thumbnail;
	private String title;
	//it was a URL but there are some malformed returned URLs from the API and there was no way to validate them
	// before an object is instantiated and crashes the program
	private String url;
	
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param obj the object to be compared to
	 * @return <p>There are three cases<ul>
	 * 		<li>If the {@code obj} is null or not of the same class {@code false} is returned</li>
	 * 		<li>
	 * 		If the obj is of the {@link APOD} class then their dates and titles are compared and if they are both
	 * 		equal then {@code true} is returned
	 * 		</li>
	 * 		<li>
	 * 		If the obj is of the {@link APOD} and their dates or titles are not equal then {@code false} is returned
	 * 		</li>
	 * 		</ul></p>
	 */
	@Override
	public boolean equals(Object obj){
		if(obj != null && obj.getClass().equals(this.getClass()))
		{
			APOD apod = (APOD) obj;
			return apod.getDate().isEqual(this.getDate()) && apod.getTitle().equals(this.getTitle());
		}
		return false;
	}
	
	/**
	 * A brief representation of the NASA's astronomy picture of the day
	 * This will output th
	 * <pre>
	 * date - title
	 * excerpt of the explanation
	 *    </pre>
	 * if the media type is video, that would be appended to the next line
	 * @return a string representation of this APOD
	 */
	@Override
	public String toString(){
		StringJoiner joiner = new StringJoiner(" ", "", "...");
		String[] s = explanation.split(" ");
		//if they add 5 elements or reach the end of the description string
		for(int i = 0; (i < 5) && (i < s.length); i++)
			joiner.add(s[i]);
		String txt = "%s - %s\n%s\n%s\n".formatted(getDateString(), getMediaType(), title, joiner.toString());
		if(mediaType.equals(mediaTypes[0]))
			return "%smedia type: %s".formatted(txt, mediaType);
		return txt;
	}
	
	/**
	 * This will return the JSON url string as a {@link URL}
	 * @return the url or null if it can't be converted
	 */
	public URL getThumbnail(){
		try {return new URL(thumbnail);}
		catch(MalformedURLException e) {return null;}
	}
	
	/**
	 * Returns the date which this A.P.O.D. was posted as a {@link LocalDate}
	 * @return the date which the A.P.O.D. was posted
	 */
	public LocalDate getDate(){
		//i need to convert to lacaldate instead of maikng the datatype local date because java.time module "doEsn'T
		// oPeN tO th gson
		//Converts date which opens to gson to LocalDate (which doesn't) so subtract and add and better functions can be performed
		// divide 60 60 24 to convert the epochSecond to epoch Day for the method to work correctly
		return LocalDate.ofEpochDay(date.toInstant().getEpochSecond() / 60 / 60 / 24);
	}
	
	/**
	 * Returns the explanation of the A.P.O.D.
	 * @return the string representing the A.P.O.D., trimmed with {@link String#trim()}
	 */
	public String getExplanation(){
		return explanation.trim();
	}
	
	/**
	 * Returns the url of the HD image of the A.P.O.D.
	 * @return the HD image url, can be {@code null}
	 */
	public URL getHdUrl(){
		return hdUrl;
	}
	
	/**
	 * Return the media type of the A.P.O.D.
	 * @return will return the media type either, video or image
	 */
	public String getMediaType(){
		return mediaType;
	}
	
	/**
	 * Return the copyright of the video or image to whom the credit should go to
	 * @return the copyright party of the A.P.O.D.
	 */
	public String getCopyright(){
		return copyright;
	}
	
	/**
	 * Returns the title of the A.P.O.D.
	 * @return the A.P.O.D.'s title
	 */
	public String getTitle(){
		return title.trim();
	}
	
	/**
	 * Returns the url either to non-HD render image or video for the A.P.O.D.
	 * @return A {@link URL} to the video or image or null if it can't be converted
	 */
	public URL getUrl(){
		try
		{
			if(url.contains("http")) return new URL(url);
			return new URL("https:" + url);
		}
		catch(MalformedURLException e)
		{
			return null;
		}
	}
	
	/**
	 * This will return an Image of the A.P.O.D.
	 * It will return the hd image if there is one if not the lower resolution image will be returned.
	 * If the media type of the A.P.O.D. is a video the video's thumbnail is returned
	 * @return the Image for the A.P.O.D
	 */
	public Image getImage(){
		if(mediaType.equals(mediaTypes[0])) return new Image(String.valueOf(thumbnail));
		//if there is no hdURL set imgURL the url A.K.A. lower res image
		URL imgUrl = (hdUrl == null) ? getUrl() : hdUrl;
		return new Image(imgUrl.toString());
	}
	
	/**
	 * This will return the video for the A.P.O.D.
	 * if the media type is an image null is returned
	 * @return a {@link Media} of the A.P.O.D. video, or null if the A.P.O.D. doesn't contain an image
	 */
	public Media getVideo(){
		return mediaType.equals(mediaTypes[1]) ? null : new Media(getUrl().toString());
	}
	
	/**
	 * Returns a string of the A.P.O.D.'s date in the format
	 * <pre>YYYY-MM-DD</pre>
	 * @return the formatted date string
	 */
	public String getDateString(){
		return DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault()).format(date);
	}
	
	/**
	 * Uses {@link LocalDate}{@code .compareTo()} method
	 * <br/>
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 * @param o the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object
	 * 		is less than, equal to, or greater than the specified object.
	 * @throws NullPointerException if the specified object is null
	 * @throws ClassCastException   if the specified object's type prevents it
	 *                              from being compared to this object.
	 */
	@Override
	public int compareTo(APOD o){
		return getDate().compareTo(o.getDate());
	}
}
