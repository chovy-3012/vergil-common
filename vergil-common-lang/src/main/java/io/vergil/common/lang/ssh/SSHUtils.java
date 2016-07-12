package io.vergil.common.lang.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import io.vergil.common.lang.StringUtils;

/**
 * ssh工具类
 * 
 * @author zhaowei
 * @date 2015年11月27日下午5:17:06
 */
public class SSHUtils {

	/**
	 * 传输文件
	 * 
	 * @author zhaowei
	 * @date 2015年11月27日下午5:17:17
	 * @param ip
	 * @param port
	 * @param user用户名
	 * @param psw密码
	 * @param dstDir目标文件夹
	 * @param srcFile源文件
	 * @throws JSchException
	 * @throws SftpException
	 * @throws IOException
	 */
	public static void sshSftp(String ip, int port, String user, String psw, String dstDir, File srcFile)
			throws JSchException, SftpException, IOException {
		Session session = null;
		Channel channel = null;
		OutputStream outstream = null;
		InputStream instream = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, ip, port);
			session.setPassword(psw);// 设置密码
			// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			// 设置登陆超时时间
			session.connect(30000);
			// 创建sftp通信通道
			channel = session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			sftp.cd(dstDir);
			outstream = sftp.put(srcFile.getName(), new SftpProcessMonitorImpl(srcFile.length()),
					ChannelSftp.OVERWRITE);
			instream = new FileInputStream(srcFile);
			byte b[] = new byte[1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}
		} catch (JSchException e) {
			throw e;
		} catch (SftpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				outstream.flush();
				outstream.close();
				instream.close();
			} catch (IOException e) {
			}
			session.disconnect();
			channel.disconnect();
		}
	}

	/**
	 * @author zhaowei
	 * @date 2015年11月30日上午9:47:16
	 * @param ip
	 * @param user
	 * @param psw
	 * @param dstDir
	 * @param srcFile
	 * @throws JSchException
	 * @throws SftpException
	 * @throws IOException
	 */
	public static void sshSftp(String ip, String user, String psw, String dstDir, File srcFile)
			throws JSchException, SftpException, IOException {
		sshSftp(ip, 22, user, psw, dstDir, srcFile);
	}

	/**
	 * @author zhaowei
	 * @date 2015年11月30日上午9:21:26
	 * @param ip
	 * @param port
	 * @param user
	 * @param psw
	 * @param dstDir
	 * @param srcName
	 * @param srcIn
	 *            输入流
	 * @throws Exception
	 */
	public static void sshSftp(String ip, int port, String user, String psw, String dstDir, String srcName,
			InputStream srcIn) throws JSchException, SftpException, IOException {
		Session session = null;
		Channel channel = null;
		OutputStream outstream = null;
		InputStream instream = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, ip, port);
			session.setPassword(psw);// 设置密码
			// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			// 设置登陆超时时间
			session.connect(30000);
			// 创建sftp通信通道
			channel = session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;
			// 进入服务器指定的文件夹
			sftp.cd(dstDir);
			outstream = sftp.put(srcName, new SftpProcessMonitorImpl(srcIn.available()), ChannelSftp.OVERWRITE);
			instream = srcIn;
			byte b[] = new byte[1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}
		} catch (JSchException e) {
			throw e;
		} catch (SftpException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				outstream.flush();
				outstream.close();
				instream.close();
			} catch (IOException e) {
			}
			session.disconnect();
			channel.disconnect();
		}
	}

	/**
	 * 文件夹是否存在
	 * 
	 * @author zhaowei
	 * @date 2015年11月30日上午9:36:00
	 * @param ip
	 * @param port
	 * @param user
	 * @param psw
	 * @param dstDir
	 * @return
	 * @throws JSchException
	 * @throws SftpException
	 */
	public static boolean sshDir(String ip, int port, String user, String psw, String dstDir)
			throws JSchException, SftpException {
		Session session = null;
		Channel channel = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, ip, port);
			// 设置登陆主机的密码
			session.setPassword(psw);// 设置密码
			// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			// 设置登陆超时时间
			session.connect(30000);
			// 创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;
			@SuppressWarnings("rawtypes")
			Vector content = sftp.ls(dstDir);
			if (content != null) {
				return true;
			}
		} catch (JSchException | SftpException e) {
		} finally {
			session.disconnect();
			channel.disconnect();
		}
		return false;
	}

	/**
	 * @author zhaowei
	 * @date 2015年11月30日上午9:35:36
	 * @param host
	 * @param port
	 * @param user
	 * @param passwd
	 * @param command
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 */
	public static String execCmd(String host, int port, String user, String passwd, String command)
			throws JSchException, IOException {
		Session session = null;
		Channel channel = null;
		BufferedReader reader = null;
		try {
			JSch jsch = new JSch();
			// 连接服务器，采用默认端口
			session = jsch.getSession(user, host, port);
			session.setPassword(passwd);// 设置密码
			// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			// 设置登陆超时时间
			session.connect(30000);
			if (command != null) {
				channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);
				channel.connect();
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				StringBuffer sb = new StringBuffer();
				String buf = null;
				while ((buf = reader.readLine()) != null) {
					sb.append(buf).append("\n");
				}
				return sb.toString();
			}
		} catch (JSchException | IOException e) {
			throw e;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
			channel.disconnect();
			session.disconnect();
		}
		return null;
	}

	/**
	 * @author zhaowei
	 * @date 2015年11月30日上午9:48:32
	 * @param host
	 * @param user
	 * @param passwd
	 * @param command
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 */
	public static String execCmd(String host, String user, String passwd, String command)
			throws JSchException, IOException {
		return execCmd(host, 22, user, passwd, command);
	}

	public static String execCmdExt(String command, String user, String passwd, String host) throws JSchException {
		Session session = null;
		Channel channel = null;

		JSch jsch = new JSch();

		// 连接服务器，采用默认端口
		session = jsch.getSession(user, host);

		session.setPassword(passwd);// 设置密码
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		// 设置登陆超时时间
		session.connect(30000);

		BufferedReader reader = null;

		try {
			if (command != null) {

				channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);

				channel.setInputStream(null);

				channel.connect();
				InputStream in = channel.getExtInputStream();
				reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				StringBuffer sb = new StringBuffer();
				String buf = null;
				while ((buf = reader.readLine()) != null) {
					sb.append(buf).append("\n");
				}
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			channel.disconnect();
			session.disconnect();
		}
		return null;
	}

	/**
	 * 检测连通
	 * 
	 * @author zhaowei
	 * @date 2016年3月2日下午2:25:36
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean detectConnection(String ip, int sshPort, String username, String password) {
		JSch jSch = new JSch();
		Session session = null;
		try {
			session = jSch.getSession(username, ip, sshPort);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect(3000); // 登录超时时间
			return true;
		} catch (JSchException e) {
			// 忽略
			return false;
		} finally {
			session.disconnect();
		}
	}

	/**
	 * 检测目标主机的端口是否通
	 * 
	 * @author zhaowei
	 * @date 2016年3月3日下午12:29:11
	 * @param ip
	 * @param sshPort
	 * @param detectPort
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean detectPort(String ip, int sshPort, int detectPort, String username, String password) {
		String command = "netstat -apn |grep " + detectPort;
		try {
			String execCmd = execCmd(ip, sshPort, username, password, command);
			if (StringUtils.isBlank(execCmd)) {
				return true;
			}
		} catch (JSchException | IOException e) {
			return false;
		}
		return false;
	}
}
