package fr.satanche.titanche.command.core;

import java.lang.reflect.Method;

import fr.satanche.titanche.command.core.Command.ExecutorType;
import fr.satanche.titanche.command.core.Command.RangeType;

public class SimpleCommand {

	private final String name, description;
	private final ExecutorType executorType;
	private final RangeType rangeType;
	private final Object object;
	private final Method method;
	
	public SimpleCommand(String name, String description, ExecutorType executorType, RangeType rangeType, Object object, Method method) {
		super();
		this.name = name;
		this.description = description;
		this.executorType = executorType;
		this.rangeType = rangeType;
		this.object = object;
		this.method = method;
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public ExecutorType getExecutorType() {
		return executorType;
	}
	public Object getObject() {
		return object;
	}
	public Method getMethod() {
		return method;
	}
	
	public RangeType getRangeType(){
		return rangeType;
	}
}
