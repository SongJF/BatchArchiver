package com.laplace.dove.batcharchiver.utils.archive;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArchiveUtil {
    public static void zip(File src, File dest) throws IOException {
        zip(src, dest, null, null, null);
    }

    public static void zip(File src, File dest, ArchiveParam param,
                           Consumer<Integer> fileCountNotifyer, Consumer<File> currentFileNotifyer) throws IOException {
        if (src == null || dest == null){
            return;
        }

        List<File> fileList = new ArrayList<>();
        listDir(src, fileList);
        if (fileCountNotifyer != null){
            fileCountNotifyer.accept(fileList.size());
        }

        zip4jZip(fileList, dest, param, currentFileNotifyer);
    }

    private static void zip4jZip(List<File> src, File dest, ArchiveParam param,
                                 Consumer<File> currentFileNotifyer) throws ZipException {
        ZipFile zipFile = param.canEncrypt() ? new ZipFile(dest, param.getPassword()) : new ZipFile(dest);

        ZipParameters archiveParam = new ZipParameters();
        if (param.canEncrypt()){
            archiveParam.setEncryptFiles(true);
            archiveParam.setEncryptionMethod(EncryptionMethod.valueOf(param.getEncryptionType()));
        }
        archiveParam.setCompressionLevel(CompressionLevel.valueOf(param.getCompressLevel()));
        for (File file:src) {
            if (currentFileNotifyer != null){
                currentFileNotifyer.accept(file);
            }

            if (file.isDirectory()){
                zipFile.addFile(file, archiveParam);
            }
        }
    }

    private static void commonCompressZip(List<File> src, File dest, File root, Consumer<File> currentFileNotifyer) throws IOException {
        try(ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new FileOutputStream(dest))){
            CommonsCompressUtil.archive(zos, src, root, currentFileNotifyer,
                    ZipArchiveEntry::new);
        }
    }

    private static void listDir(File root, List<File> fileList){
        File[] subItems = root.listFiles();
        if (subItems == null){
            return;
        }

        for (File file:subItems) {
            fileList.add(file);
            if (file.isDirectory()){
                listDir(file, fileList);
            }
        }
    }
}
