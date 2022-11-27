package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import entities.Conta;

public class BDStringfier {
	
	public static void makeTXT(RandomAccessFile arq,RandomAccessFile indxArq,String path) throws IOException {
		if (arq.length()==0||indxArq.length()==0)
			return;
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			int tmpId;
			Conta tmpAcc;
			arq.seek(0);
			String lastId =""+ arq.readInt();			
			bw.write(lastId);
			bw.newLine();
			indxArq.seek(0);
			int indxTam=indxArq.readInt();
			for (int i = 0; i < indxTam; i++) {
				tmpId=indxArq.readInt();
				indxArq.readInt();
				tmpAcc=CRUD.readIndex(tmpId, arq, indxArq);
				bw.write(tmpAcc.txtWriter());
				bw.newLine();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void readTXT(RandomAccessFile arq,String path) throws IOException {
		arq.setLength(0);
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
			
			arq.seek(0);
			int lastId =Integer.parseInt(br.readLine());			
			arq.writeInt(lastId);
			while (br.ready()) {
				Conta tmpAcc = new Conta();
				tmpAcc.txtReader(br.readLine());
				byte[] accBytes=tmpAcc.toByteArray();
				arq.writeByte(0);
				arq.writeInt(accBytes.length);
				arq.write(accBytes);
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
