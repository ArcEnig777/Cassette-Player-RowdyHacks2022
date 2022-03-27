package application.model;

import java.util.ArrayList;

public class Playlist {

	String title;
	String description;
	ArrayList<Song> songs = new ArrayList<>();
	
	public Playlist(String t, String d, ArrayList<Song> sgs) {
		this.title = title;
		this.description = description;
		this.songs = sgs;
	}
	
	public void setTitle( String a ) {
		this.title = a;
	}
	
	public void setDescription( String a ) {
		this.description = a;
	}
	
	public void addSong( Song a ) {
		this.songs.add(a);
	}
	
	
	
}
