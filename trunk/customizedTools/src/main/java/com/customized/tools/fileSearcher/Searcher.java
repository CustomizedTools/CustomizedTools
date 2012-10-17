package com.customized.tools.fileSearcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;


public class Searcher {
	
	private static final Logger logger = Logger.getLogger(Searcher.class);

	private String searchName ;
	
	private String searchFolder;

	public Searcher(String searchName, String searchFolder) {
		super();
		this.searchName = searchName;
		this.searchFolder = searchFolder;
	}

	public List<String> search() throws ZipException, IOException {
		
		logger.info("Searching " + searchName + " under " + searchFolder);
		
		List<String> result = new ArrayList<String>();
		
		search(result, new File(searchFolder), searchName);
		
		return result;
	}

	private void search(List<String> result, File file, String searchName) throws ZipException, IOException {
		
		for(File f : file.listFiles()) {
			
			if(f.getName().contains(searchName)) {
				String path = f.getAbsolutePath() ;
				result.add(path.substring(path.indexOf(searchFolder)));
			}
			
			if(f.isDirectory()) {
				search(result, f, searchName);
			}  else if(f.getName().endsWith(".zip") || f.getName().endsWith(".jar") || f.getName().endsWith(".war") || f.getName().endsWith(".ear") || f.getName().endsWith(".esb")) {
				String path = f.getAbsolutePath() ;
				path = path.substring(path.indexOf(searchFolder));
				traverseZipFile(result, path, new ZipFile(f), searchName);
			}
		}
	}

	private void traverseZipFile(List<String> result, String prefix, ZipFile zip, String searchName) {
		
		@SuppressWarnings("unchecked")
		Enumeration <ZipEntry>en = (Enumeration<ZipEntry>) zip.entries();
		
		while(en.hasMoreElements()) {
			
			ZipEntry entry = en.nextElement() ;
			
			if(entry.getName().contains(searchName)) {
				result.add(prefix + " $ " + entry.getName());
			}
		}
	}
	
}
