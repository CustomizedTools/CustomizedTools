package com.customized.tools.jarClassSearcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.customized.tools.AbstractTools;
import com.customized.tools.cli.InputConsole;
import com.customized.tools.model.ClassSearcher;
import com.customized.tools.model.Entity;


/**
 * JarClassSearcher search class from a folder, even class exist in jar file
 * 
 * @author kylin
 *
 */
public class JarClassSearcher extends AbstractTools {

	private static final Logger logger = Logger.getLogger(JarClassSearcher.class);
	
	private ClassSearcher jarClassSearcher;
	
	public JarClassSearcher(Entity entity, InputConsole console) {
		super(entity, console);
	}
	
	public JarClassSearcher(InputConsole console, boolean isAesh) {
		super(null, console, isAesh);
	}

	public void execute() {
		
		jarClassSearcher = (ClassSearcher) entity;
		
		logger.info("JarClassSearcher Start");
		
		try {
			if(!isAesh){
				if(console.readFromCli(jarClassSearcher.getId())) {
					String folder = console.readFolderPath("Input jarClassSearcher folder path", jarClassSearcher.getFolderPath(), true);
					jarClassSearcher.setFolderPath(folder);
					String className = console.readString("Input jarClassSearcher class name", jarClassSearcher.getClassName(), false);
					jarClassSearcher.setClassName(className);
				}
			}
			
			String prompt = "Searching '" + jarClassSearcher.getClassName() + "' Under Directory " + jarClassSearcher.getFolderPath() ;
			
			console.prompt(prompt);
			
			Set<JarFile> jarFileSet = new HashSet<JarFile>();		
			JarFileCollection(new File(jarClassSearcher.getFolderPath()), jarFileSet);
			
			List<String> result = getResultJars(jarFileSet, jarClassSearcher.getClassName());
			
			printToConsole(result);
		} catch (Throwable e) {
			JarClassSearcherException ex = new JarClassSearcherException("JarClassSearcher return a Error", e);
			console.prompt(ex.getMessage());
			logger.error("", ex);
//			throw ex;
		}

	}
	


	private void printToConsole(List<String> result) {

		StringBuffer sb = new StringBuffer();
		sb.append(jarClassSearcher.getClassName() + " has been found " + result.size() + " times." + console.ln());
		for (String str : result) {
			sb.append(console.twoTab() + str + console.ln());
		}
		
		console.prompt(sb.toString());
		
		if(logger.isDebugEnabled()) {
			logger.debug(sb.toString());
		}
	}

	protected void JarFileCollection(File jarDirs, Set<JarFile> jarFileSet) throws IOException {
		
		if (!jarDirs.isDirectory()) {
			throw new JarClassSearcherException("Incorrect directory name has passed: " + jarDirs.getAbsolutePath());
		}
		
		for (File file : jarDirs.listFiles()) {
			if (file.isDirectory()) {
				JarFileCollection(file, jarFileSet);
			} else if (file.getName().endsWith(".jar") || file.getName().endsWith(".war")) {
				JarFile jarFile;
				try {
					jarFile = new JarFile(file);
					jarFileSet.add(jarFile);
				} catch (Exception e) {
					String prompt = "can not create jarFile via " + file ;
					logger.error(prompt, new JarClassSearcherException(prompt, e));
					console.prompt(prompt + ", ignored");
				}
				
			}
		}
	}

	protected List<String> getResultJars(Set<JarFile> jarFileSet, String className) throws IOException {
		
		List<String> results = new ArrayList<String>();
		className = buildClassName(className);
		for (JarFile jarFile : jarFileSet) {
			traverseJarFile(results, jarFile, className);
			jarFile.close();
		}
		return results;
	}

	private void traverseJarFile(List<String> results, JarFile jarFile, String className) {
		Enumeration<JarEntry> entrys = jarFile.entries();
		while (entrys.hasMoreElements()) {
			JarEntry jar = entrys.nextElement();
			if (jar.getName().endsWith(className)) {
				results.add(jarFile.getName() + " $ " + jar.getName());
			} else if(jar.getName().endsWith(".jar")) {
				//TODO
			}
		}
	}

	protected String buildClassName(String className) {
		if (className.endsWith(".java")) {
			className = className.substring(0, className.length() - 5);
		} else if (className.endsWith(".class") || className.endsWith(".Class")) {
			className = className.substring(0, className.length() - 6);
		}
		return className.replace('.', '/').replace('\\', '/') + ".class";
	}

}
