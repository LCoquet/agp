package bank4.jdbc.container;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;

public class OperationReporter {

	private static final String OPERATION_FILE = "src/bank4/jdbc/container/Operations.txt";
	
	public void test(JoinPoint joinPoint) {
		System.out.println("Hello i am here.");
	}
	
	public void treatedOperationEntry(StaticPart staticPart, Object result) {
		String name = staticPart.getSignature().toShortString();
		System.out.println(name + " returning: [" + result + "]");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(OPERATION_FILE, true));
			writer.write("Dear boss, today, a new operation has been done !");
			writer.newLine();
			writer.write("Here is some information about the operation : ");
			writer.newLine();
			writer.write((String) result);
			writer.newLine();
			writer.write("-----------------------------------------------------------------------------");
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
}
