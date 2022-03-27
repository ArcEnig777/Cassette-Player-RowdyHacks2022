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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return this.file;
	}
	
}