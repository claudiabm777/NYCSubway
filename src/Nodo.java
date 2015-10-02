import java.util.ArrayList;
import java.util.List;


public class Nodo {
	public String nombre;
	public int tiempo;
	public List<Arco>adj=new ArrayList();
	public Nodo(String nombre,int tiempo){
		this.tiempo=tiempo;
		this.nombre=nombre;
	}

}
