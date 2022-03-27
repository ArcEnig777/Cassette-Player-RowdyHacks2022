package application.model;

import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlaylistContainer {

	ArrayList<Playlist> playlists;
	
	public PlaylistContainer(ArrayList<Playlist> pl) {
		this.playlists = pl;
	}
	
	public void addPlaylist( Playlist a ) {
		this.playlists.add(a);
	}
	
	public void encodeToJson() {
		
//		Gson gson = new Gson();
//		PlaylistContainer obj = gson.toJson(new FileReader("./playlists.json"), PlaylistContainer.class);
//		gson.toJson(INSERT THE THING HERE, new FileWriter("./playlists.json"));
		
		
	}
	
	public void getFromJson() {			// change to return PlaylistContainer
		
//		Gson gson = new Gson();
//		PlaylistContainer obj = gson.fromJson(new FileReader("./playlists.json"), PlaylistContainer.class);
//		return obj;
	}
	
}
