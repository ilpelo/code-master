package aisexample;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.util.function.Consumer;
import dk.dma.enav.model.geometry.Position;

public class Main {

	public static BufferedWriter fileWriter;
	public static String outputFileName = "ais_output.txt";
	public static int msgCounter = 0;
	
	public static void main(String[] args) throws Exception {
		
		try {
		    fileWriter = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(outputFileName), "utf-8"));
		} catch (IOException ex) {
			System.err.println("Cannot open: " + outputFileName);
		} 
		
		FileInputStream dataFile = new FileInputStream("C:\\Users\\andrea\\Desktop\\ANSData_RawDBaisSat 11 Apr 2014 ML22959.dat");
		//AisReader reader = AisReaders.createReaderFromInputStream(Main.class.getResourceAsStream("/ais-data.txt"));
		AisReader reader = AisReaders.createReaderFromInputStream(dataFile);
		reader.registerHandler(new Consumer<AisMessage>() {
		    @Override
		    public void accept(AisMessage aisMessage) {
		    	String outputRecord = "";
		    	outputRecord = outputRecord + aisMessage.getUserId();
		    	if(aisMessage instanceof AisPositionMessage) {				    
			    	String tsUnixEpoch = aisMessage.getVdm().getCommentBlock().getString("c");
			    	outputRecord = outputRecord + "," + tsUnixEpoch;
			    	Position pos = aisMessage.getValidPosition();
			    	if(pos != null) {
				    	double lat = aisMessage.getValidPosition().getLatitude();
				    	outputRecord = outputRecord + "," + String.valueOf(lat);
				    	double lon = aisMessage.getValidPosition().getLongitude();
				    	outputRecord = outputRecord + "," + String.valueOf(lon);
			    	}
		    	    try {
						fileWriter.write(outputRecord);
						fileWriter.newLine();
					} catch (IOException e) {
						System.err.println("Cannot write "+ outputRecord +" to " + outputFileName);
					}
		    	}
		    	
		    	if(msgCounter % 1000 == 0) {
		    		System.out.println("Counter: " + msgCounter);
		    	}
		    	msgCounter += 1;
		    	
		        //System.out.println("message id: " + aisMessage.getMsgId());
		        //System.out.println("user id: " + aisMessage.getUserId());
		        //System.out.println("coord: " + aisMessage.getValidPosition());		        
		        //System.out.println("targetType: " + aisMessage.getTargetType());
		        //System.out.println("ts: " + aisMessage.getVdm().getCommentBlock().getString("c"));
		        //if(aisMessage instanceof AisStaticCommon) {
		        //	AisStaticCommon aisStatic = (AisStaticCommon)aisMessage;
		        //	System.out.println("shipType: " + aisStatic.getShipType());			        
		        //}
		    }
		});
		reader.start();
		reader.join();
		
		try {fileWriter.close();} 
		catch (Exception ex) {
			System.err.println("Cannot close: " + outputFileName);			
		}			
		
	}
	
}
