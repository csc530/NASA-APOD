package space.nasa.spaceapi.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import space.nasa.spaceapi.models.APOD;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

/**
 * Used to call NASA's APOD API
 */
public class API{
	private static final Gson gson = new Gson();
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final String uri = "https://api.nasa.gov/planetary/apod?thumbs=true&api_key=1rp568Tl7gR9976UiFzaPbedFvxnBFFYbdqxXazV";
	private static float progress = 0.00F;
	
	/**
	 * Returns an {@link APOD} of today's A.P.O.D.
	 * @return today's A.P.O.D.
	 */
	public static APOD getAPOD(){
		return getAPOD(uri);
	}
	
	/**
	 * Call APOD API to get the specified date's A.P.O.D.
	 * @param date a {@link LocalDate} of desired A.P.O.D.
	 * @return the {@link APOD} of the given date
	 */
	public static APOD getAPOD(LocalDate date){
		return getAPOD(uri + "&date=" + date);
	}
	
	private static APOD getAPOD(String uri){
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
		try
		{
			String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
			return gson.fromJson(response, APOD.class);
		}
		catch(JsonSyntaxException|IOException | InterruptedException e)
		{
			e.printStackTrace();
			return new APOD();
		}
	}
	
	/**
	 * Get the curent progress of the current or previous API call
	 * <ul>
	 *     <li>
	 *         returns 0 if the API call has not returned any elements
	 *     </li>
	 *     <li>
	 *         returns >=1 if the API call has been completed and a {@link APOD} has been returned
	 *     </li>>
	 *     <li>
	 *         returns between 0 and 1 if the query has not finished returning all the {@link APOD} elements
	 *     </li>
	 * </ul>
	 * @return the progress of the API call as decimal (percent value)
	 */
	public static float getProgress(){
		return progress;
	}
	
	/**
	 * Set the progress to given {@code float}
	 * Best used if chaining together multiple singular API calls of one A.P.O.D.
	 * @param progress the {@code float} to set the API progress to
	 */
	public static void setProgress(float progress){
		API.progress = progress;
	}
	
	/**
	 * Call APOD API to get multiple A.P.O.D.s between the {@code start} and {@code end} date; inclusive.
	 * <p>
	 * If the start date is after the end date null will be returned. Furthemore if either the start or end date
	 * is outside the bounds of the {@link APOD#minDate} and {@link APOD#maxDate} respectively, null will be
	 * returned
	 * </p>
	 * <p>
	 * if more than 50 A.P.O.D.s are requested the call will be divided to multiple calls
	 * the progress of the overall call can be monitored with {@link API#getProgress()}
	 * </p>
	 * @param start the beginning {@link LocalDate} to search between for A.P.O.D.s must be after
	 *              {@link APOD#minDate} (inclusive)
	 * @param end   the last {@link LocalDate} to search between for A.P.O.D.s before {@link APOD#maxDate}  (inclusive)
	 * @return returns a {@link TreeSet<APOD>} of all the {@link APOD}s or null if the either dates conditions aren't
	 * 		met or request is interrupted or could not be made
	 */
	public static TreeSet<APOD> getAPODs(LocalDate start, LocalDate end){
		if(start.isAfter(end) || start.isBefore(APOD.minDate) || end.isAfter(APOD.maxDate))
			return null;
		Type typeOf = new TypeToken<TreeSet<APOD>>(){}.getType();
		//break up requests for many apods
		long days = ChronoUnit.DAYS.between(start, end);
		if(days > 50)
		{
			progress = 0;
			TreeSet<APOD> apods = new TreeSet<>();
			for(long i = 0; i < ((days / 50) + 1); i++)
			{
				//access the start date, must be final, so it doesn't change when accessed by thread
				final LocalDate threadStart = start.plusDays(50 * i);
				//async thread to call numbered amount of APODs query
				Thread query = new Thread(() -> {
					if(threadStart.isAfter(end))
						apods.add(getAPOD(end));
					else
						apods.addAll(getAPODs(threadStart, threadStart.plusDays(50)));
					progress = (float) apods.size() / days;
				});
				query.start();
				//wait for the query to complete
				try {query.join();}
				catch(InterruptedException e) {return null;}
			}
			return apods;
		}
		else
		{
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri + "&start_date=" + start + "&end_date=" + end)).build();
			try
			{
				String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
				return gson.fromJson(response, typeOf);
			}
			catch(JsonSyntaxException|IOException | InterruptedException e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}
}
