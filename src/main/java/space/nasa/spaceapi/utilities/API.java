package space.nasa.spaceapi.utilities;

import com.google.gson.Gson;
import space.nasa.spaceapi.models.APOD;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API{
	private static final Gson gson = new Gson();
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final String key = "1rp568Tl7gR9976UiFzaPbedFvxnBFFYbdqxXazV";
	
	public static APOD getAPOD(){
		String uri = "https://api.nasa.gov/planetary/apod?api_key=" + key;
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
}
