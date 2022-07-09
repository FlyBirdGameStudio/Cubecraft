package com.sunrisestudio.util;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName ZipUtil
 * @Description 压缩或解压缩zip：由于直接使用java.util.zip工具包下的类，会出现中文乱码问题，所以使用ant.jar中的org.apache.tools.zip下的工具类
 * @Author AlphaJunS
 * @Date 2020/3/8 11:30
 * @Version 1.0
 */
public class ZipUtil {

    /**
     * @Author AlphaJunS
     * @Date 11:32 2020/3/8
     * @Description
     * @param zip 压缩目的地址
     * @param srcFiles 压缩的源文件
     * @return void
     */
    public static void zipFile( String zip , File[] srcFiles ) {
        try {
            if( zip.endsWith(".zip") || zip.endsWith(".ZIP") ){
                FileOutputStream fos = new FileOutputStream(new File(zip));
                ZipOutputStream _zipOut = new ZipOutputStream(fos) ;
                for( File _f : srcFiles ){
                    handlerFile(zip , _zipOut , _f , "");
                }
                fos.close();
                _zipOut.close();
            }else{
                System.out.println("target file[" + zip + "] is not .zip type file");
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * @Author AlphaJunS
     * @Date 11:33 2020/3/8
     * @Description
     * @param zip 压缩的目的地址
     * @param zipOut
     * @param srcFile 被压缩的文件信息
     * @param path 在zip中的相对路径
     * @return void
     */
    private static void handlerFile(String zip , ZipOutputStream zipOut , File srcFile , String path) throws IOException {
        System.out.println(" begin to compression file[" + srcFile.getName() + "]");
        if( !"".equals(path) && ! path.endsWith(File.separator)){
            path += File.separator ;
        }
        if( ! srcFile.getPath().equals(zip) ){
            if( srcFile.isDirectory() ){
                File[] _files = srcFile.listFiles() ;
                if( _files.length == 0 ){
                    zipOut.putNextEntry(new ZipEntry( path + srcFile.getName() + File.separator));
                    zipOut.closeEntry();
                }else{
                    for( File _f : _files ){
                        handlerFile( zip ,zipOut , _f , path + srcFile.getName() );
                    }
                }
            }else{
                InputStream _in = new FileInputStream(srcFile) ;
                zipOut.putNextEntry(new ZipEntry(path + srcFile.getName()));
                int len = 0 ;
                byte[] _byte = new byte[1024];
                while( (len = _in.read(_byte)) > 0  ){
                    zipOut.write(_byte, 0, len);
                }
                _in.close();
                zipOut.closeEntry();
            }
        }
    }

    /**
     * @Author AlphaJunS
     * @Date 11:34 2020/3/8
     * @Description 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     * @param zipPath 待解压缩的ZIP文件名
     * @param descDir 目标目录
     * @return java.util.List<java.io.File>
     */
    public static List<File> unzipFile(String zipPath, String descDir) {
        return unzipFile(new File(zipPath) , descDir) ;
    }

    /**
     * @Author AlphaJunS
     * @Date 11:36 2020/3/8
     * @Description 对.zip文件进行解压缩
     * @param zipFile 解压缩文件
     * @param descDir 压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
     * @return java.util.List<java.io.File>
     */
    @SuppressWarnings("rawtypes")
    public static List<File> unzipFile(File zipFile, String descDir) {
        List<File> _list = new ArrayList<File>() ;
        try {
            ZipFile _zipFile = new ZipFile(zipFile) ;
            for(Enumeration entries = _zipFile.entries(); entries.hasMoreElements() ; ){
                ZipEntry entry = (ZipEntry)entries.nextElement() ;
                File _file = new File(descDir + File.separator + entry.getName()) ;
                if( entry.isDirectory() ){
                    _file.mkdirs() ;
                }else{
                    File _parent = _file.getParentFile() ;
                    if( !_parent.exists() ){
                        _parent.mkdirs() ;
                    }
                    InputStream _in = _zipFile.getInputStream(entry);
                    OutputStream _out = new FileOutputStream(_file) ;
                    int len = 0 ;
                    byte[] _byte = new byte[1024];
                    while( (len = _in.read(_byte)) > 0){
                        _out.write(_byte, 0, len);
                    }
                    _in.close();
                    _out.flush();
                    _out.close();
                    _list.add(_file) ;
                }
            }
        } catch (IOException e) {
        }
        return _list ;
    }

    /**
     * @Author AlphaJunS
     * @Date 11:36 2020/3/8
     * @Description 对临时生成的文件夹和文件夹下的文件进行删除
     * @param delpath
     * @return void
     */
    public static void deletefile(String delpath) {
        try {
            File file = new File(delpath);
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] fileList = file.list();
                for (int i = 0; i < fileList.length; i++) {
                    File delfile = new File(delpath + File.separator + fileList[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + File.separator + fileList[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}