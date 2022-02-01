package com.laplace.dove.batcharchiver.utils.archive;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CommonsCompressUtil {
    public static void archive(ArchiveOutputStream aos, List<File> srcFiles, File root,
                               Consumer<File> currentFileNotifyer, BiFunction<File, String, ArchiveEntry> entryBuilder) throws IOException {
        for (File file:srcFiles) {
            if (currentFileNotifyer != null){
                currentFileNotifyer.accept(file);
            }

            String basePath = file.getAbsolutePath().replace(
                    root.getAbsolutePath() + File.separator, "");
            ArchiveEntry entry= entryBuilder.apply(file, basePath);
            aos.putArchiveEntry(entry);
            if (file.isFile()){
                IOUtils.copy(new FileInputStream(file), aos);
            }
            aos.closeArchiveEntry();
        }
    }
}
