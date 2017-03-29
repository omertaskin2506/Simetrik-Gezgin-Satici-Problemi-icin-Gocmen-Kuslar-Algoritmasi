package pack;

import java.util.ArrayList;
import java.util.List;

public class Kus {
	
	private List<List<Integer>> sonuclar = new ArrayList<List<Integer>>();
	private List<Integer> minimumSonuc = new ArrayList<Integer>();
	private int maliyet = Integer.MAX_VALUE;
	
	public int getMaliyet() {
		return maliyet;
	}

	public void setMaliyet(int maliyet) {
		this.maliyet = maliyet;
	}

	public void cozumEkle(List<Integer> cozum) {
		sonuclar.add(cozum);
	}
	
	public List<Integer> getCozum (int row) {
		return sonuclar.get(row);
	}
	
	public int getCozumSayisi () {
		return sonuclar.size();
	}
	
	public void setMinimumSonuc (List<Integer> minimum) {
		minimumSonuc = minimum;
	}
	
	public List<Integer> getMinumumSonuc () {
		return minimumSonuc;
	}
	
	public void cozumSil(int index) {
		sonuclar.remove(index);
	}
	
	public void tumCozumleriSil() {
		sonuclar.clear();
	}
}
