import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


class Vertex implements Comparable<Vertex>
{
    public final String switchDpid;
    public List<Edge> adjacencies=new ArrayList();
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String switchDpid) { this.switchDpid = switchDpid; }
    public String toString() { return ""+switchDpid; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }

}

class Edge
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex target, double weight) { 
    	this.target = target;
    	this.weight = weight;
    }
    public Edge(Vertex target) { 
    	this(target, 1.0); 
    }
}

public class Dijkstra
{
    public static void computePaths(Vertex source)
    {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);
	
		while (!vertexQueue.isEmpty()) {
		    Vertex u = vertexQueue.poll();
	
            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
				    vertexQueue.remove(v);
		
				    v.minDistance = distanceThroughU ;
				    v.previous = u;
				    vertexQueue.add(v);
				}
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }
    
    public static Vertex buscar(Vertex[] vertices,String n){
    	for(int i=0;i<vertices.length;i++){
    		if(vertices[i].toString().equals(n)){
    			return vertices[i];
    		}
    	}
    	return null;
    }
    
    public static List<Vertex> example(List<Nodo>nodos,String nombreOrigen,String nombreDestino)
    {
    	Vertex[] vertices=new Vertex[nodos.size()];
    	for(int i=0;i<nodos.size();i++){
    		Vertex v=new Vertex(nodos.get(i).nombre);
    		vertices[i]=v;
    		
    	}
    	
    	for(int i=0;i<nodos.size();i++){
    	for(Arco arc:nodos.get(i).adj){
			
			Edge e=new Edge(buscar(vertices,arc.destino.nombre),arc.costo);
			//System.out.println("Target de "+vertices[i].switchDpid+" --- "+e.target.switchDpid+" -- "+e.weight);
			vertices[i].adjacencies.add(e);
		}
    	}
    	
    	
    	Vertex vi=null;
    	Vertex vf=null;
    	for(int i=0;i<vertices.length;i++){
    		if(vertices[i].switchDpid.equals(nombreOrigen)){
    			vi=vertices[i];
    		}
    	}
    	for(int i=0;i<vertices.length;i++){
    		if(vertices[i].switchDpid.equals(nombreDestino)){
    			vf=vertices[i];
    		}
    	}
	    computePaths(vi);
	    List<Vertex> path = getShortestPathTo(vf);
	    for (Vertex v : vertices)
		{
	    	
		    List<Vertex> path3 = getShortestPathTo(v);
		    
		}
	    path.add(vf);
	    return path;
    }
}
