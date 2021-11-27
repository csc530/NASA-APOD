package space.nasa.spaceapi.utilities;

import com.google.gson.Gson;
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
import java.util.LinkedHashSet;

public class API{
	private static final Gson gson = new Gson();
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final String uri = "https://api.nasa.gov/planetary/apod?thumbs=true&api_key=1rp568Tl7gR9976UiFzaPbedFvxnBFFYbdqxXazV";
	
	public static APOD getAPOD(){
		return getAPOD(uri);
	}
	
	public static APOD getAPOD(LocalDate date){
		return getAPOD(uri + "&date=" + date);
	}
	
	public static APOD getAPOD(String uri){
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
		try
		{
			String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
			return gson.fromJson(response, APOD.class);
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
			return new APOD();
		}
	}
	
	public static LinkedHashSet<APOD> getAPODs(LocalDate start, LocalDate end){
		Type typeOf = new TypeToken<LinkedHashSet<APOD>>(){}.getType();
		//break up requests for many apods
		long days = ChronoUnit.DAYS.between(Tstart, end.plusDays(0));
		//System.out.println("Days: "+days);
		//System.out.println("Start: " + start + " End: " +end);
		if(days > 50)
		{
			LinkedHashSet<APOD> apods = new LinkedHashSet<APOD>();
			final LocalDate Tstart = Tstart;
			Thread main = new Thread(() -> {
				
				for(long i = 0; i < ((days / 50) + 1); i++)
				{
					Tstart = Tstart.plusDays(50 * i);
					System.out.println(Tstart + " : " + i + " : " + Tstart.plusDays(50));
					LocalDate finalStart = Tstart;
					new Thread(() -> {
						if(finalStart.isAfter(end))
							apods.add(getAPOD(end));
						else
							apods.addAll(getAPODs(finalStart, finalStart.plusDays(50)));
						System.out.println(apods.size());
					});
				}
				System.out.println(Tstart);
			});
			return apods;
		}
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri + "&start_date=" + Tstart + "&end_date=" + end)).build();
		try
		{
			String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
			return gson.fromJson(response, typeOf);
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
			return new LinkedHashSet<>();
		}
	}
}
