package util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import entities.Conta;

public class IndexMaker {
	
	public static boolean makeIndexFile(RandomAccessFile arq,RandomAccessFile indxArq) {
		try {
			indxArq.setLength(0);
			if (arq.length()<=4)
				return false;
			arq.seek(0);
			arq.readInt();
			ArrayList<Long> positions = new ArrayList<>();
			ArrayList<Integer> indexes = new ArrayList<>();
			readValidPositionsAndIndex(arq, positions,indexes);
			//System.out.println("number of valid positions"+positions.size());
			//System.out.println("number of valid indexes"+indexes.size());
			if (positions.size()==0)
				return false;
			indxArq.writeInt(positions.size());
			for(int i =0;i<positions.size();i++) {
				indxArq.writeInt(indexes.get(i));
				indxArq.writeInt(positions.get(i).intValue());
			}
			return true;
			
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	} 
	
	public static void readValidPositionsAndIndex(RandomAccessFile arq,ArrayList<Long> positions,ArrayList<Integer> indexes) {
		
		byte tmpLapid;
		int tmpTam;
		try {
			long tmpPos = arq.getFilePointer();
			long tmpLen = arq.length();
			//procura por todo o arquivo registros validos (com lapides aceitas)
			while (tmpPos < tmpLen) {
				Conta acc = null;
				tmpLapid = arq.readByte();
				tmpTam = arq.readInt();
				byte[] tmpByteArray;
				tmpByteArray = new byte[tmpTam];
				arq.read(tmpByteArray);
				//ao encontrar um registro valido interrompe a operação
				if (tmpLapid == 0) {
					positions.add(tmpPos);
					acc = new Conta();
					acc.fromByteArray(tmpByteArray);
					indexes.add(acc.getId());
//					System.out.println("Position found: "+acc.getId()+" at: "+tmpPos);
				}
				tmpPos = arq.getFilePointer();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
