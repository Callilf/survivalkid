package com.survivalkid.game.util;

import android.os.Handler;
import android.os.Message;

public final class HandlerUtil {
	
	private HandlerUtil() {}
	
	//private static final Context CONTEXT = GameContext.getSingleton().getContext();
// exemple to create in a surfaceView	
//	HandlerUtil.handlerFin = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			Toast.makeText(getContext(), "Survival time : " + msg.arg1 / 1000f + " seconds", Toast.LENGTH_LONG)
//					.show();
//		}
//	};
	
	public enum HandlerEnum { HANDLER_FIN }
	
	public static Handler handlerFin;
	
	public static void sendMessage(int args1, HandlerEnum typeHandler)
	{
		sendMessage(args1,0,typeHandler);
	}
	
	public static void sendMessage(int args1, int args2, HandlerEnum typeHandler) {
		Handler handler = getHandler(typeHandler);
		Message msg = handler.obtainMessage();
		msg.arg1 = args1;
		msg.arg2 = args2;
		handler.sendMessage(msg);		
	}
	
	/**
	 * Get the corresponding handler
	 * 
	 * @param typeHandler type of the handler
	 * @return the handler
	 */
	private static Handler getHandler(HandlerEnum typeHandler) {
		switch (typeHandler) {
		case HANDLER_FIN:
			return handlerFin;
		default:
			return null;
		}
	}
	
	
}
