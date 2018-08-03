package com.jbm.game.bydr.tcp.fight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbm.game.bydr.manager.RoomManager;
import com.jbm.game.bydr.struct.role.Role;
import com.jbm.game.bydr.struct.room.Room;
import com.jbm.game.engine.handler.HandlerEntity;
import com.jbm.game.engine.handler.TcpHandler;
import com.jbm.game.engine.thread.ThreadType;
import com.jbm.game.engine.util.TimeUtil;
import com.jbm.game.message.Mid.MID;
import com.jbm.game.message.bydr.BydrFightMessage.FireRequest;
import com.jbm.game.message.bydr.BydrFightMessage.FireResponse;
import com.jbm.game.model.constant.Reason;

/**
 * 开炮
 * @author JiangBangMing
 *
 * 2018年8月3日 下午3:37:47
 */

@HandlerEntity(mid=MID.FireReq_VALUE,msg=FireRequest.class,thread=ThreadType.ROOM)
public class FireHandler extends TcpHandler{
	private static final Logger logger=LoggerFactory.getLogger(FireHandler.class);
	
	@Override
	public void run() {
		FireRequest req=getMsg();
		Role role=getPerson();
		
		Room room=RoomManager.getInstance().getRoom(role.getRoomId());
		
		if(role.getGold()<req.getGold()) {
			return;
		}
		
		role.getFireGolds().add(req.getGold());
		
		role.changeGold(-req.getGold(), Reason.RoleFire);
		role.addBetGold(req.getGold());
		role.setFireTime(TimeUtil.currentTimeMillis());
		room.addAllFireCount();
		role.addFireCount();
		
		FireResponse.Builder builder=FireResponse.newBuilder();
		builder.setRid(role.getId());
		builder.setGold(req.getGold());
		builder.setAngleX(req.getAngleX());
		builder.setAngleY(req.getAngleY());
		builder.setTargetFishId(req.getTargetFishId());
		FireResponse response=builder.build();
		room.getRoles().values().forEach( roomRole ->{
			roomRole.sendMsg(response);
		});
		
	}
}
