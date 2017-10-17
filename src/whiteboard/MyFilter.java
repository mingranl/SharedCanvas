package whiteboard;
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFilter extends FileFilter {
	String ends; 
	String description; 

	public MyFilter(String ends, String description) { 
	    this.ends = ends; 
	    this.description = description; 
	  }
	@Override
	public boolean accept(File f) {
		// files can be showed with legal ends
		if (f.isDirectory()) {
			return true;
		}
	    String fileName = f.getName();
	    if (fileName.toUpperCase().endsWith(this.ends.toUpperCase())) {
	    	return true;
	    }
		return false;
	}

	@Override
	public String getDescription() {
		// return ends' description
		return this.description;
	}
	
	public String getEnds() {
		// return ends
		return this.ends;
	}
}
