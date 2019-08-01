package showfree.commoncore.tools;

import org.apache.log4j.Logger;
import showfree.commoncore.lang.VariableBoolean;

import java.io.File;

/**
 * 类文件迭代器工具
 *
 * @author
 */
public abstract class ClassIterator {

    private Logger log = Logger.getLogger(ClassIterator.class);

    private File root; //根路径

    public ClassIterator(String rootPath) {
        root = new File(rootPath);
    }

    public ClassIterator(File root) {
        this.root = root;
    }

    /**
     * !!!非线程安全
     * 执行类文件迭代操作
     * 如果根路径不存在，或者不是目录则停止遍历
     */
    public final void execute() {
        execute(null);
    }

    /**
     * !!!非线程安全
     * 执行类文件迭代操作
     * 如果根路径不存在，或者不是目录则停止遍历
     *
     * @param args
     */
    public final void execute(Object... args) {
        new PackageDirectoryIterator(root).executeDirectoryFirst(args);
    }

    /**
     * 每次迭代到一个新类，则会加载类实例，并调用此方法
     *
     * @param cls  类实例
     * @param args 扩展数据
     */
    protected abstract void handler(Class cls, Object... args);

    private class PackageDirectoryIterator extends FileIterator {
        PackageDirectoryIterator(File root) {
            super(root);
        }

        @Override
        protected void handler(int level, File nowDir, File file, VariableBoolean stopIterator, VariableBoolean levelNext, Object... args) {
            if (file.getName().lastIndexOf(".class") > 0) {
                String packageName = nowDir.getAbsolutePath().substring(root.getAbsolutePath().length());
                if (!packageName.isEmpty()) {
                    if (packageName.charAt(0) == '/' || packageName.charAt(0) == '\\') {
                        packageName = packageName.substring(1);
                    }
                    if (!packageName.isEmpty()) {
                        packageName = packageName.replaceAll("/", ".").replaceAll("\\\\", ".");
                    }
                }

                if (!packageName.isEmpty()) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().lastIndexOf("."));
                    try {
                        ClassIterator.this.handler(Class.forName(className), args);
                    } catch (Exception ex) {
                        log.error("加载类失败: " + className, ex);
                    }
                }
            }
        }
    }
}
