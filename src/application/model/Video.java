package application.model;

import java.io.File;

public class Video {
	String name;
	int length;
	File file;
	
	public Video(String a, int b, File c) {
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