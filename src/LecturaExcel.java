
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jxl.*;
public final class LecturaExcel extends JFrame {
	
	public LecturaExcel(){
		super("Subway");
	}
	
	public final static InputStream archivoDatos1=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos2=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos3=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos4=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos5=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos6=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos7=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos8=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos9=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos10=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos11=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos12=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	public final static InputStream archivoDatos13=LecturaExcel.class.getResourceAsStream("NYC Subway(1) (2).xls");
	
	public HashMap<String, String> todasLasEstaciones= new HashMap<String,String>();
	public List<Servicio>servicios=new ArrayList<Servicio>();
	public HashMap<String, Integer> origenesDestinos=new HashMap<String,Integer>();
	public List<String> servicio1= new ArrayList<String>();
	public List<String> servicio2= new ArrayList<String>();
	public List<String> servicio3= new ArrayList<String>();
	public List<String> servicio4= new ArrayList<String>();
	public List<String> servicio5= new ArrayList<String>();
	public List<String> servicio5Diamante= new ArrayList<String>();
	public List<String> servicio6= new ArrayList<String>();
	public List<String> servicio6Diamante= new ArrayList<String>();
	public List<String> servicio7= new ArrayList<String>();
	public List<String> servicio7Expreso= new ArrayList<String>();
	public List<Nodo> nodos= new ArrayList<Nodo>();
	public List<Arco> arcos=new ArrayList<Arco>();
	
	class Bus{
		
		public String nombre;
		public HashMap<String, Integer> estacionesHoras=new HashMap<String,Integer>();
		public List<String>estaciones=new ArrayList<String>();
		
		public Bus(String nombre, HashMap<String, Integer> estacionesHoras, List<String> e){
			
			this.nombre=nombre;
			this.estacionesHoras=estacionesHoras;
			this.estaciones=e;
		}
		
	}
	
	 class Servicio{
		
		public String nombre;
		public int horaInicio;
		public double tiempoEspera;
		public String estacionInicio;
		public int numBuses;
		public boolean normal;
		public int numEstUltimoBus;
		public Bus[]buses;
		
		public Servicio(String nombre, int horaInicio,int tiempoEspera, String eInic, int ii,HashMap<String, Integer> origenesDestinos, List<String>estaciones){
			this.nombre=nombre;
			this.horaInicio=horaInicio;
			this.tiempoEspera=tiempoEspera*60;
			this.numBuses=(int)Math.ceil((60.1-(this.horaInicio/60.0))/(this.tiempoEspera/60.0));
			System.out.println(this.nombre+" - - "+this.numBuses);
			this.estacionInicio=eInic;
			this.normal= (ii<=10)?true:false;
			this.buses=new Bus[numBuses];
			if(!normal){
				List<String> estV=new ArrayList<String>();
				for(int kk=estaciones.size()-1;kk>=0;kk--){
					estV.add(estaciones.get(kk));
				}
				estaciones=estV;
			}
			int horaSalida=this.horaInicio;
			String nom;
			
			for(int j=0;j<this.numBuses;j++){
				nom=this.nombre+";"+normal+";Bus"+j;
				int c=horaSalida;
				HashMap<String, Integer> estacionesHoras=new HashMap<String,Integer>();
				
				List<String>esta=new ArrayList<String>();
				estacionesHoras.put(estaciones.get(0), c);
				esta.add( estaciones.get(0));
				for(int k=1;k<estaciones.size();k++){
					int u=origenesDestinos.get(estaciones.get(k-1)+";"+estaciones.get(k));
					//if(c+u>60*60){
						//break;
					//}
					//else{
						c+=u;
						estacionesHoras.put(estaciones.get(k), c);
						esta.add(estaciones.get(k));
					//}
				}
				Bus b=new Bus(nom,estacionesHoras,esta);
				buses[j]=b;
				horaSalida= horaSalida+(int)this.tiempoEspera;
			}	
		}
	}
	
	class OrigenDestino{
		
		public String origen;
		public String destino;
		public int segundos;
		
		public OrigenDestino(String origen, String destino,int segundos){
			this.origen=origen;
			this.destino=destino;
			this.segundos=segundos;
		}
	}
	
	
	public void crearNodos(){
		for(int i=0;i<servicios.size();i++){
			Bus[]b=servicios.get(i).buses;
			for(int j=0;j<b.length;j++){
				HashMap<String, Integer> eH=b[j].estacionesHoras;
				List<String>estaciones=b[j].estaciones;
				for(int k=0;k<estaciones.size();k++){
					String estac=estaciones.get(k);
					String n=b[j].nombre+":"+estac;
					int t=eH.get(estac.toString());
					Nodo no=new Nodo(n,t);
					nodos.add(no);
				}
			}
		}
	}
	
	public void crearArcos(){
		for(Nodo n1:nodos){
			for(Nodo n2:nodos){
				if(n1.nombre.equals(n2.nombre)){
					Arco a=new Arco(n1,n2,0);
					arcos.add(a);
					n1.adj.add(a);
				}
				else if(n1.nombre.split(":")[0].equals(n2.nombre.split(":")[0])){
					if(n1.tiempo<n2.tiempo){
						Arco a=new Arco(n1,n2,(n2.tiempo-n1.tiempo));
						
						arcos.add(a);
						n1.adj.add(a);
					}
				}
				else if(n1.nombre.split(":")[1].equals(n2.nombre.split(":")[1])){
					if((n2.tiempo-n1.tiempo)>=5*60){
						Arco a=new Arco(n1,n2,(n2.tiempo-n1.tiempo));

						arcos.add(a);
						n1.adj.add(a);
					}
				}
			}
		}
	}
	
	public String[] incorporarParametrosEntrada(String estacionOrigen,String estacionDestino,int horaSalida){
		String[]respuesta=new String[2];
		Nodo n=new Nodo("Nodo Inicial:Estacion"+estacionOrigen,horaSalida);
		
		for(Nodo n1:nodos){
			if(n1.nombre.split(":")[1].equals(estacionOrigen)){
				
				if(n1.tiempo-n.tiempo>=5*60){
					Arco a=new Arco(n,n1,n1.tiempo-n.tiempo);
					arcos.add(a);
					n.adj.add(a);
				}
			}
			
		}
		nodos.add(n);
		Arco auu=new Arco(n,n,0);
		arcos.add(auu);
		Nodo f=new Nodo("Nodo Final:Estacion"+estacionDestino,0);
		
		for(Nodo n1:nodos){
			if(n1.nombre.split(":")[1].equals(estacionDestino)||n1.nombre.split(":")[1].equals("Estacion"+estacionDestino)){
				
				Arco a=new Arco(n1,f,0);
				arcos.add(a);
				n1.adj.add(a);
			}
		}
		nodos.add(f);
		
		
		respuesta[0]=n.nombre;
		respuesta[1]=f.nombre;
		return respuesta;
	}
	public  void tiempoHastaX(Servicio servicio){
		
		double x=Math.floor((60*60-servicio.horaInicio)/servicio.tiempoEspera);
		double y=(60*60-servicio.horaInicio)/servicio.tiempoEspera;
		if(servicio.numBuses>x){
			if(servicio.normal){
				if(servicio.nombre.equals("Servicio 1")){
					int c=0;
					for(int i=0;i<servicio1.size()-1;i++){
						int u=origenesDestinos.get(servicio1.get(i)+";"+servicio1.get(i+1));
						
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
						
				}
				else if(servicio.nombre.equals("Servicio 2")){
					int c=0;
					for(int i=0;i<servicio2.size()-1;i++){
						int u=origenesDestinos.get(servicio2.get(i)+";"+servicio2.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				
				else if(servicio.nombre.equals("Servicio 3")){
					int c=0;
					for(int i=0;i<servicio3.size()-1;i++){
						int u=origenesDestinos.get(servicio3.get(i)+";"+servicio3.get(i+1));
						
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 4")){
					int c=0;
					for(int i=0;i<servicio4.size()-1;i++){
						int u=origenesDestinos.get(servicio4.get(i)+";"+servicio4.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 5")){
					int c=0;
					for(int i=0;i<servicio5.size()-1;i++){
						int u=origenesDestinos.get(servicio5.get(i)+";"+servicio5.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio diamente 5")){
					int c=0;
					for(int i=0;i<servicio5Diamante.size()-1;i++){
						int u=origenesDestinos.get(servicio5Diamante.get(i)+";"+servicio5Diamante.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 6")){
					int c=0;
					for(int i=0;i<servicio6.size()-1;i++){
						int u=origenesDestinos.get(servicio6.get(i)+";"+servicio6.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio diamante 6")){
					int c=0;
					for(int i=0;i<servicio6Diamante.size()-1;i++){
						int u=origenesDestinos.get(servicio6Diamante.get(i)+";"+servicio6Diamante.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 7")){
					int c=0;
					for(int i=0;i<servicio7.size()-1;i++){
						int u=origenesDestinos.get(servicio7.get(i)+";"+servicio7.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio expreso 7")){
					int c=0;
					for(int i=0;i<servicio7Expreso.size()-1;i++){
						int u=origenesDestinos.get(servicio7Expreso.get(i)+";"+servicio7Expreso.get(i+1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= i;
							break;
						}
						c+=u;
					}
				}
				else{
					servicio.numEstUltimoBus= -1;
				}
				
			}
			else{
				if(servicio.nombre.equals("Servicio 1")){
					int c=0;
					for(int i=servicio1.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio1.get(i)+";"+servicio1.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio1.size()-1-i;
							break;
						}
						c+=u;
					}
						
				}
				else if(servicio.nombre.equals("Servicio 2")){
					int c=0;
					for(int i=servicio2.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio2.get(i)+";"+servicio2.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio2.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 3")){
					int c=0;
					for(int i=servicio3.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio3.get(i)+";"+servicio3.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio3.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 4")){
					int c=0;
					for(int i=servicio4.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio4.get(i)+";"+servicio4.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio4.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 5")){
					int c=0;
					for(int i=servicio5.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio5.get(i)+";"+servicio5.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio5.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio diamente 5")){
					int c=0;
					for(int i=servicio5Diamante.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio5Diamante.get(i)+";"+servicio5Diamante.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio5Diamante.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 6")){
					int c=0;
					for(int i=servicio6.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio6.get(i)+";"+servicio6.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio6.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio diamante 6")){
					int c=0;
					for(int i=servicio6Diamante.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio6Diamante.get(i)+";"+servicio6Diamante.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio6Diamante.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio 7")){
					int c=0;
					for(int i=servicio7.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio7.get(i)+";"+servicio7.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							servicio.numEstUltimoBus= servicio7.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else if(servicio.nombre.equals("Servicio expreso 7")){
					int c=0;
					for(int i=servicio7Expreso.size()-1;i>0;i--){
						int u=origenesDestinos.get(servicio7Expreso.get(i)+";"+servicio7Expreso.get(i-1));
						if(c+u>(60*60-x*servicio.tiempoEspera)){
							
							servicio.numEstUltimoBus= servicio7Expreso.size()-1-i;
							break;
						}
						c+=u;
					}
				}
				else{
					servicio.numEstUltimoBus= -1;
				}
			}
			
		}
		
		else{
			servicio.numEstUltimoBus=-1;
		}
		
		
		
		
	}
	
	public void leerInfoServicios(HashMap<String, Integer> origenesDestinos){
		try{
		Workbook workbook = Workbook.getWorkbook(archivoDatos1);

		Sheet sheet = workbook.getSheet("Horarios");
		Cell a1;
		for(int i=1;i<21;i++){
			int min=Integer.parseInt(sheet.getCell(2,i).getContents().split(" ")[0].split(":")[0]);
			int seg=Integer.parseInt(sheet.getCell(2,i).getContents().split(" ")[0].split(":")[1]);
			String a=sheet.getCell(0,i).getContents();
			List<String>estaciones;
			if(a.equals("Servicio 1")){
				estaciones=servicio1;
			}
			else if(a.equals("Servicio 2")){
				estaciones=servicio2;
			}
			else if(a.equals("Servicio 3")){
				estaciones=servicio3;			
			}
			else if(a.equals("Servicio 4")){
				estaciones=servicio4;
			}
			else if(a.equals("Servicio 5")){
				estaciones=servicio5;
			}
			else if(a.equals("Servicio diamente 5")){
				estaciones=servicio5Diamante;
			}
			else if(a.equals("Servicio 6")){
				estaciones=servicio6;
			}
			else if(a.equals("Servicio diamante 6")){
				estaciones=servicio6Diamante;
			}
			else if(a.equals("Servicio 7")){
				estaciones=servicio7;
			}
			else{
				estaciones=servicio7Expreso;
			}
			Servicio s=new Servicio(sheet.getCell(0,i).getContents(),((min-7)*60*60)+seg*60,Integer.parseInt(sheet.getCell(3,i).getContents()),sheet.getCell(1,i).getContents(),i,origenesDestinos,estaciones);
			
			servicios.add(s);
		}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public void leerTiemposViaje(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos2);
			Sheet sheet = workbook.getSheet("Tiempos de viaje");
			for(int i=1;i<149;i++){
				for(int j=1;j<149;j++){
					if(!sheet.getCell(j,i).getContents().equals("")){
					String origen=sheet.getCell(0,i).getContents();
					String destino=sheet.getCell(j,0).getContents();
					int min=Integer.parseInt(sheet.getCell(j,i).getContents().split(" ")[0].split(":")[0]);
					int seg=Integer.parseInt(sheet.getCell(j,i).getContents().split(" ")[0].split(":")[1]);
					OrigenDestino o=new OrigenDestino(origen,destino,((min)*60)+seg);
					origenesDestinos.put(o.origen+";"+o.destino, o.segundos);
					}
				}	
			}
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	
	public void leerTodasLasEstaciones(){
		try{
		Workbook workbook = Workbook.getWorkbook(archivoDatos3);
		Sheet sheet = workbook.getSheet("Todas las estaciones");
		
		Cell a1 = sheet.getCell(0,1);
		Cell a2 = sheet.getCell(1,1);
		for(int i=2;i<149;i++){
			todasLasEstaciones.put(a1.getContents(), a2.getContents());
			a1 = sheet.getCell(0,i);
			a2 = sheet.getCell(1,i);
			
		}
		a1 = sheet.getCell(0,148);
		a2 = sheet.getCell(1,148);
		todasLasEstaciones.put(a1.getContents(), a2.getContents());
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public void leerServicio1(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos4);
			Sheet sheet = workbook.getSheet("Servicio 1");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<38;i++){
				servicio1.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,37);
			servicio1.add(36, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio2(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos5);
			Sheet sheet = workbook.getSheet("Servicio 2");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<45;i++){
				servicio2.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,44);
			servicio2.add(43, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio3(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos6);
			Sheet sheet = workbook.getSheet("Servicio 3");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<31;i++){
				servicio3.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,30);
			servicio3.add(29, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio4(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos7);
			Sheet sheet = workbook.getSheet("Servicio 4");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<29;i++){
				servicio4.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,28);
			servicio4.add(27, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
		
	}
	public void leerServicio5(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos8);
			Sheet sheet = workbook.getSheet("Servicio 5");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<36;i++){
				servicio5.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,35);
			servicio5.add(34, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio5Diamante(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos9);
			Sheet sheet = workbook.getSheet("Servicio 5 diamante");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<9;i++){
				servicio5Diamante.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,8);
			servicio5Diamante.add(7, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio6(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos10);
			Sheet sheet = workbook.getSheet("Servicio 6");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<28;i++){
				servicio6.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,27);
			servicio6.add(26, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio6Diamante(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos11);
			Sheet sheet = workbook.getSheet("Servicio 6 diamante");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<8;i++){
				servicio6Diamante.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,7);
			servicio6Diamante.add(6, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio7(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos13);
			Sheet sheet = workbook.getSheet("Servicio 7");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<18;i++){
				servicio7.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,17);
			servicio7.add(16, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	public void leerServicio7Expreso(){
		try{
			Workbook workbook = Workbook.getWorkbook(archivoDatos12);
			Sheet sheet = workbook.getSheet("Servicio 7 expreso");
			
			Cell a1 = sheet.getCell(1,1);
			for(int i=2;i<7;i++){
				servicio7Expreso.add(i-2, a1.getContents());
				a1 = sheet.getCell(1,i);
			}
			a1 = sheet.getCell(1,6);
			servicio7Expreso.add(5, a1.getContents());
			}catch(Throwable e){
				e.printStackTrace();
			}
	}
	
	
	
	public static void main(String[] args) {
		LecturaExcel l=new LecturaExcel();
		l.setVisible(true);
		l.leerTodasLasEstaciones();
		l.leerTiemposViaje();
		//Set<String>se=l.origenesDestinos.keySet();
		//for(String st:se){
			//int num=l.origenesDestinos.get(st);
			//l.origenesDestinos.replace(st, num-60);
		//}
		
		l.leerServicio1();
		l.leerServicio2();
		l.leerServicio3();
		l.leerServicio4();
		l.leerServicio5();
		l.leerServicio5Diamante();
		l.leerServicio6();
		l.leerServicio6Diamante();
		l.leerServicio7();
		l.leerServicio7Expreso();
		l.leerInfoServicios(l.origenesDestinos);
		l.crearNodos();
		l.crearArcos();
		String origen="";
		String destino="";
		String minutos="";
		Integer min=0;
		
		for(int i=0;i<l.servicios.get(2).buses.length;i++){
			System.out.println(l.servicios.get(2).buses[i].estacionesHoras);
		}
		
		origen = JOptionPane.showInputDialog ( "Ingrese el origen" ); 

		destino = JOptionPane.showInputDialog ( "Ingrese el destino" ); 
		minutos = JOptionPane.showInputDialog ( "Ingrese el minuto de las 7:00a las 8:00. Ej: 7:25 -> ingrese: 25" );
		try{
			min=Integer.parseInt(minutos);
			if(min<0 ||min>=60){
				throw new Exception();
			}
		}catch(Throwable e){
			JOptionPane.showMessageDialog (null, "Escriba un numero vlido dentro de la ventana de tiempo.", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
		int existen=0;
		Set<String> estaciones=l.todasLasEstaciones.keySet();
		for(String n:estaciones){
			
			if((n.equals(origen))||(n.equals(destino))){
				existen++;
			}
		}
		if(origen.equals(destino)){
			existen=2;
		}
		if(existen<2){
			JOptionPane.showMessageDialog (null, "La estacion de origen y/o destino no existen.\n Revise que digito los nombres exactamente igual que en el archivo de parmetros.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		else{
		int seg=min*60;
		String[]inFi=l.incorporarParametrosEntrada(origen, destino, seg);
		
		
		
		
		
		JFrame parentFrame = new JFrame();
		 
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");   
		 
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    
		    try {

				String content = "This is the content to write into file";

				File file = fileToSave;

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("N:[");
				bw.newLine();
				for(Nodo n:l.nodos){
					bw.write("\""+n.nombre+"\"");
					bw.newLine();
				}
				bw.write("]");
				bw.newLine();
				
				bw.write("c:[");
				bw.newLine();
				for(Arco a:l.arcos){
					bw.write("(\""+a.origen.nombre+"\",\""+a.destino.nombre+"\")"+a.costo);
					bw.newLine();
				}
				bw.write("]");
				bw.newLine();
				
				bw.write("b:[");
				bw.newLine();
				for(Nodo n:l.nodos){
					if(inFi[0].equals(n.nombre)){
						bw.write("(\""+n.nombre+"\""+")"+1);
						bw.newLine();
					}
					else if(inFi[1].equals(n.nombre)){
						bw.write("(\""+n.nombre+"\""+")"+-1);
						bw.newLine();
					}
					else{
						bw.write("(\""+n.nombre+"\""+")"+0);
						bw.newLine();
					}
				}
				bw.write("]");
				bw.close();

				JOptionPane.showMessageDialog (null, "El archivo se guardo con los parametros satisfactoriamente.", "Archivo Guardado", JOptionPane.INFORMATION_MESSAGE);

			} catch (IOException e) {
				JOptionPane.showMessageDialog (null, "Uppss! no se pudo guardar bien tu archivo, vuelve a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);

			}
		    
		    
		}
		
		
		
		
		for(Nodo n:l.nodos){
			//System.out.println(n.nombre+"--"+n.tiempo);
		}
		int hh=0;
		//for(int k=0;k<20;k++){

			//hh+=l.servicios.get(k).numBuses*l.servicios.get(k).buses.length;
			//System.out.println(l.servicios.get(k).numBuses*l.servicios.get(k).buses.length);
			//for(int i=0;i<l.servicios.get(k).buses.length;i++){
				//l.tiempoHastaX(l.servicios.get(i));
				//System.out.println(l.servicios.get(k).buses[i].nombre+" --- "+l.servicios.get(k).buses[i].estacionesHoras);
				//hh+=l.servicios.get(k).buses[i].estacionesHoras.size();
			//}
		//}
		List<String[]>respDijkstra=new ArrayList<String[]>();
		List<Vertex>path=Dijkstra.example(l.nodos,"Nodo Inicial:Estacion"+origen,"Nodo Final:Estacion"+destino);				
		
		if(path.size()>2){
			String inicioN=path.get(1).switchDpid.split(":")[0];
			String inicioE=path.get(1).switchDpid.split(":")[1];
			String inicioS=path.get(1).switchDpid.split(":")[0].split(";")[0];
			String[]respu=new String[2];
			respu[0]=inicioS;
			respu[1]=inicioE;
			respDijkstra.add(respu);
		for(int u=2;u<path.size()-1;u++){
			if(!path.get(u).switchDpid.split(":")[0].equals(inicioN)){
			
			inicioN=path.get(u).switchDpid.split(":")[0];
			inicioE=path.get(u).switchDpid.split(":")[1];
			inicioS=path.get(u).switchDpid.split(":")[0].split(";")[0];
			 
			respu=new String[2];
			respu[0]=inicioS;
			respu[1]=inicioE;
			respDijkstra.add(respu);
			}
		}
		
		String mess="El minimo tiempo de demora es "+path.get(path.size()-1).minDistance/60+" minutos. Esto es lo que debe hacer:\n 1. Tome el "+respDijkstra.get(0)[0]+" en la estacion "+respDijkstra.get(0)[1];
		for(int u=1;u<respDijkstra.size()-1;u++){
			mess+="\n "+(u+1)+". Bajese en la estacion "+respDijkstra.get(u)[1]+" y tome el servicio "+respDijkstra.get(u)[0]+".";
		}
		mess+="\n "+(respDijkstra.size())+". Bajese en la estacion "+respDijkstra.get(respDijkstra.size()-1)[1].split("Estacion")[1]+".";
		JOptionPane.showMessageDialog (null, mess, "Ruta mas corta", JOptionPane.INFORMATION_MESSAGE);
		
		}else{
			JOptionPane.showMessageDialog (null, "No existe una ruta entre estas dos estaciones \ndentro de la ventana de tiempo especificada.", "Error", JOptionPane.ERROR_MESSAGE);

		}
		}
	}
}
