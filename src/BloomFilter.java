import java.util.*;

public class BloomFilter {

	private byte B[];
	private int tamanho;
	private int k=0;
	
	private static int hashMurmur (final byte[] data, int length, int seed) {
		// 'm' and 'r' are mixing constants generated offline.
		// They're not really 'magic', they just happen to work well.
		final int m = 0x5bd1e995;
		final int r = 24;
		// Initialize the hash to a random value
		int h = seed^length;
		int length4 = length/4;

		for (int i=0; i<length4; i++) {
			final int i4 = i*4;
			int k = (data[i4+0]&0xff) +((data[i4+1]&0xff)<<8)
					+((data[i4+2]&0xff)<<16) +((data[i4+3]&0xff)<<24);
			k *= m;
			k ^= k >>> r;
			k *= m;
			h *= m;
			h ^= k;
		}
		
		// Handle the last few bytes of the input array
		switch (length%4) {
		case 3: h ^= (data[(length&~3) +2]&0xff) << 16;
		case 2: h ^= (data[(length&~3) +1]&0xff) << 8;
		case 1: h ^= (data[length&~3]&0xff);
				h *= m;
		}

		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;

		return h;
	}

	private int indiceHash(String palavra){
		final byte[] bytes = palavra.getBytes(); 
		int h = Math.abs((this.hashMurmur( bytes, bytes.length, 0x9747b28c)));
		return h%this.tamanho;
	}

	public BloomFilter(int n){
		this.tamanho=n;
		this.B = new byte[n];
		for(int i=0; i<n; i++){
			this.B[i]=0;
		}
	}
	
	public BloomFilter(int n, int k){
		this.k=k;
		this.tamanho=n;
		this.B = new byte[n];
		for(int i=0; i<n; i++){
			this.B[i]=0;
		}
	}
	
	public BloomFilter(int keys, double p){ //criar com numero de elementos a ser inseridos e probabilidade de falsos positivos
		int n=(int) ((keys*Math.log(1/p))/Math.pow(Math.log(2), 2));
		int k=(int) ((n*Math.log(2))/keys);
		this.k=k;
		this.tamanho=n;
		this.B = new byte[n];
		for(int i=0; i<n; i++){
			this.B[i]=0;
		}
	}
	
	public int getK() {
		return k;
	}
	
	public int lengthB() {
		return B.length;
	}

	public void insertMember(String palavra, int k){
		if(this.k==0){
			this.k=k;
		}
		if(this.k==k){
			for(int a=0; a<k; a++){
				String chave=palavra+Integer.toString(a);
				int i = this.indiceHash(chave);
				this.B[i]=1;
			}
		}
	}
	
	public boolean isMember(String palavra, int k){
		boolean control=false;
		if(this.k==k){
			control=true;
			for(int a=0; a<k; a++){
				String chave=palavra+Integer.toString(a);
				int i = this.indiceHash(chave);
				if(this.B[i]!=1){
					control=false;
					break;
				}
			}
		}
		return control;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.B);
	}
}
