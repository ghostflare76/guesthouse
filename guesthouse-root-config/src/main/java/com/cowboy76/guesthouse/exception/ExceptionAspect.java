package com.cowboy76.guesthouse.exception;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ExceptionAspect {
	
	protected Logger log = LoggerFactory.getLogger(ExceptionAspect.class);
	
	@AfterThrowing(pointcut="execution(* com.cowboy76..*Impl.*(..))", throwing="error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) throws Exception {
		
		StringBuffer sbf = new StringBuffer();
		
		Object[] args = joinPoint.getArgs();
		
		sbf.append("\n############## ExceptionAspect Start ######### ");
		sbf.append("\n").append("Signature Name : ").append(joinPoint.getSignature().getName());
		sbf.append("\n").append("Target Class Name : ").append(joinPoint.getTarget().getClass().getName());
		
		
		if (args.length > 0) {
			for (int i=0 ; i  < args.length; i++) {
				sbf.append("\n args[" + i + "] : ").append(args[i].toString());
			} 		
		} else {
			sbf.append("\n No Arguments ");
		}
		
		sbf.append("\n ############### ExceptionAspect End ##############");
		log.debug(sbf.toString());
		log.debug(error.toString());
		
	}

}
