package io.vergil.common.lang.ssh;

import java.text.DecimalFormat;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * 文件传输监控
 * 
 * @author zhaowei
 * @date 2015年11月27日下午6:52:32
 */
public class SftpProcessMonitorImpl implements SftpProgressMonitor {
	private long fileSize;
	private long transfered;
	private double processPercent;
	private boolean isEnd;

	public SftpProcessMonitorImpl(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getTransfered() {
		return transfered;
	}

	public double getProcessPercent() {
		return processPercent;
	}

	public boolean isEnd() {
		return isEnd;
	}

	@Override
	public boolean count(long arg0) {
		add(arg0);
		return true;
	}

	@Override
	public void end() {
		isEnd = true;
	}

	@Override
	public void init(int arg0, String arg1, String arg2, long arg3) {
	}

	private synchronized void add(long count) {
		transfered = transfered + count;
		if (fileSize != 0) {
			double d = ((double) transfered * 100) / (double) fileSize;
			DecimalFormat df = new DecimalFormat("#.##");
			this.processPercent = Double.parseDouble(df.format(d));
		}
	}
}
