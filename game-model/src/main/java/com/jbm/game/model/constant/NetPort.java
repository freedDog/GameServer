package com.jbm.game.model.constant;

/**
 * 网络端口常量
 * @author JiangBangMing
 *
 * 2018年7月23日 下午1:47:08
 */
public class NetPort {

	/** 集群Tcp端口 */
	public static int CLUSTER_PORT = 8000;
	/** 集群http端口 */
	public static int CLUSTER_HTTP_PORT = 8001;

	/** 网关用户Tcp端口 */
	public static int GATE_USER_PORT = 8002;
	/** 网关游戏 tcp端口 (固定死) */
	public static int GATE_GAME_PORT = 9003;
	/** 网关用户UPD端口 */
	public static int GATE_USER_UDP_PORT = 8004;
	/** 网关用户HTTP端口 */
	public static int GATE_HTTP_PORT = 8006;
	/** 网关用户WebSocket端口 */
	public static int GATE_USER_WEBSOCKET_PORT = 8007;
	
	
	
	
	// ======游戏服端口9000开始=======

	/** 捕鱼达人 */
	public static int GAME_BYDR_PORT = 9001;
	/**捕鱼达人世界服*/
	public static int GAME_BYDR_WORLD_HTTP_PORT = 9992;
}
