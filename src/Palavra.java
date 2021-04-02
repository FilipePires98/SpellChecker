import java.util.*;

public class Palavra {

    private int shingle=2;
    private LinkedHashSet<Integer> string_in_hash=new LinkedHashSet();

    private int primo(int N) {
        int p;
        for(p=N+1; p<2*N; p++) {
            if(p%2==0) {
                continue;
            }
            boolean pr=true;
            for(int i=p-1; i>2; i--) {
                if(p%i==0) {
                    pr=false;
                    break;
                }
            }
            if(pr==true) {
                break;
            }
        }
        return p;
    }

    private static int hashMurmur (final byte[] data, int length, int seed) {
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
        return h;
    }

    public Palavra(String a) {
        for(int i=0; i<a.length()-this.shingle+1; i++) {
            this.string_in_hash.add(this.indiceHash(a.substring(i, i+this.shingle)));
        }
    }

    public double compare(String word, int hk, double threshold) {

        LinkedHashSet<Integer> tocompare=new LinkedHashSet();

        for(int i=0; i<word.length()-this.shingle+1; i++) {
            tocompare.add(this.indiceHash(word.substring(i, i+this.shingle)));
        }
        int N=(int) ((((this.string_in_hash.size()*(tocompare.size()))/2)*Math.log(1/0.0001))/Math.pow(Math.log(2), 2));
        if (N == 0) {
            return 0;
        }
        int p=this.primo(N);
        if(lsh(string_in_hash,tocompare, 3, 5, p, N)==0) {
            return 0;
        }

        LinkedList<Integer> signatureThis=new LinkedList();
        LinkedList<Integer> signatureWord=new LinkedList();

        for(int i=0; i<hk; i++) {
            int a=(int) (Math.random()*Integer.MAX_VALUE);
            int b=(int) (Math.random()*Integer.MAX_VALUE);

            signatureThis.addLast(signature(this.string_in_hash, a, b, p, N));
            signatureWord.addLast(signature(tocompare, a, b, p, N));
        }

        double inter=0;
        double uni=0;

        for(int i=0; i<signatureThis.size(); i++) {
            int n1=(int) signatureThis.get(i);
            int n2=(int) signatureWord.get(i);
            if(n1==n2) {
                inter++;
            }
            uni++;
        }
        if (inter/uni> threshold) {
            return (inter/uni);
        } else {
            return 0;
        }
    }

    public int signature(LinkedHashSet<Integer> set, int a, int b, int p, int N) {
        Iterator<Integer> iter = set.iterator();
        int mincontrol=0;
        int min=Math.abs(((a*iter.next()+b)%p)%N);
        int r=0;
        while(iter.hasNext()) {
            r=Math.abs(((a*iter.next()+b)%p)%N);
            if(r<min) {
                min=r;
            }
        }
        return min;
    }


    public int lsh(Set<Integer> set1, Set<Integer> set2, int rows, int hf, int p, int N) {
        Iterator<Integer> iter1 = set1.iterator();
        Iterator<Integer> iter2 = set2.iterator();
        int indice=0;
        List<Integer> banda1=new ArrayList<>();
        List<Integer> banda2=new ArrayList<>();
        while(iter1.hasNext() && iter2.hasNext()) {
            banda1.add(iter1.next());
            banda2.add(iter2.next());
            indice++;
            if(indice%rows==0) {
                for(int i=0; i<hf; i++) {
                    int a=(int) (Math.random()*Integer.MAX_VALUE);
                    int b=(int) (Math.random()*Integer.MAX_VALUE);
                    int min1=Math.abs(((a*(banda1.get(0))+b)%p)%N);
                    int min2=Math.abs(((a*(banda2.get(0))+b)%p)%N);
                    for(int k=1; k<banda1.size(); k++) {
                        if(Math.abs(((a*banda1.get(k)+b)%p)%N)<min1) {
                            min1=Math.abs(((a*banda1.get(k)+b)%p)%N);
                        }
                        if(Math.abs(((a*banda2.get(k)+b)%p)%N)<min1) {
                            min1=Math.abs(((a*banda2.get(k)+b)%p)%N);
                        }
                    }
                    if(min1==min2) {
                        return 1;
                    }
                }
                banda1.clear();
                banda2.clear();
            }
        }
        return 0;
    }


    public Set<Integer> getString_in_hash() {
        return this.string_in_hash;
    }

    //public List<Integer> getSignature() {
    //	return this.signature;
    //}

    //@Override
    //public String toString() {
    //	return signature.toString();
    //}

}