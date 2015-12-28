package com.dxz.helloworld.download;


import java.util.List;


/**
 * Created by dxz on 2015/12/23.
 * Description: 类描述
 * <p/>
 * History:
 */
public class FileEntity {


    protected int id;
    /**
     * 下载的url
     **/
    protected String url;
    /**
     * 本地存储路径
     **/
    protected String path;
    /**
     * 文件长度
     **/
    protected long length;
    /**
     * 下载进程
     **/
    protected int threads;
    /**
     * 是否支持断点续传
     **/
    protected boolean range;
    /**
     * 是否成功
     **/
    protected boolean isSuccess;

    /**
     * 如果长度不一样则重新下载
     **/
    private boolean again;
    private boolean isUpdate;
    private long loadedLength;
    private String real_url;
    private List<ThreadEntity> threadsEntities;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public long getLength() {
        return length;
    }


    public void setLength(long length) {
        this.length = length;
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
        return isSuccess;
    }


    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }


    public List<ThreadEntity> getThreadsEntities() {
        return threadsEntities;
    }


    public void setThreadsEntities(List<ThreadEntity> threadsEntities) {
        this.threadsEntities = threadsEntities;
    }


    public boolean isAgain() {
        return again;
    }


    public void setAgain(boolean again) {
        this.again = again;
    }

    public static FileEntity getEntityByUrl(String url) {

//        Selector selector = Selector.from(FileEntity.class);
//        selector.select(" * ").where(WhereBuilder.b("url", "=", url));
//        List<FileEntity> fileEntities = Ioc.getIoc().getDb().findAll(selector);
//        if (fileEntities == null || fileEntities.size() == 0) {
//            return null;
//        }
//        FileEntity entity = fileEntities.get(0);
//        Selector selector2 = Selector.from(ThreadEntity.class);
//        selector2.select(" * ").where(WhereBuilder.b("ThreadId", "=", entity.getId()));
//        List<ThreadEntity> entities = Ioc.getIoc().getDb().findAll(selector2);
//        entity.setThreadsEntities(entities);
        return null;
    }

    public void update() {
//        Ioc.getIoc().getDb().update(this);
    }

    @Override
    public String toString() {
        return "FileEntity [id=" + id + ", url=" + url + ", path=" + path + ", length=" + length + ", threads=" + threads + ", range=" + range + ", isSuccess=" + isSuccess + ", again=" + again + ", isUpdate=" + isUpdate + ", loadedLength=" + loadedLength + ", real_url=" + real_url + ", threadsEntities=" + threadsEntities + "]";
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


    public boolean isUpdate() {
        return isUpdate;
    }


    public void setUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }


}
