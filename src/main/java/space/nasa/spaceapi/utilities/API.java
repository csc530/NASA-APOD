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
import java.util.LinkedHashSet;

public class API{
	private static final Gson gson = new Gson();
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final String key = "1rp568Tl7gR9976UiFzaPbedFvxnBFFYbdqxXazV";
	private static final String uri = "https://api.nasa.gov/planetary/apod?thumbs=true&api_key=" + key;
	
	public static APOD getAPOD(){
		return getApod(uri);
	}
	
	public static APOD getAPOD(LocalDate date){
		return getApod(uri + "&date=" + date);
	}
	
	public static APOD getApod(String uri){
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
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri + "&start_date=" + start + "&end_date=" + end)).build();
		try
		{
			String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
			return gson.fromJson(response, typeOf);
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
			return new LinkedHashSet<APOD>(){};
		}
	}
}
