package space.nasa.spaceapi.utilities;

import com.google.gson.Gson;
import space.nasa.spaceapi.models.APOD;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

public class API{
	public static void main(String[] args) throws IOException, InterruptedException{
		System.out.println(getAPOD());
	}
	private final static String key = "1rp568Tl7gR9976UiFzaPbedFvxnBFFYbdqxXazV";
	public static APOD getAPOD() throws IOException, InterruptedException{
		String uri = "https://api.nasa.gov/planetary/apod?api_key="+key;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
		Gson gson = new Gson();
		String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		return gson.fromJson(response, APOD.class);
	}
}
