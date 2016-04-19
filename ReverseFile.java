package com.cwang.util;
/**
 * 从末尾开始按行读取文件
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReverseFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File dictionary = new File("");
			File file = new File(dictionary.getCanonicalPath());
			File[] files = file.listFiles();

			List<File> fileList = new ArrayList<File>();
			for (File f : files) {
				if (f.isFile()) {
					fileList.add(f);
				}
			}

			Collections.sort(fileList, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					if (o1.isDirectory() && o2.isFile())
						return -1;
					if (o1.isFile() && o2.isDirectory())
						return 1;
					return o2.getName().compareTo(o1.getName());
				}
			});

			read(fileList.get(0).getName(), "utf-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void read(String filename, String charset) {
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filename, "r");
			long len = rf.length();
			long start = rf.getFilePointer();
			long nextend = start + len - 1;
			String line;
			rf.seek(nextend);
			int c = -1;
			while (nextend > start) {
				c = rf.read();
				if (c == '\n' || c == '\r') {
					line = rf.readLine();
					if (line != null) {
						System.out.println(new String(line
								.getBytes("ISO-8859-1"), charset));
					} else {
						System.out.println(line);
					}
					nextend--;
				}
				nextend--;
				rf.seek(nextend);
				if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行
					System.out.println(rf.readLine());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
