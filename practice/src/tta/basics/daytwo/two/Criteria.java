package tta.basics.daytwo.two;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Criteria {
	public static final char COLON = ':';
	public static final char SLASH = '\\';
	public static final String MARK = ".";
	private final String currentDisk;
	private String directory;

	{
		String dir = System.getProperty("user.dir");
		int in = dir.indexOf(Criteria.COLON);
		this.currentDisk = dir.substring(0, in + 1);
	}

	public Criteria() {
		this.directory = this.currentDisk;
	}
	
	public void setDirectory(String path) {
		this.directory = path;
		return;
	}

	private void checkMultipleColonsOrSlashes() throws DirectoryNotValidException {
		// check if directory contains multiple colons or slashes
		int countOfColon = 0, lastSlash = 0;
		for (int i = 0; i < this.directory.length(); i++) {
			char c = this.directory.charAt(i);
			if (c == Criteria.COLON) {
				++countOfColon;
			} else if (c == Criteria.SLASH) {
				if (lastSlash + 1 == i) {
					throw new DirectoryNotValidException("Input is invalid. Multiple slashes detected. For example: C:\\Users\\Root\\Desktop\\Desktop");
				} 
				lastSlash = i;
			}
		}
		if (countOfColon > 1) {
			throw new DirectoryNotValidException("Input is invalid. Multiple colons detected.");
		}
	}
	
	public void checkValidnessOfDirectory() throws DirectoryNotValidException {
		int indexOfColon = this.directory.indexOf(Criteria.COLON);
		if (indexOfColon < 0) {
			if (!this.directory.startsWith(String.valueOf(Criteria.SLASH))) {
				this.checkMultipleColonsOrSlashes();
				// if not explicitly request which disk is looking for
				// current disk will be used
				this.directory = currentDisk + Criteria.SLASH + this.directory;
			} else {
				this.checkMultipleColonsOrSlashes();
				this.directory = currentDisk + this.directory;
			}
		} else if (indexOfColon == 0) {
			// valid directory path shouldn't start with colon
			throw new DirectoryNotValidException("Input is invalid. Starting with a colon.");
		} else {
			this.checkMultipleColonsOrSlashes();
		}

		// check if it is a file
		if (this.directory.indexOf(".") >= 0) {
			throw new DirectoryNotValidException("Input is invalid. Please input a directory path.");
		}
		return;
	}

	public boolean checkDirectoryExistence() {
		File file = new File(this.directory);
		if (!file.exists()) {
			System.out.println("The input directory is not existed. Please input a valid one.");
			return false;
		}
		return true;
	}

	public void displayFileList() {
		Map<String, List<Integer>> countRecord = new HashMap<> ();
		File file = new File(this.directory);
		File[] files = file.listFiles(); 
		int indx = 0;
		for (File f : files) {
			String name = f.getName();
			int indexOfDot = name.indexOf(Criteria.MARK);
			String suffix = null;
			if (indexOfDot < 0) {
				suffix = Criteria.MARK;
			} else {
				suffix = name.substring(indexOfDot);
			}
			countRecord.putIfAbsent(suffix, new LinkedList<Integer> ());
			countRecord.get(suffix).add(indx++);
		}
		this.dispaly(files, countRecord);
		return;
	} 

	private void dispaly(File[] files, Map<String, List<Integer>> map) {
		System.out.println("The directory is " + this.directory);
		System.out.println("There are " + files.length + " files or directories in all.\n[Each type of file will be displayed sorted by the last accessing time.]");
		if (map.containsKey(".")) {
			this.fileDisplay(map.get(Criteria.MARK), files, "sub-directory");
			map.remove(Criteria.MARK);
		} 
		for (String suffix : map.keySet()) {
			this.fileDisplay(map.get(suffix), files, suffix);
		}
		return;
	}
	
	private void fileDisplay(List<Integer> indexes, File[] files, String suffix) {
		System.out.println("    The number of \'" + suffix + "\' is " + indexes.size() + ".");
		Collections.sort(indexes, new Comparator<Integer> () {
			@Override
			public int compare(Integer integer1, Integer integer2) {
				File file1 = files[integer1];
				File file2 = files[integer2];
				BasicFileAttributes attr1 = null;
				BasicFileAttributes	attr2 = null;
				try {
					attr1 = Files.readAttributes(file1.toPath(), BasicFileAttributes.class);
					attr2 = Files.readAttributes(file2.toPath(), BasicFileAttributes.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return (int) (attr1.lastAccessTime().to(TimeUnit.NANOSECONDS) - attr2.lastAccessTime().to(TimeUnit.NANOSECONDS));
			}
		});
		for (Integer index : indexes) {
			System.out.println("        " + files[index].getName());
		}
	}
}
