package aisexample;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.util.function.Consumer;

public class Main {

	public static void main(String[] args) throws Exception {
		
		AisReader reader = AisReaders.createReaderFromInputStream(Main.class.getResourceAsStream("/ais-data.txt"));
		reader.registerHandler(new Consumer<AisMessage>() {
		    @Override
		    public void accept(AisMessage aisMessage) {
		        System.out.println("message id: " + aisMessage.getMsgId());
		    }
		});
		reader.start();
		reader.join();
		
	}
	
}
