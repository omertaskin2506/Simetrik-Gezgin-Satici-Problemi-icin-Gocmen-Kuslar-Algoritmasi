package pack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class MainWindow implements ActionListener{
	/**
	 * Create the application.
	 */
	private List<Point> sehirler = new ArrayList<Point>();
	private List<Point> cizdirilecek = new ArrayList<Point>();
	private List<Line> dogrular = new ArrayList<Line>();
	private List<List<Integer>> enIyıler = new ArrayList<List<Integer>>();
	private List<Yol> yollar = new ArrayList<Yol>();
	private List<Kus> kuslar = new ArrayList<Kus>();
	private JFrame frame;
	private JLabel dosyaIsmi;
	private boolean dosyaOkundumu = false;
	private JButton baslat, kusKaydet, dosyaSec, kanatCirpma;
	private JSpinner kusSpinner, kanatSpinner;
	private int kus_sayisi, kanat_cirpma_sayisi;
	
	// Burayi Kendinize Gore Degisin
	private static final String YOL = "/home/omer/Masaüstü/Şehirler/";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g){
				super.paint(g);
				
				Graphics2D grafik = (Graphics2D) g.create();
				grafik.setColor(Color.BLACK);
				grafik.setStroke(new BasicStroke(3));
				
				// Algoritma Calistiktan Sonra Ekrana En Kisa Yolun Cizilmesi
				for (Line dogru : dogrular)
					grafik.drawLine(dogru.getX1(), dogru.getY1(), dogru.getX2(), dogru.getY2());
				
				// Sehirlerin Ekrana Basilmasi (Ilk Sehir Mavi Digerleri Kirmizi)
				for (int i = 0; i < cizdirilecek.size(); i++) {
					if (i == 0) {
						grafik.setColor(Color.BLUE);
						grafik.setStroke(new BasicStroke(7));
						grafik.drawLine((int) cizdirilecek.get(i).getX(), (int) cizdirilecek.get(i).getY(), (int) cizdirilecek.get(i).getX(), (int) cizdirilecek.get(i).getY());
					} else {
						grafik.setColor(Color.RED);
						grafik.setStroke(new BasicStroke(7));
						grafik.drawLine((int) cizdirilecek.get(i).getX(), (int) cizdirilecek.get(i).getY(), (int) cizdirilecek.get(i).getX(), (int) cizdirilecek.get(i).getY());
					}
				}

				grafik.dispose();
			}
		};
		
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.CYAN);
		frame.setBounds(100, 100, 1366, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		
		// Labellerin Disindaki Cizgileri Cizmek Icin
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		
		// Kus Sayinin Girildigi Spinner
		kusSpinner = new JSpinner();
		kusSpinner.setFont(new Font("FreeSerif", Font.BOLD, 25));
		kusSpinner.setBounds(50, 74, 200, 40);
		frame.getContentPane().add(kusSpinner);
		
		// Kanat Cirpma Sayisinin Girildigi Spinner
		kanatSpinner = new JSpinner();
		kanatSpinner.setFont(new Font("FreeSerif", Font.BOLD, 25));
		kanatSpinner.setBounds(50, 286, 200, 40);
		frame.getContentPane().add(kanatSpinner);

		JComponent editor = kusSpinner.getEditor();

		if (editor instanceof JSpinner.DefaultEditor) {
			JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
			spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		}
		
		JComponent editor2 = kanatSpinner.getEditor();
		
		if (editor2 instanceof JSpinner.DefaultEditor) {
			JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor2;
			spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		}
		
		// Kuş Sayısı Yazan Label
		JLabel lblKuSays = new JLabel("Kuş Sayısı");
		lblKuSays.setFont(new Font("FreeSerif", Font.BOLD, 21));
		lblKuSays.setBorder(border);
		lblKuSays.setHorizontalAlignment(SwingConstants.CENTER);
		lblKuSays.setBounds(50, 22, 200, 40);
		frame.getContentPane().add(lblKuSays);
		
		// Kanat Cirpma Sayisini Yazan Label
		JLabel lbl = new JLabel("Kanat Çırpma Sayısı");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setBorder(border);
		lbl.setFont(new Font("FreeSerif", Font.BOLD, 21));
		lbl.setBounds(50, 234, 200, 40);
		frame.getContentPane().add(lbl);
		
		// Dosya Adini Yazan 
		dosyaIsmi = new JLabel("Dosya Seçilmedi");
		dosyaIsmi.setBorder(border);
		dosyaIsmi.setFont(new Font("FreeSerif", Font.BOLD, 21));
		dosyaIsmi.setHorizontalAlignment(SwingConstants.CENTER);
		dosyaIsmi.setBounds(50, 564, 200, 40);
		frame.getContentPane().add(dosyaIsmi);
		
		// Algoritmayi Calistiran Button
		baslat = new JButton("<html><center>Algoritmayı<br/>Çalıştır</center></html>");
		baslat.setFont(new Font("FreeSerif", Font.BOLD, 21));
		baslat.setBounds(50, 616, 200, 100);
		baslat.addActionListener(this);
		frame.getContentPane().add(baslat);
		
		// Kus Sayisini Alan Button
		kusKaydet = new JButton("<html><center>Kuş Sayısını<br/> Kaydet</center></html>\n");
		kusKaydet.setFont(new Font("FreeSerif", Font.BOLD, 21));
		kusKaydet.setBounds(50, 126, 200, 100);
		kusKaydet.addActionListener(this);
		frame.getContentPane().add(kusKaydet);
		
		// Dosya Sectiren Button
		dosyaSec = new JButton("Dosya Seç");
		dosyaSec.setFont(new Font("FreeSerif", Font.BOLD, 21));
		dosyaSec.setBounds(50, 452, 200, 100);
		dosyaSec.addActionListener(this);
		frame.getContentPane().add(dosyaSec);
		
		// Kanat Cirpma Sayisini 
		kanatCirpma = new JButton("<html><center>Kanat Çırpma <br/> Sayısını Kaydet</center></html>\n");
		kanatCirpma.setFont(new Font("FreeSerif", Font.BOLD, 20));
		kanatCirpma.setBounds(50, 340, 200, 100);
		kanatCirpma.addActionListener(this);
		frame.getContentPane().add(kanatCirpma);

	}
	
	
	// ***************** EKRANIN TEMIZLEYEN FONKSIYON *****************
	
	private void ekraniTemizle() {

		// Tum Sehirleri Silme
		sehirler.clear();
		
		// Tum Olceklendirilip Cizilmis Sehirleri Silme
		cizdirilecek.clear();
		
		// Ekrandaki Cizilen Yollar Silinir
		dogrular.clear();
		
		// Tum Yollar Silinir.
		yollar.clear();
		
		// repaintle Ekrandaki Yollar Temizlenir
		frame.repaint();
	}

	// ***************** TUM BUTONLARIN TIKLANMASIYLA CALISAN FONKSIYON *****************
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// KUS SAYISINI ALMA
		if(e.getSource() == kusKaydet){
			
			if (((int) kusSpinner.getValue()) % 2 == 0  || ((int) kusSpinner.getValue()) < 3) {
				// Kus Sayisi 3'ten Kucuk veya Cift ise Hata Veriyorum.
				mesajYazdir("Hata!", "Kus Sayisi 2'den Buyuk Tek\nSayı Olmalıdır. ");
				return;
			} else {
				kus_sayisi = ((int) kusSpinner.getValue());
				mesajYazdir("Başarılı!", "Kus Sayısı Kaydedildi.");
			}
			
		}
		
		// DOSYA SECME BOLUMU
		else if(e.getSource() == dosyaSec){
			ekraniTemizle();
			
			FileDialog dialog = new FileDialog(frame, "Dosya Seçiniz", FileDialog.LOAD);
			dialog.setDirectory(YOL);
			dialog.setVisible(true);
			String dosyaYolu = dialog.getFile();
			if(dosyaYolu != null) {
				dosyaIsmi.setText(dosyaYolu);	
				dosyaOku(dosyaYolu);
			} else {
				dosyaIsmi.setText("Dosya Seçilmedi");
			}
			
		}
		
		// ALGORITMAYI BASLATMA
		else if(e.getSource() == baslat){
			if (kanat_cirpma_sayisi > 0 && kus_sayisi >= 3 && dosyaOkundumu) {
				// Parametreler Giridi Ise Algoritmayi Calistiriliyor.
				enIyıler.clear();
				kuslar.clear();
				dogrular.clear();
				migratingBird();
			} else {
				mesajYazdir("HATA!", "Tum Parametreleri Giriniz");
			}
		}
		
		// KANAT ÇIRPMA SAYISINI ALMA
		else if(e.getSource() == kanatCirpma) {
			kanat_cirpma_sayisi = (int) kanatSpinner.getValue();
			mesajYazdir("Başarılı!", "Kanat Çırpma Sayısı Kaydedildi.");
		}
		
	}
	
	// ***************** DOSYANIN OKUNDUGU FONKSIYON *****************
	
	private void dosyaOku(String dosyaIsim) {
	
		//List<Point> gecici = new ArrayList<Point>();
		
		// Okunacak dosyanin yolunu veriyorum.
		File dosya = new File(YOL + dosyaIsim);
		
		FileReader rd = null;
		try {
			rd = new FileReader(dosya);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Satir satir okumak icin Buffered Reader Kullaniyorum. Yoksa FileReader ilede okuma yapilabilir.
		BufferedReader oku = new BufferedReader(rd);
		String line;
		
		try {
			// Burasi Dosyadan Okuma Kismi 
			boolean okuyayimmi = false;
			
			while ((line = oku.readLine()) != null) {
		
				if(line.contains("NODE_COORD_SECTION")){
					okuyayimmi = true;
				}
				// Dosyanin sonu
				else if(line.equals("EOF")){
					break;
				}
				// Burada Sehirlerin Noktalarini Aliyorum.
				else if(okuyayimmi){
					String[] temp = line.split(" ");
					Point p = new Point();
					p.setLocation(Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
					sehirler.add(p);
				}
			}
			frame.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			// Okunan Dosyayi Kapatiyorum.
			oku.close();
			rd.close();
			dosyaOkundumu = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Sehirlerin Çizdirilmek İcin Boyulandirildigi Fonksiyon
		sehirleriOlcekle();
		
		// Yollari Olusturma Fonksiyonu
		yollariOlustur();
			
	}
	
	/* ***************** SEHIRLERIN CIZDIRILMEDEN ONCE OLCEKLENDIRILDIGI FONKSIYON *****************
	 * SEHIRLERIN ASIL KOORDINATLARIYLA YOL HESAPLAMLARI YAPILIYOR. SADECE SEHIRLER BIRBIRINE COK
	 * YAKIN DIYE EKRANDA GOSTERIRKEN BIRAZ OLCEKLENDIRIYORUM. */
	
	private void sehirleriOlcekle() {
		
		double maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		// Sehirleri 300-1300 X koordinatlari ve 200-700 Y koordinatlarina arasina cizdiriyorum.
		double ekranMaxX = 1300, ekranMinX = 300, ekranMaxY = 700, ekranMinY = 200, xArtis, yArtis;
		
		// minX, maxX ve minY, maxY degerlerini buluyorum.
		for (Point sehir : sehirler) {
			
			if (sehir.getX() > maxX)
				maxX = sehir.getX();
			else if (sehir.getX() < minX)
				minX = sehir.getX();
			
			if (sehir.getY() > maxY)
				maxY = sehir.getY();
			else if (sehir.getY() < minY)
				minY = sehir.getY();
			
		}
		
		// X ve Y Ekseninde Artis Miktarini Buluyorum.
		xArtis = (ekranMaxX - ekranMinX) / (maxX - minX);
		yArtis = (ekranMaxY - ekranMinY) / (maxY - minY);
				
		// Ekrana Cizdirmek Icin Olceklendirilmis Noktalari Bulup List'e Ekliyorum.
		for (Point sehir : sehirler) {
			
			int yeniX = (int) ((sehir.getX() - minX) * xArtis) + 300;
			int yeniY = (int) ((sehir.getY() - minY) * yArtis) + 200;
			
			Point s = new Point(yeniX, yeniY);
			
			cizdirilecek.add(s);
		}
		
		// Olcekli Sehirlerin Cizdirilmesi icin repaint fonksiyonunu kullaniyorum. 103 ve 104. Satirlarda da 
		// Sehirleri Cizdiriyorum.
		frame.repaint();
	}

	// ***************** SEHIRLER ARASI OLLARIN OLUSTURULDUGU FONKSIYON *****************
	
	private void yollariOlustur() {
		
		for (int i = 0; i < sehirler.size() - 1; i++) {
			
			for (int j = i + 1; j < sehirler.size(); j++) {
				
				if (i != j) {
					// Oklid Uzakliklari Alinip Yollari Olusturuyorum.
					int x1 = (int) sehirler.get(i).getX();
					int y1 = (int) sehirler.get(i).getY();
					int x2 = (int) sehirler.get(j).getX();
					int y2 = (int) sehirler.get(j).getY();
					int x = (x2 - x1) * (x2 - x1);
					int y = (y2 - y1) * (y2 - y1);
					int mesafe = (int) Math.sqrt(x + y);
					
					String mesaj = "Koordinatlar\n x1 : " + x1 + "   y1 : " + y1 + "\n x2 : " 
								   + x2 + "   y2 : " + y2 + "\n (x2 - x1)^2 : " + x + "\n (y2 - y1)^2 : " + y + "\n mesafe : " + mesafe; 
					
					//mesajYazdir("Deneme", mesaj);
					
					// Simetrik Problem Oldugu Icin A'dan B'ye ve B'den A'ya Olan Mesafeye Esit Olur.
					Yol yol1 = new Yol(mesafe, j, i);
					Yol yol2 = new Yol(mesafe, i, j);
					
					yollar.add(yol1);
					yollar.add(yol2);
				}
				
			}
			
		}
		
	}

	// ***************** ALGORITMANIN CALISTIGI FONKSIYON *****************
	
	public void migratingBird(){
		
		// Kuslari Olusturuyorum.
		kusOlustur();
		
		// cevrim_sayisi Sehir Sayisinin Kupu Olur.
		int cevrim_sayisi = (sehirler.size())^3;
		
		// Ilk Kusun Olusturacagi Cozum Sayisini 3 Olarak Belirtiyorum.
		int cozum_sayisi = 3;
		
		/* Paylasim Sayisi Cozum Sayisinin Yarisi Kadar Olmalidir. Yani 3 Cozum Uretti ise
		 * Ilk Kus 1 Tanesini Kendisine Alacak Geriye Kalan 2 Cozumu Diger Kuslara Verecek. */
		int paylasim_sayisi = cozum_sayisi / 2;
		int minimum_index;
		
		for (int i = 0; i < cozum_sayisi; i++)
			kuslar.get(0).cozumEkle(cozumUret());
		
		minimum_index = minimumMaliyetliYoluBul(kuslar.get(0));
		
		enIyıler.add(kuslar.get(0).getCozum(minimum_index));
		
		// Rezerve Edilen Cozumu Siliyorum.
		kuslar.get(0).cozumSil(minimum_index);
		
		// Lider Kus Rezerve Disindaki Tum Cozumlerini Dagitacak
		for (int k = 0; k < cozum_sayisi - 1; k++) {
			if (k % 2 == 0)
				kuslar.get(1).cozumEkle(kuslar.get(0).getCozum(k));
			else
				kuslar.get(2).cozumEkle(kuslar.get(0).getCozum(k));
		}
		
		// Paylastirma Isleminden Sonra Tum Cozumleri Siliyorum.
		kuslar.get(0).tumCozumleriSil();
		
		for (int index = 1; index < kuslar.size(); index++) {
			// Diger Kuslarin Cozum Uretmesi
			for (int k = 0; k < cozum_sayisi - paylasim_sayisi; k++)
				kuslar.get(index).cozumEkle(cozumUret());
			
			// Diger Kuslarin Minimum Sonuclarini Secmesi
			minimum_index = minimumMaliyetliYoluBul(kuslar.get(index));
			
			enIyıler.add(kuslar.get(index).getCozum(minimum_index));
			
			// Rezerve Edilen Cozumu Siliyorum.
			kuslar.get(index).cozumSil(minimum_index);
			
			/* Egerki Son Kuslar Degilse Bir Arkasindaki Kusa 
			 * Paylasim Sayisi Kadar Cozumu Aktariyorum.*/
			if ((index + 2) < kuslar.size())
				for (int k = 0; k < paylasim_sayisi; k++)
					kuslar.get(index + 2).cozumEkle(kuslar.get(index).getCozum(k));
			
			// Paylastirma Isleminden Sonra Tum Cozumleri Siliyorum.
			kuslar.get(index).tumCozumleriSil();
		}
		
		for (int i = 0; i < cevrim_sayisi; i++) {
			
			for (int j = 0; j < kanat_cirpma_sayisi; j++) {
				
				int index = i % kus_sayisi;
				
				if (index == 0) {
					// Lider Kusun Cozum Uretmesi
					for (int k = 0; k < cozum_sayisi; k++)
						kuslar.get(index).cozumEkle(cozumUret());
					
					//Lider Kusun Minimum Sonucu Uretmesi
					minimum_index = minimumMaliyetliYoluBul(kuslar.get(index));
					
					// Egerki Yeni Minimum Sonuc Rezerve Edilen Sonucdan Daha Kucukse Bu Degeri Rezerve Ediyorum.
					
					int eniyiMaliyet = maliyetHesapla(enIyıler.get(index));
					int simdiki = maliyetHesapla(kuslar.get(index).getCozum(minimum_index));
					
					if (eniyiMaliyet > simdiki) {
						enIyıler.add(index, kuslar.get(index).getCozum(minimum_index));
						enIyıler.remove(index + 1);
						
						String mesaj = String.valueOf(eniyiMaliyet) + " > " + String.valueOf(simdiki);
						//mesajYazdir("asd", mesaj);
					}
					
					// Rezerve Edilen Cozumu Siliyorum.
					kuslar.get(index).cozumSil(minimum_index);
					
					// Lider Kus Rezerve Disindaki Tum Cozumlerini Dagitacak
					for (int k = 0; k < cozum_sayisi - 1; k++) {
						if (k % 2 == 0)
							kuslar.get(1).cozumEkle(kuslar.get(index).getCozum(k));
						else
							kuslar.get(2).cozumEkle(kuslar.get(index).getCozum(k));
					}
					
					// Paylastirma Isleminden Sonra Tum Cozumleri Siliyorum.
					kuslar.get(index).tumCozumleriSil();
					
				} else {
					// Diger Kuslarin Cozum Uretmesi
					for (int k = 0; k < cozum_sayisi - paylasim_sayisi; k++)
						kuslar.get(index).cozumEkle(cozumUret());
					
					// Diger Kuslarin Minimum Sonuclarini Secmesi
					minimum_index = minimumMaliyetliYoluBul(kuslar.get(index));
					
					// Egerki Yeni Minimum Sonuc Rezerve Edilen Sonucdan Daha Kucukse Bu Degeri Rezerve Ediyorum.
					int eniyiMaliyet = maliyetHesapla(enIyıler.get(index));
					int simdiki = maliyetHesapla(kuslar.get(index).getCozum(minimum_index));
					
					if (eniyiMaliyet > simdiki) {
						enIyıler.add(index, kuslar.get(index).getCozum(minimum_index));
						enIyıler.remove(index + 1);
					}
					
					// Rezerve Edilen Cozumu Siliyorum.
					kuslar.get(index).cozumSil(minimum_index);
					
					/* Egerki Son Kuslar Degilse Bir Arkasindaki Kusa 
					 * Paylasim Sayisi Kadar Cozumu Aktariyorum.*/
					if ((index + 2) < kuslar.size())
						for (int k = 0; k < paylasim_sayisi; k++)
							kuslar.get(index + 2).cozumEkle(kuslar.get(index).getCozum(k));
					
					// Paylastirma Isleminden Sonra Tum Cozumleri Siliyorum.
					kuslar.get(index).tumCozumleriSil();
				}
				
			}
			
			/* Kanat Cirpmasi Sayisi Biten Lider Kus En Arkaya Geciyor. 
			 * Bir Sonraki Kusta Lider Kus Oluyor.*/
			kuslar.add(kuslar.get(0));
			kuslar.remove(0);
		}
		
		enIyıSonucuBul();
		
	}
	
	// ***************** GLOBAL EN IYI SONUCU BULAN FONKSIYON *****************

	private void enIyıSonucuBul() {
		int minimum_index = 0;
		int minimum_maliyet = Integer.MAX_VALUE;
		
		for (int i = 0; i < enIyıler.size(); i++) {
			int maliyet = maliyetHesapla(enIyıler.get(i));
			
			if (minimum_maliyet > maliyet) {
				minimum_maliyet = maliyet;
				minimum_index = i;
			}
		}
		
		mesajYazdir("ads", String.valueOf(minimum_maliyet));
		
		yoluCiz(enIyıler.get(minimum_index));
	}

	// ***************** EN KISA YOLU BULAN 
	
	private void yoluCiz(List<Integer> list) {

		for (int i = 0; i < list.size() - 1; i++) {
			
			int x1 = (int) cizdirilecek.get(list.get(i)).getX(); 
			int x2 = (int) cizdirilecek.get(list.get(i + 1)).getX(); 
			int y1 = (int) cizdirilecek.get(list.get(i)).getY(); 
			int y2 = (int) cizdirilecek.get(list.get(i + 1)).getY(); 
			
			Line yol  = new Line(x1, y1, x2, y2);
			
			dogrular.add(yol);
		}
		
		frame.repaint();
	}

	// ***************** KUSLARIN URETTIGI COZUMLER ARASINDAKI MININUM MALIYETLI OLANI BULAN FONKSIYON *****************
	
	private int minimumMaliyetliYoluBul(Kus kus) {
		int minimum = Integer.MAX_VALUE;
		int gecici  = 0;
		int minimumCozum = 0;
		
		for (int i = 0; i < kus.getCozumSayisi(); i++) {
			
			List<Integer> cozum = kus.getCozum(i);
			gecici = 0;
			
			gecici = maliyetHesapla(cozum);
			
			if (minimum > gecici) {
				minimum = gecici;
				minimumCozum = i;
			}
			
		}
		
		return minimumCozum;
	}

	// ***************** MALIYET HESAPLAMA FONKSIYONU *****************
	
	public int maliyetHesapla (List<Integer> cozum) {
		int maliyet = 0;
		
		for (int i = 0; i < cozum.size() - 1; i++)  {
			int yol = yoluBul(cozum.get(i), cozum.get(i + 1));
			int onceki = maliyet;
			maliyet += yol;
			
			String mesaj = String.valueOf(maliyet) + " = " + String.valueOf(onceki) + " + " + String.valueOf(yol);
			//mesajYazdir("asd", mesaj);
		}
			
		return maliyet;
	}
	
	// ***************** 2 SEHIR ARASINDAKI MESAFEYI DONDUREN FONKSIYON *****************
	
	private int yoluBul(int kaynak, int hedef) {
		
		for (Yol yol : yollar) {
			// Egerki Aranan Yol Bulundu Ise Bu Yolun Mesafesini Yolluyorum.
			if (yol.getKaynak() == kaynak && yol.getHedef() == hedef)
				return (int) yol.getMesafe();
		}
		
		return Integer.MAX_VALUE;
	}

	// ***************** COZUMLERIN URETILDIGI FONKSIYON *****************
	
	private List<Integer> cozumUret() {
		
		boolean[] secilenler = new boolean[sehirler.size()];
		List<Integer> gecici = new ArrayList<Integer>();
		Random rnd = new Random();
		
		for (int i = 0; i < secilenler.length; i++)
			secilenler[i] = false;
		
		gecici.add(0);
		secilenler[0] = true;
		
		while (true) {
			
			// Random Indis Seciyorum.
			int index = rnd.nextInt(sehirler.size());
			
			if (!secilenler[index]) {
				// Egerki Sehir Secilmedi ise O Sehri Seciyorum.
				gecici.add(index);
				secilenler[index] = true;
			} else {
				// EGERKI BELIRTILEN INDISTEKI SEHIR ONCEDEN SECILDIYSE YENISINI BULMA KISMI
				// Tum Sehirler Secildimi Secilmedimi Icin Olusturdugum Kontrol Degiskeni
				boolean hepsiSecildimi = true;
				
				for (int j = 0; j < secilenler.length; j++) {
					if (!secilenler[j]) {
						hepsiSecildimi = false;
						break;
					}
				}
				
				// Egerki Tum Sehirler Secildi ise Cikiyorum.
				if (hepsiSecildimi)
					break;
					
				while (true) {
					// Egerki Tum Sehirler Secilmedi ise Secilmeyen Bir Sehir Bulana Kadar Indexi Artiriyorum.
					index++;
					
					if (!secilenler[index % sehirler.size()]) {
						
						gecici.add(index % sehirler.size());
						secilenler[index % sehirler.size()] = true;
						break;
						
					}
					
				}
				
			}
			
		}
		gecici.add(0);
		
		return insertion(gecici);
	}
	
	// ***************** INSERTION ILE YOLLARIN DEGISTIRILMESI *****************
	
	private List<Integer> insertion(List<Integer> gecici) {
		Random r = new Random();
		
		int nereden = r.nextInt(gecici.size() - 2) + 1;
		int nereye = r.nextInt(gecici.size() - 2) + 1;
		
		if (nereden > nereye) {
			gecici.add(nereye, gecici.get(nereden));
			gecici.remove(nereden + 1);		
		} else {
			gecici.add(nereye, gecici.get(nereden));
			gecici.remove(nereden);
		}
		
		String msg = "";
		
		for (int i = 0; i < gecici.size(); i++)
			msg += String.valueOf(gecici.get(i)) + ",";
		
		//mesajYazdir("asdfasd", msg);
		
		return gecici;
	}

	// ***************** KUSLARIN OLUSTURULDUGU FONKSIYON *****************
	
	private void kusOlustur() {
		for (int i = 0; i < kus_sayisi; i++) {
			// Yeni Kus Olusturup ArrayList'e Ekliyorum.
			Kus k = new Kus();
			kuslar.add(k); 
		}
		return;
	}
	
	// ***************** MESSAGEBOX ICIN FONKSIYON *****************
	
	public void mesajYazdir(String baslik, String mesaj) {
		JOptionPane.showMessageDialog(null, mesaj, baslik, JOptionPane.INFORMATION_MESSAGE);
		return;
	}
	
}