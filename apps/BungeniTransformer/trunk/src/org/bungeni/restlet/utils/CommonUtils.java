package org.bungeni.restlet.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommonUtils {
	public static String readTextFile(File fFile) throws IOException {
		byte[] buffer = new byte[(int)fFile.length()];
		DataInputStream in = new DataInputStream(new FileInputStream(fFile));
		in.readFully(buffer);
		String s = new String(buffer);
		return s;
	}
	

	public static void copyFile(File source, File dest) throws IOException {
	 if(!dest.exists()) {
	  dest.createNewFile();
	 }
	 InputStream in = null;
	 OutputStream out = null;
	 try {
	  in = new FileInputStream(source);
	  out = new FileOutputStream(dest);
	    
	  // Transfer bytes from in to out
	  byte[] buf = new byte[1024];
	  int len;
	  while ((len = in.read(buf)) > 0) {
	   out.write(buf, 0, len);
	  }
	 }
	 finally {
	  if(in != null) {
	   in.close();
	  }
	  if(out != null) {
	   out.close();
	  }
	 }
	}
	
}
