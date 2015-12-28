package com.dxz.helloworld.download;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Author: dxz
 * Date: 15-12-28
 * Time: 中午12:30
 */
@Table(name = "download", onCreated = "CREATE UNIQUE INDEX index_name ON download(fileSavePath)")
public class DownloadInfo {

    public DownloadInfo() {
    }

    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "state")
    private DownloadState state = DownloadState.STOPPED;

    /**
     * 下载的url
     **/
    @Column(name = "url")
    private String url;

    /**
     * 本地存储路径
     **/
    @Column(name = "fileSavePath")
    private String fileSavePath;

    @Column(name = "progress")
    private int progress;

    /**
     * 文件长度
     **/
    @Column(name = "fileLength")
    private long fileLength;

    @Column(name = "autoResume")
    private boolean autoResume;

    @Column(name = "autoRename")
    private boolean autoRename;

    /**
     * 下载线程数
     **/
    @Column(name = "threads")
    private int threads;

    /**
     * 是否支持断点续传
     **/
    @Column(name = "range")
    private boolean range;
    /**
     * 是否成功
     **/
    @Column(name = "success")
    protected boolean success;

    /**
     * 如果长度不一样则重新下载
     **/
    @Column(name = "again")
    private boolean again;

    @Column(name = "isUpdate")
    private boolean isUpdate;

    @Column(name = "loadedLength")
    private long loadedLength;

    @Column(name = "real_url")
    private String real_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isAgain() {
        return again;
    }

    public void setAgain(boolean again) {
        this.again = again;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public long getLoadedLength() {
        return loadedLength;
    }

    public void setLoadedLength(long loadedLength) {
        this.loadedLength = loadedLength;
    }

    public String getReal_url() {
        return real_url;
    }

    public void setReal_url(String real_url) {
        this.real_url = real_url;
    }

    public List<ThreadEntity> getThreadsEntities(DbManager db) throws DbException {
        return db.selector(ThreadEntity.class).where("downloadId", "=", this.id).findAll();
    }

    public static DownloadInfo getDownloadInfoByUrl(DbManager db, String url) throws DbException {

        DownloadInfo info = db.selector(DownloadInfo.class).where("url", "=", url).findFirst();
        return info;
    }

    public void update(DbManager db)throws DbException {
        db.update(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadInfo)) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
