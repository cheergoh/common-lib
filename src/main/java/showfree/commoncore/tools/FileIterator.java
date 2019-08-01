package showfree.commoncore.tools;

import showfree.commoncore.lang.VariableBoolean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件迭代器工具
 * @author 
 *
 */
public abstract class FileIterator {

	private File root; //根路径
	
	public FileIterator(String rootPath) {
		root = new File(rootPath);
	}
	
	public FileIterator(File root) {
		this.root = root;
	}
	
	public final File getRoot() {
		return root;
	}
	
	/**
	 * !!!非线程安全
	 * 执行目录内文件迭代操作，优先遍历当前目录内的文件
	 * 如果根路径不存在，或者不是目录则停止遍历
	 */
	public final void executeFileFirst() {
		executeFileFirst((Object) null);
	}
	
	/**
	 * !!!非线程安全
	 * 执行目录内文件迭代操作，优先遍历当前目录内的文件
	 * 如果根路径不存在，或者不是目录则停止遍历
	 * @param args
	 */
	public final void executeFileFirst(Object... args) {
		if(root != null && root.exists() && root.isDirectory()) {
			iteratorFileFirst(0, root, new VariableBoolean(false), args);
		}
	}
	
	/**
	 * !!!非线程安全
	 * 执行目录内文件迭代操作，优先遍历当前目录内的子目录
	 * 如果根路径不存在，或者不是目录则停止遍历
	 */
	public final void executeDirectoryFirst() {
		executeDirectoryFirst((Object) null);
	}
	
	/**
	 * !!!非线程安全
	 * 执行目录内文件迭代操作，优先遍历当前目录内的子目录
	 * 如果根路径不存在，或者不是目录则停止遍历
	 * @param args
	 */
	public final void executeDirectoryFirst(Object... args) {
		if(root != null && root.exists() && root.isDirectory()) {
			iteratorDirectoryFirst(0, root, args);
		}
	}
	
	/**
	 * 内部迭代器，优先遍历当前目录内的文件
	 * @param level
	 * @param nowDir
	 * @param stopIterator
	 * @param args
	 */
	private void iteratorFileFirst(int level, File nowDir, VariableBoolean stopIterator, Object... args) {
		File[] list = nowDir.listFiles();
		if(list != null) {
			List<File> fileList = new ArrayList<File>();
			List<File> dirList = new ArrayList<File>();
			for(File file : list) {
				if(file.isFile()) {
					fileList.add(file);
				}else if(file.isDirectory()) {
					dirList.add(file);
				}
			}
			
			if(fileList.size() > 0) {
				for(File file : fileList) {
					VariableBoolean levelNext = new VariableBoolean(true);
					handler(level, nowDir, file, stopIterator, levelNext, args);
					
					if(!levelNext.getValue()) {
						break;
					}
				}
			}
			
			if(!stopIterator.getValue() && dirList.size() > 0) {
				for(File dir : dirList) {
					iteratorFileFirst(level + 1, dir, stopIterator, args);
					if(stopIterator.getValue()) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 内部迭代器，优先遍历当前目录内的子目录
	 * @param level
	 * @param nowDir
	 * @param args
	 */
	private void iteratorDirectoryFirst(int level, File nowDir, Object... args) {
		File[] list = nowDir.listFiles();
		if(list != null) {
			for(File file : list) {
				VariableBoolean stopIterator = new VariableBoolean(false);
				VariableBoolean levelNext = new VariableBoolean(true);
				if(file.isFile()) {
					handler(level, nowDir, file, stopIterator, levelNext, args);
					if(!levelNext.getValue()) {
						break;
					}
				}else if(file.isDirectory()) {
					if(!stopIterator.getValue()) {
						iteratorDirectoryFirst(level + 1, file, args);
					}
				}
			}
		}
	}
	
	/**
	 * 每次迭代到一个新文件，则会调用此方法
	 * @param level 目录层级，从根路径开始=0
	 * @param nowDir 当前目录
	 * @param file 当前文件
	 * @param stopIterator 是否停止继续迭代
	 * @param levelNext 是否继续同层级的后续文件迭代
	 * @param args 扩展数据
	 */
	protected abstract void handler(int level, File nowDir, File file, VariableBoolean stopIterator, VariableBoolean levelNext, Object... args);
}
