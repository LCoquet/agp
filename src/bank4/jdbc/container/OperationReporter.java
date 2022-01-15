package bank4.jdbc.container;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.aspectj.lang.JoinPoint;

import bank4.jdbc.client.AbstractClient;

public class OperationReporter {

	private static final String OPERATION_FILE = "src/bank3/spring/container/Operations.txt";
	
	public void treatedOperationEntry(JoinPoint joinPoint) {
		
		Object[] args = joinPoint.getArgs();
		AbstractClient client = (AbstractClient) args[0];
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(OPERATION_FILE, true));
			writer.write("Dear boss, today, a new operation has been done !");
			writer.newLine();
			writer.write("Here is some information about the operation : ");
			writer.newLine();
			writer.write(client.getOperation().print(client.getAccount()));
			writer.newLine();
			writer.write("-----------------------------------------------------------------------------");
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
}
