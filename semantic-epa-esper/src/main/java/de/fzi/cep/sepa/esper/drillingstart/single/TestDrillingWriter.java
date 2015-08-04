package de.fzi.cep.sepa.esper.drillingstart.single;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.espertech.esper.client.EventBean;

import de.fzi.cep.sepa.esper.Writer;

public class TestDrillingWriter implements Writer {

	private PrintWriter writer;
	
	private static final String fileName = "c:\\users\\riemer\\documents\\2015-07-MhWirth-Data\\drilling0.csv";
	
	public TestDrillingWriter() {
		File file = new File(fileName);
		try {
			writer = new PrintWriter(new FileOutputStream(file), true);
			writer.write("time,drilling" +System.lineSeparator());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEvent(EventBean bean) {
		Map map = (Map) bean.getUnderlying();
		MapUtils.debugPrint(System.out, new Object(), (Map) bean.getUnderlying());
		writer.write(map.get("time") +"," +map.get("drillingStatus") +System.lineSeparator());
		writer.flush();
	}

}
