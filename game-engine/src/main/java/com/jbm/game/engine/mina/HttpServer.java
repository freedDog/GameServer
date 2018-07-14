package com.jbm.game.engine.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.engine.mina.code.HttpServerCodecImpl;
import com.jbm.game.engine.mina.config.MinaServerConfig;
import com.jbm.game.engine.mina.handler.HttpServerIoHandler;
import com.jbm.game.engine.util.SysUtil;

/**
 * http 服务
 * @author JiangBangMing
 *
 * 2018年7月10日 上午11:33:41
 */
public class HttpServer implements Runnable{

	private static final Logger logger=LoggerFactory.getLogger(HttpServer.class);
	
	private final MinaServerConfig minaServerConfig;
	
	private final NioSocketAcceptor acceptor;
	
	private final HttpServerIoHandler ioHandler;
	
	protected boolean isRunning=false;//通信是否在运行
	
	private OrderedThreadPoolExecutor threadPool;//默认线程池
	
	public HttpServer(MinaServerConfig minaServerConfig,HttpServerIoHandler ioHandler) {
		this.minaServerConfig=minaServerConfig;
		this.ioHandler=ioHandler;
		acceptor=new NioSocketAcceptor();
	}
	
	@Override
	public void run() {
		synchronized (this) {
			if(!isRunning) {
				isRunning=true;
				new Thread(new BindServer()).start();;
			}
		}
	}
	
	public void stop() {
		synchronized (this) {
			if(!isRunning) {
				logger.info("HttpServer"+minaServerConfig.getName()+"is already stoped.");
				return;
			}
			isRunning=false;
			try {
				if(threadPool!=null) {
					threadPool.shutdown();
				}
				acceptor.unbind();
				acceptor.dispose();
				logger.info("Server is stoped.");
			}catch (Exception e) {
				logger.error("",e);
			}
		}
	}
	
	/**
	 * 绑定端口
	 * @author JiangBangMing
	 *
	 * 2018年7月10日 上午11:37:51
	 */
	private class BindServer implements Runnable{
		
		private  final Logger log=LoggerFactory.getLogger(BindServer.class);
		
		@Override
		public void run() {
			DefaultIoFilterChainBuilder chain=acceptor.getFilterChain();
			chain.addLast("codec",new HttpServerCodecImpl());
			
			//线程队列池
			OrderedThreadPoolExecutor threadPool=new OrderedThreadPoolExecutor(minaServerConfig.getOrderedThreadPoolExecutorSize());
			chain.addLast("threadPool", new ExecutorFilter(threadPool));
			
			acceptor.setReuseAddress(minaServerConfig.isReuseAddress());//允许地址重用
			
			SocketSessionConfig sc=acceptor.getSessionConfig();
			sc.setReuseAddress(minaServerConfig.isReuseAddress());
			sc.setReceiveBufferSize(minaServerConfig.getMaxReadSize());
			sc.setSendBufferSize(minaServerConfig.getSendBufferSizze());
			sc.setTcpNoDelay(minaServerConfig.isTcpNoDelay());
			sc.setSoLinger(minaServerConfig.getSoLinger());
			sc.setIdleTime(IdleStatus.READER_IDLE,minaServerConfig.getReaderIdleTime());
			sc.setIdleTime(IdleStatus.WRITER_IDLE, minaServerConfig.getWriteIdleTime());
			
			acceptor.setHandler(ioHandler);
			try {
				acceptor.bind(new InetSocketAddress(minaServerConfig.getHttpPort()));
				log.info("已开始监听HTTP端口:{} ",minaServerConfig.getHttpPort());
			}catch (Exception e) {
				SysUtil.exit(this.getClass(), e, "监听HTTP 端口：{}已被占用",minaServerConfig.getHttpPort());
			}
		}
	}
}