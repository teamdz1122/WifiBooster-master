package com.binhdz.wifibooster.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by admin on 1/6/2018.
 */

public class StorageUtil {
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static StorageSize convertStorageSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        StorageSize sto = new StorageSize();
        if (size >= gb) {

            sto.suffix = "GB";
            sto.value = (float) size / gb;
            return sto;
        } else if (size >= mb) {

            sto.suffix = "MB";
            sto.value = (float) size / mb;

            return sto;
        } else if (size >= kb) {


            sto.suffix = "KB";
            sto.value = (float) size / kb;

            return sto;
        } else {
            sto.suffix = "B";
            sto.value = (float) size;

            return sto;
        }


    }

    public static SDCardInfo getSDCardInfo() {
        // String sDcString = Environment.getExternalStorageState();

        if (Environment.isExternalStorageRemovable()) {
            String sDcString = Environment.getExternalStorageState();
            if (sDcString.equals(Environment.MEDIA_MOUNTED)) {
                File pathFile = Environment
                        .getExternalStorageDirectory();

                try {
                    StatFs statfs = new StatFs(
                            pathFile.getPath());

                    long nTotalBlocks = statfs.getBlockCount();

                    long nBlocSize = statfs.getBlockSize();


                    long nAvailaBlock = statfs.getAvailableBlocks();

                    long nFreeBlock = statfs.getFreeBlocks();

                    SDCardInfo info = new SDCardInfo();
                    // 计算SDCard 总容量大小MB
                    info.total = nTotalBlocks * nBlocSize;

                    // 计算 SDCard 剩余大小MB
                    info.free = nAvailaBlock * nBlocSize;

                    return info;
                } catch (IllegalArgumentException e) {

                }
            }
        }
        return null;
    }

    public static SDCardInfo getSystemSpaceInfo(Context context) {
        File path = Environment.getDataDirectory();
        // File path = context.getCacheDir().getAbsoluteFile();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();

        long totalSize = blockSize * totalBlocks;
        long availSize = availableBlocks * blockSize;
        SDCardInfo info = new SDCardInfo();
        info.total = totalSize;
        info.free = availSize;
        return info;


    }

    public static SDCardInfo getRootSpaceInfo() {
        File path = Environment.getRootDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();

        long totalSize = blockSize * totalBlocks;
        long availSize = availableBlocks * blockSize;
        long nBlocSize = stat.getBlockSize();

        SDCardInfo info = new SDCardInfo();
        info.total = totalSize;

        info.free = availSize;
        return info;

    }
}
