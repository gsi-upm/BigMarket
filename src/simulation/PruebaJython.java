package simulation;



import java.io.IOException;

import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PruebaJython {
	
	public static void main(String[] args) throws PyException, IOException{
//        PythonInterpreter interp = new PythonInterpreter();
//        interp.exec("import sys");
//        interp.exec("import matplotlib.pyplot");
//        interp.exec("import networkx");
//        
//        interp.exec("G=nx.cycle_graph(24)");
//        interp.exec("pos=nx.spring_layout(G,iterations=200)");
//        interp.exec("nx.draw(G,pos,node_color=range(24),node_size=800,cmap=plt.cm.Blues)");
//        interp.exec("plt.savefig('nodePrueba.png')");
//        interp.exec("plt.show()");
        
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import sys");
        interp.exec("print sys");
        interp.set("a", new PyInteger(42));
        interp.exec("print a");
        interp.exec("x = 2+2");
        PyObject x = interp.get("x");
        System.out.println("x: " + x);
        
//        Runtime r = Runtime.getRuntime();
//        Process p = r.exec("python /home/dlara/PFC/EjemplosPython/red1.py");

        
    }

}
