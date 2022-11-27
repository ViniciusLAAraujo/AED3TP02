package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class LZWEncoder {

	private ArrayList<Integer> outputCode= new ArrayList<Integer>();
	private ArrayList<Character> inputchars= new ArrayList<Character>();
	
	public void lzwencode(RandomAccessFile arq,RandomAccessFile cod) throws IOException {
		arq.seek(0);
		cod.setLength(0);
		readInputFile(arq);
		encode();
		writeToFile(cod);
	}

	public void readInputFile(RandomAccessFile arq)
	{
		
		try {
			int r;
			do {
				r = arq.read();
				char ch = (char) r;
				inputchars.add(ch);
			} while (arq.getFilePointer()<arq.length());

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void encode()
	{
		HashMap<String,Integer> dictionary= new HashMap<String,Integer>();
		for(int i=0;i<256;i++)
		{
			dictionary.put(""+ (char) i,i);
		}
		int nextCode=256;
		StringBuilder str= new StringBuilder();
		String symbol=null;
		str.setLength(0);
		for(int i = 0; i < inputchars.size(); i++) {

			symbol=""+inputchars.get(i);
			if(dictionary.containsKey(str.toString()+symbol))
			{

				str=str.append(symbol);
			}
			else
			{
				outputCode.add(dictionary.get(str.toString()));
				dictionary.put(str.toString()+symbol, nextCode);
				nextCode++;
				str.setLength(0);
				str.append(symbol);

			}
		}
		outputCode.add(dictionary.get(str.toString()));
	}

	public void writeToFile(RandomAccessFile cod)
	{

		if(outputCode.size()>0)
		{
			try {
				
				

				for(int i=0;i<outputCode.size();i++)
				{
					int code=outputCode.get(i);
					cod.writeChar(code);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}
}