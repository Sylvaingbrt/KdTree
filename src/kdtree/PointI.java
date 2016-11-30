package kdtree;

import java.util.ArrayList;

public abstract class PointI 
{
	protected int v[]; 

	public int get(int i) {
		return v[i];
	}

	public void add(PointI p) {
		for(int i=0;i<v.length; ++i) {
			v[i] += p.v[i];
		}
	}
	public void div(int d) {
		for(int i=0;i<v.length; ++i) {
			v[i] /= d;
		}
	}
	
	public int sqrDist(PointI p) {
		int res = 0;
		for(int i=0;i<v.length; ++i) {
			res += (v[i]-p.v[i])*(v[i]-p.v[i]);
		}
		return res;
	}

	public boolean equals(PointI p) {
		for(int i=0;i<v.length; ++i) {
			if(v[i]!=p.v[i]) return false;
		}
		return true;
	}
	
	int[] cloneValues() {
		return v.clone();		
	}
	
	// this method should be static but that's not possible in Java...
	public abstract PointI zero();
	
	public void insert(ArrayList<PointI> L, int maxSize, int Dself, PointI relative){
		int size = L.size();
		if(size == 0){
			L.add(this);
		}else if(!L.contains(this)){
			if(size < maxSize){
				int j = 0;
				while(j<size && Dself < L.get(j).sqrDist(relative)){
					j += 1;	
				}
				L.add(j, this);
			}else{
				int j = 0;
				while(j < size - 1 && Dself <= L.get(j).sqrDist(relative)){
					L.set(j, L.get(j+1));
					j += 1;
				}
				if(j==size - 1){
					L.set(j, this);
				}else{
					L.set(j-1, this);
				}
			}
		}
	}
}
