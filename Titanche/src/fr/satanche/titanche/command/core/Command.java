package fr.satanche.titanche.command.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	public String name();
	public String description() default "Sans description.";
	public ExecutorType type() default ExecutorType.ALL;
	public RangeType range() default RangeType.ALL;
	
	public enum ExecutorType{
		ALL, USER, CONSOLE;
	}
	
	public enum RangeType{
		ALL, PUBLIC, PRIVATE;
	}
}
