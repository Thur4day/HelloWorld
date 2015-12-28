package com.dxz.helloworld.download;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by dxz on 2015/12/23.
 * Description: 类描述
 * <p/>
 * History:
 */
@Table(name = "thread")
public class ThreadEntity {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "start")
    protected long start;

    @Column(name = "end")
    protected long end;

    @Column(name = "load")
    protected long load;

    @Column(name = "downloadId", property = "UNIQUE"/*, property = "UNIQUE"//如果是一对一加上唯一约束*/)
    private long downloadId; // 外键表id

    public FileEntity fileEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getLoad() {
        return load;
    }

    public void setLoad(long load) {
        this.load = load;
    }

    public DownloadInfo getDownloadInfo(DbManager db) throws DbException {
        return db.findById(DownloadInfo.class, downloadId);
    }

    public FileEntity getFileEntity() {
        return fileEntity;
    }

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public static void delete(int id) {
        // TODO:
    }

    @Override
    public String toString() {
        return "ThreadEntity [id=" + id + ", start=" + start + ", end=" + end + ", load=" + load + "]";
    }
}
