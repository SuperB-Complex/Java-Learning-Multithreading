package tta.basics.daytwo.two;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Assignment_2_16_Solution_JinjinMa {
	
//	public static final String SLASH = "\\";
//	
//	public static void main(String[] args) {
//		String current = System.getProperty("user.dir");
//		String temp = current + SLASH + "ss";
//		System.out.println(current);
//		System.out.println(SLASH);
//		System.out.println(temp);
//		File file1 = new File(current);
//		System.out.println(file1.exists());
//		File file2 = new File(temp);
//		System.out.println(file2.exists());
//		File file3 = new File("D:\\s\\practice");
//		System.out.println(file3.exists());
//		
////		try (DirectoryStream<Path> stream = Files.newDirectoryStream(null, "C:\\Users\\Root\\Desktop\\Desktop")) {
////		    for (Path file: stream) {
////		        System.out.println(file.getFileName());
////		    }
////		} catch (IOException | DirectoryIteratorException x) {
////		    // IOException can never be thrown by the iteration.
////		    // In this snippet, it can only be thrown by newDirectoryStream.
////		    System.err.println(x);
////		}
//		
//		
//		File file = new File("C:\\Users\\Root\\Desktop\\Desktop");
//        File[] files = file.listFiles();
//        for(File f: files){
//            System.out.println(f.getName());
//        }
//	}
	
	private static boolean checkQuit(String instruction) {
		if (instruction.length() == 1 && instruction.equals("q")) {
			return true;
		}
		return false;
	}
	
	public static void count(Criteria criteria) {
		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.println("Pleaes input the path:");
			String path = input.nextLine();
			if (Assignment_2_16_Solution_JinjinMa.checkQuit(path)) {
				break;
			}
			criteria.setDirectory(path);
			try {
				criteria.checkValidnessOfDirectory();
			} catch(DirectoryNotValidException e) {
				e.printStackTrace();
				continue;
			} 
			if (!criteria.checkDirectoryExistence()) {
				continue;
			}
			criteria.displayFileList();
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Criteria criteria = new Criteria();
		Assignment_2_16_Solution_JinjinMa.count(criteria);
		return;
	}
}
