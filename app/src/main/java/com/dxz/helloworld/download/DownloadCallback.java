package com.dxz.helloworld.download;

import org.xutils.common.Callback;

import java.io.File;

/**
 * Created by dxz on 15/12/28.
 */
/*package*/ class DownloadCallback implements
        Callback.CommonCallback<File>,
        Callback.ProgressCallback<File>,
        Callback.Cancelable {

    private DownloadInfo downloadInfo;
    private ThreadEntity threadEntity;
    private boolean cancelled = false;
    private Cancelable cancelable;

    public DownloadCallback(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public void setCancelable(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    @Override
    public void onWaiting() {
        downloadInfo.setState(DownloadState.WAITING);
    }

    @Override
    public void onStarted() {
        downloadInfo.setState(DownloadState.STARTED);
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        if (isDownloading) {
            downloadInfo.setState(DownloadState.STARTED);
            downloadInfo.setFileLength(total);
            downloadInfo.setProgress((int) (current * 100 / total));
        }
    }

    @Override
    public void onSuccess(File result) {
        synchronized (DownloadCallback.class) {
            downloadInfo.setState(DownloadState.FINISHED);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        synchronized (DownloadCallback.class) {
            downloadInfo.setState(DownloadState.ERROR);
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        synchronized (DownloadCallback.class) {
            downloadInfo.setState(DownloadState.STOPPED);
        }
    }

    @Override
    public void onFinished() {
        cancelled = false;
    }

    private boolean isStopped() {
        DownloadState state = downloadInfo.getState();
        return isCancelled() || state.value() > DownloadState.STARTED.value();
    }

    @Override
    public void cancel() {
        cancelled = true;
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
