package application.model;

import java.io.File;

public class Song {

	String name;
	int length;
	File file;
	
	public Song(String a, int b, File c) {
		this.name = a;
		this.length = b;
		this.file = c;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public File getFile() {
		return this.file;
	}
	
}
