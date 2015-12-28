package com.dxz.helloworld.event;

import java.io.File;

/**
 * Created by dxz on 2015/12/23.
 */
public class FileResultEvent {

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_START = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_SUCCESS = 3;

    private String url;
    private int progress;
    private int status;
    private long length;
    private long loadedLength;
    private File file;
    protected boolean range;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLoadedLength() {
        return loadedLength;
    }

    public void setLoadedLength(long loadedLength) {
        this.loadedLength = loadedLength;
    }

    @Override
    public String toString() {
        return "FileResultEvent [url=" + url + ", progress=" + progress + ", status=" + status + ", length=" + length + ", loadedLength=" + loadedLength + ", file=" + file + ", range=" + range + "]";
    }
}
