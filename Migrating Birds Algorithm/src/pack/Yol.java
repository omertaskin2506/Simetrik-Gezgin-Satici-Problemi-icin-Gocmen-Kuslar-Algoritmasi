package pack;

public class Yol {

	private double mesafe;
	private int hedef;
	private int kaynak;
	
	public Yol(double mesafe, int hedef, int kaynak) {
		super();
		this.mesafe = mesafe;
		this.hedef = hedef;
		this.kaynak = kaynak;
	}

	public double getMesafe() {
		return mesafe;
	}

	public int getHedef() {
		return hedef;
	}

	public int getKaynak() {
		return kaynak;
	}
	
}
