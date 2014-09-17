package com.fantasy.framework.lucene.cluster;

public class MessageFactory {

	public static EntityMessage createInsertMessage(Object entity) {
		EntityMessage message = new EntityMessage();
		message.setType(1);
		message.setEntity(entity);
		return message;
	}

	public static EntityMessage createUpdateMessage(Object entity) {
		EntityMessage message = new EntityMessage();
		message.setType(2);
		message.setEntity(entity);
		return message;
	}

	public static ClassIdMessage createRemoveMessage(Class<?> clazz, String id) {
		ClassIdMessage message = new ClassIdMessage();
		message.setType(3);
		message.setClazz(clazz);
		message.setId(id);
		return message;
	}

	public static ClassIdMessage createRefByMessage(Class<?> clazz, String id) {
		ClassIdMessage message = new ClassIdMessage();
		message.setType(4);
		message.setClazz(clazz);
		message.setId(id);
		return message;
	}
}
