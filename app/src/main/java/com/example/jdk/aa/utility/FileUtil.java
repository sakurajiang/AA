package com.example.jdk.aa.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	/**
	   * �����ļ�
	   */
	  public static void cp(String from, String to)
	    throws IOException {
	    cp(new File(from), new File(to));
	  }
	  /**
	   * �����ļ�
	   */
	  public static void cp(File from, File to)
	    throws IOException {
	    cp(new FileInputStream(from), 
	        new FileOutputStream(to));
	  }
	  /**
	   * �����ļ�
	   */
	  public static void cp(InputStream in, 
	      OutputStream out)
	    throws IOException {
	    //1K byte �Ļ�����!
	    byte[] buf = new byte[1024];
	    int count;
	    while((count=in.read(buf))!=-1){
	      System.out.println(count); 
	      out.write(buf, 0, count);
	    }
	    in.close();
	    out.close();
	  }
}
