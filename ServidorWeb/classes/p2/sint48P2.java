package p2;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.InetAddress;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import org.xml.sax.SAXException;
import java.util.Collections;
import java.util.ArrayList;



public class sint48P2 extends HttpServlet{
   
    private final static String FICHERO_INICIAL = "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8"); 
        String datos = "/fichero.xml";
        String url = request.getRequestURL().toString(); //obtiene la url del servlet
        String queryString = request.getQueryString();  //obtiene la cadena de consulta
        String fullurl = url + (queryString != null ? "?" + queryString: ""); //combina la url y la cadena
        PrintWriter out = response.getWriter();
        
        
    
       
        //Primera fase, se muestra la fase 0, no hay parametro de fase.
        
        if(fullurl.endsWith("/P2Lib")) {

        
        String ipcliente = request.getRemoteAddr(); // para obtener la ip del cliente
        String navegador = request.getHeader("User-Agent"); //para obtener el navegador del cliente
        String nombreservidor = request.getServerName();
        InetAddress serverAddress = InetAddress.getByName(nombreservidor);       
        String ipservidor = serverAddress.getHostAddress(); //para obtener la ip del servidor
        //para mostrar el fichero libreria xml
        String[] partes = datos.split("/");

        // Obtener el último fragmento después del último '/'
        String ultimoFragmento = partes[partes.length - 1];
       

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Servicio de consulta de informaci&oacuten</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servicio de consulta de informaci&oacuten</h1>");
        out.println("<h3>Fichero procesado:"+ultimoFragmento+"</h3>");                       
        out.println("<h3>IP del cliente:" +ipcliente+"</h3>");
        out.println("<h3>Navegador del cliente:"+navegador+"</h3>");
        out.println("<h3>IP del servidor:"+ipservidor+"</h3>");
        out.println("<h4><a href=\"P2LIB?fase=1\"><ul><li type=\"circle\">Avanza.</li></ul></a></h4>");
        out.println("<hr> </hr>");
        out.println("<h4>Autor: Alejandro Comesa&ntildea Almui&ntildea</h4>");
        out.println("</body>");
        out.println("</html>");
        
            
            }

        //si estamos en la fase 1, mostramos la pantalla necesaria

        if(fullurl.contains("fase=1")){ 
        
        try{
        //llamada al ArrayList countries
        ArrayList<Country> countries = new ArrayList<Country>(DataModel.getCountries());

        FrontEnd.fase1(out, countries);
        
    }   catch (XPathExpressionException e){
        e.printStackTrace();

    }catch (SAXException e){
        e.printStackTrace();

    }catch (ParserConfigurationException e){
        e.printStackTrace();
    }

        

        }
        if(fullurl.contains("fase=2")){ //fase2, se va después de seleccionar un país en la fase 1
            try{
                
                
                //obtenemos el pais que estamos viendo y los pasamos para hallar el id
                String pcountry = request.getParameter("pais");
                
                ArrayList<Country> countries = new ArrayList<Country>(DataModel.getCountries());
                int countriestam = countries.size();
                for (int i = 0; i < countriestam; i++ ){
                    if(countries.get(i).getnombre().equals(pcountry)){
                //una vez sabemos el id lo pasamos para ver los autores de ese pais
                String dev = countries.get(i).getIdentificador(); 
                //llamada al ArrayList de autores
                ArrayList<Author> autores = new ArrayList<Author>(DataModel.getAuthors(dev));
                
                
                //se muestra la fase dos, se pasan el listado de  autores
                FrontEnd.fase2(out, pcountry, autores);
                }
            }
                
                
            }   catch (XPathExpressionException e){
                e.printStackTrace();
        
            }catch (SAXException e){
                e.printStackTrace();
        
            }catch (ParserConfigurationException e){
                e.printStackTrace();
            }
        }
        
        if(fullurl.contains("fase=3")){ 
           
            try{
                
                
                //obtenemos el pais que estamos viendo y los pasamos para hallar el id
                String autornombre = request.getParameter("autor");
                ArrayList<Country> paises = new ArrayList<Country>(DataModel.getCountries());
                int countriestam = paises.size();
                for (int i = 0; i < countriestam; i++ ){

                    ArrayList<Author> autores = new ArrayList<Author>(DataModel.getAuthors(paises.get(i).getIdentificador()));
                    int autorestam= autores.size();
                    for (int j = 0; j <autorestam; j ++){
                    if(autores.get(j).getnombre().equals(autornombre)){

                        //llamada al ArrayList de libros
                    ArrayList<Book> Books = new ArrayList<Book>(DataModel.getBooks(autores.get(j).getIdentificador()));
                     //le pasamos el id del autor
                    Country nuevopais = DataModel.getCountry(autores.get(j).getPais());
                    String pais = nuevopais.getnombre();
 
                    //se muestra la fase dos, se pasan el listado de  autores
                    FrontEnd.fase3(out, pais, autornombre,Books);

                    }
                }
            }

                
            }   catch (XPathExpressionException e){
                e.printStackTrace();
        
            }catch (SAXException e){
                e.printStackTrace();
        
            }catch (ParserConfigurationException e){
                e.printStackTrace();
            }
        }


        
        }
    
    


//Este es el DATA model, hay que ponerlo fuera si o si
public static class DataModel {
    

    //Metodos para consulta 1
    
    //Método que organiza los países del ArrayList en orden alfabético inverso. /// tiene que ser orden alfabetico normal
    public static ArrayList<Country> getCountries() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException { //hay que hacer el constructor de estp
	
    
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    String url =  "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    Document doc = db.parse(url);	
    //Lista en la que se van a guardar los paises ya ordenados.
	ArrayList<Country> paises = new ArrayList<Country>();

	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	NodeList nlpais;
    Node n2;
	Node raiz = doc.getDocumentElement();
    
    NodeList n1= raiz.getChildNodes();
    for (int x=0; x < n1.getLength(); x++) {
        n2 = n1.item(x);
        String pais = n2.getNodeName();
        String enviar = n2.getTextContent();
        
        if(pais.equals("pais")){
         //Añadimos el país al HashSet de forma que, si alguno aparece duplicado, no se guarde.
         Element elemento =(Element) n2;
        String identificador = elemento.getAttribute("identificador");
        Country country =  new Country (enviar, identificador);
        paises.add(country);

        }
         
    
}
	
		
	//Ordenamos los países por orden alfabético (por defecto).
	Collections.sort(paises);
    return paises;
}//fin get countries


//Metodos para consulta 2

public static ArrayList<Author> getAuthors(String countryId) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    String url =  "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    Document doc = db.parse(url);

    //Lista en la que se van a guardar los albums ya ordenados.
	ArrayList<Author> albumsOrdenados = new ArrayList<Author>();

    //Creamos un mapa para almacenar los autores.
	HashSet<Author> listado = new HashSet<Author>();
	
	XPath xpath = XPathFactory.newInstance().newXPath();

	
    Node n2;
    String cad;
	Node raiz = doc.getDocumentElement();
    
    NodeList n1= raiz.getChildNodes();
    for (int x=0; x < n1.getLength(); x++) {
        n2 = n1.item(x);
        if(n2.getNodeType()==Node.ELEMENT_NODE){

            Element elemento =(Element) n2;
            String atributo  = elemento.getAttribute("pais");

            if(atributo.equals(countryId)){
                
                String enviar = n2.getTextContent();
                String identificador = elemento.getAttribute("identificador"); 
                String nacimiento= elemento.getAttribute("nacimiento");
                String pais = elemento.getAttribute("pais");

                //Añadimos el país al HashSet de forma que, si alguno aparece duplicado, no se guarde.
                Author autor = new Author(enviar,identificador, nacimiento, pais);
                listado.add(autor);
            }
            
        }
    
         
    
}
	
	//Almacenamos todos los países en el ArrayList.
	albumsOrdenados.addAll(listado);	
	//Ordenamos los países por orden alfabético.
	Collections.sort(albumsOrdenados);
    return albumsOrdenados;


} //fin get albums

//metodo consulta fase 3

public static ArrayList<Book> getBooks(String authorId) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    String url =  "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    Document doc = db.parse(url);

    //Lista en la que se van a guardar los albums ya ordenados.
	ArrayList<Book> librosOrdenados = new ArrayList<Book>();

    //Creamos un mapa para almacenar los autores.
	HashSet<Book> listado = new HashSet<Book>();
	
	XPath xpath = XPathFactory.newInstance().newXPath();

	
    Node n2;
    String cad;
	Node raiz = doc.getDocumentElement();
    NodeList n1= raiz.getChildNodes();

    for (int x=0; x < n1.getLength(); x++) {
        n2 = n1.item(x);
        if(n2.getNodeType()==Node.ELEMENT_NODE){

            Element elemento =(Element) n2;
            String atributo  = elemento.getAttribute("autor");

            if(atributo.equals(authorId)){
                
                String enviar = n2.getTextContent();
                String identificador = elemento.getAttribute("identificador"); 
                String ISBN= elemento.getAttribute("ISBN");
                String autor = elemento.getAttribute("autor");
                String disponible = elemento.getAttribute("disponible");
                //Añadimos el país al HashSet de forma que, si alguno aparece duplicado, no se guarde.
                Book libro = new Book (enviar,identificador, ISBN, autor, disponible );
                listado.add(libro);
            }
            
        }
     
}
	
	//Almacenamos todos los países en el ArrayList.
	librosOrdenados.addAll(listado);	
	//Ordenamos los países por orden alfabético.
	Collections.sort(librosOrdenados);
    return librosOrdenados;


}//fint get books


//se le pasa el id del pais y te devuelve el nombre

public static Country getCountry(String countryId)throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{ 

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    String url =  "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    Document doc = db.parse(url);	
    //Lista en la que se van a guardar los paises ya ordenados.
	ArrayList<String> paisesOrdenados = new ArrayList<String>();

	//Creamos un mapa para almacenar los países.
	HashSet<String> listado = new HashSet<String>();
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	String atributo = "";
    String enviar ="";
	NodeList nlpais;
    Node n2;
	Node raiz = doc.getDocumentElement();
    
    NodeList n1= raiz.getChildNodes();
    for (int x=0; x < n1.getLength(); x++) {
        n2 = n1.item(x);
        String pais = n2.getNodeName();
        
        
        if(pais.equals("pais")){
        if(n2.getNodeType()==Node.ELEMENT_NODE){
        Element elemento =(Element) n2;
        atributo  = elemento.getAttribute("identificador");

        if(atributo.equals(countryId)){
         enviar = n2.getTextContent();

        }
    }
}   
    
}
Country country = new Country(enviar, null);



return country;

} //fin get 1 country

public static Author getAuthor(String authorId)throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{ //en principio, se le pasa el nombre de un pais en un string, y devuelve un string con el identificador de ese país.

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    String url =  "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    Document doc = db.parse(url);	
    //Lista en la que se van a guardar los paises ya ordenados.
	ArrayList<Author> paisesOrdenados = new ArrayList<Author>();

	//Creamos un mapa para almacenar los países.
	HashSet<Author> listado = new HashSet<Author>();
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	String atributo = "";
    String enviar = "";
    String nac="";
    String pais ="";
	NodeList nlpais;
    Node n2;
	Node raiz = doc.getDocumentElement();
    
    NodeList n1= raiz.getChildNodes();
    for (int x=0; x < n1.getLength(); x++) {
        n2 = n1.item(x);
        String autor = n2.getNodeName();
        
        
        if(autor.equals("autor")){
        if(n2.getNodeType()==Node.ELEMENT_NODE){
        Element elemento =(Element) n2;
        atributo  = elemento.getAttribute("identificador");

        if(atributo.equals(authorId)){
         enviar = n2.getTextContent();
         nac = elemento.getAttribute("nacimiento");
         pais = elemento.getAttribute("pais");

        }
    }
}   
    
}
Author autor = new Author(enviar, atributo , nac, pais);


return autor;

} //fin get 1 autor

public static Book getBook (String bookId)throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{ //en principio, se le pasa el nombre de un pais en un string, y devuelve un string con el identificador de ese país.

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    String url =  "https://manolo.webs.uvigo.gal/SINT/libreria.xml";
    Document doc = db.parse(url);	
    //Lista en la que se van a guardar los paises ya ordenados.
	ArrayList<Book> paisesOrdenados = new ArrayList<Book>();

	//Creamos un mapa para almacenar los países.
	HashSet<Book> listado = new HashSet<Book>();
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	String atributo = "";
    String enviar = "";
    String identificador= "";
    String ISBN = "";
    String autor = "";
    String disponible = "";
	NodeList nlpais;
    Node n2;
	Node raiz = doc.getDocumentElement();
    NodeList n1= raiz.getChildNodes();

    for (int x=0; x < n1.getLength(); x++) {
        n2 = n1.item(x);
        String libro = n2.getNodeName();
        
        
        if(libro.equals("libro")){
        if(n2.getNodeType()==Node.ELEMENT_NODE){
        Element elemento =(Element) n2;
        atributo  = elemento.getAttribute("identificador");

        if(atributo.equals(bookId)){
         enviar = n2.getTextContent();
         identificador = elemento.getAttribute("identificador"); 
         ISBN= elemento.getAttribute("ISBN");
         autor = elemento.getAttribute("autor");
         disponible = elemento.getAttribute("disponible");
                

        }
    }
}   
    
}
Book libro = new Book(enviar, identificador, ISBN ,autor, disponible);


return libro;

} //fin get 1 book
}//fin de DataModel

//clases country, book y autor

public static class Country implements Comparable<Country>{

    //Atributos de la clase.
    private String nombre;
    private String Identificador;

    
    //Constructor del objeto Country
    public Country(String nombre, String Identificador) {

    this.nombre = nombre;
	this.Identificador = Identificador;
    }
	

    public int compareTo(Country pais) {

        int comp = this.nombre.compareTo(pais.nombre);
	
        
        if(comp !=0){
            return comp;
        }
	
	return 0;
    }
    
    //Getters y Setters.

    public String getIdentificador() {

	return Identificador;
    }

    public void setIdentificador(String setIdentificador) {

	this.Identificador= setIdentificador;
    }
    public String getnombre() {

        return nombre;
        }
    
    public void setnombre(String setnombre) {
    
        this.nombre= setnombre;
}
} //fin comparable country

public static class Author implements Comparable<Author>{

    //Atributos de la clase.
    private String nombre;
    private String Identificador;
    private String Nacimiento;
    private String Pais;

    
    //Constructor del objeto Album.
    public Author(String nombre, String Identificador, String Nacimiento, String Pais) {

    this.nombre = nombre;
	this.Identificador = Identificador;
    this.Nacimiento= Nacimiento;
    this.Pais = Pais;
    }
	


    //
    public int compareTo(Author autor) {

        int comp = this.nombre.compareTo(autor.nombre);
	
        
        if(comp !=0){
            return comp;
        }
	
	return 0;
    }
    
    //Getters y Setters.

    public String getIdentificador() {

	return Identificador;
    }

    public void setIdentificador(String setIdentificador) {

	this.Identificador= setIdentificador;
    }
    public String getnombre() {

        return nombre;
        }
    
    public void setnombre(String setnombre) {
    
        this.nombre= setnombre;
}
public String getNacimiento() {

    return Nacimiento;
    }

public void setNacimiento(String setNacimiento) {

    this.Nacimiento= setNacimiento;
}
public String getPais() {

    return Pais;
    }

public void setPais(String setPais) {

    this.Pais= setPais;
}


} //fin comparable Autor

public static class Book implements Comparable<Book>{

    //Atributos de la clase.
    private String nombre;
    private String Identificador;
    private String ISBN;
    private String Autor;
    private String Disponible;

    
    //Constructor del objeto Book.
    public Book(String nombre, String Identificador, String ISBN, String Autor, String Disponible) {

    this.nombre = nombre;
	this.Identificador = Identificador;
    this.ISBN= ISBN;
    this.Autor = Autor;
    this.Disponible =Disponible;
    }
	


    //
    public int compareTo(Book libro) {

        int comp = this.nombre.compareTo(libro.nombre);
	
        
        if(comp !=0){
            return comp;
        }
	
	return 0;
    }
    
    //Getters y Setters.

    public String getIdentificador() {

	return Identificador;
    }

    public void setIdentificador(String setIdentificador) {

	this.Identificador= setIdentificador;
    }
    public String getnombre() {

    return nombre;
        }
    
    public void setnombre(String setnombre) {
    
        this.nombre= setnombre;
    }
public String getISBN() {

    return ISBN;
    }

public void setISBN(String setISBN) {

    this.ISBN= setISBN;
}
public String getAutor() {

    return Autor;
    }

public void setAutor(String setAutor) {

        this.Autor= setAutor;
    }

public String getDisponible() {

    return Disponible;
    }


public void setDisponible(String setDisponible) {

    this.Disponible= setDisponible;
}


} //fin comparable Book





public static class FrontEnd{

    
    
    public static void fase1(PrintWriter out, ArrayList<Country> countries){

        

            int numpaises = countries.size();
            
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"es\">");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Servicio de consulta de informacion</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servicio de consulta de informaci&oacuten</h1>");
            out.println("<h2>Fase 1</h2>");
            out.println("<h3>Selecciona el pa&iacutes:</h3>");
            out.println("<ol>"); 
            
               
           for(int i=0; i<numpaises; i++){
               
            out.println("<li>");
            out.println("<h4><a href=\"P2LIB?fase=2&pais="+countries.get(i).getnombre()+"\"><ul><li type=\"circle\">"+countries.get(i).getnombre()+"</li></ul></a></h4>");
            out.println("<br>");
            out.println("</li>");
               
            }
            out.println("</ol>");
            
            out.println("</ol>");
            out.println("<h4><a href=\"P2LIB\"><ul><li type=\"circle\">Anterior.</li></ul></a></h4>"); //boton para volver a la fase anterior
            out.println("<hr> </hr>");
            out.println("<h4>Autor: Alejandro Comesa&ntildea Almui&ntildea</h4>");
            out.println("</body>");
            out.println("</html>");
            
       


    }//fin fase1

        public static void fase2(PrintWriter out, String pais, ArrayList<Author> authors){


            int numalbums = authors.size();


            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"es\">");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Servicio de consulta de informacion</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servicio de consulta de informaci&oacuten</h1>"); 
            out.println("<h2>Fase 2</h2>");
            out.println("<h3>Consultando informaci&oacute de pa&iacutes:"+pais+"</h3>");
            out.println("<h3>Seleccione el autor:</h3>");
            out.println("<ol>"); 

            for(int i=0; i<numalbums; i++){
               
                out.println("<li>");
                out.println("<h4><a href=\"P2LIB?fase=3&autor="+authors.get(i).getnombre()+"\"><ul><li type=\"circle\">"+authors.get(i).getnombre()+"</li></ul></a> Nacido en "+authors.get(i).getNacimiento()+" </h4>");
                out.println("<br>");
                out.println("</li>");
                   
                }
                out.println("</ol>");


            out.println("<h4><a href=\"P2LIB?fase=1\"><ul><li type=\"circle\">Anterior.</li></ul></a></h4>"); //boton para volver a la fase anterior
            out.println("<hr> </hr>");
            out.println("<h4>Autor: Alejandro Comesa&ntildea Almui&ntildea</h4>");
            out.println("</body>");
            out.println("</html>");

        }//fin fase 2

//fase3
public static void fase3(PrintWriter out, String pais,String autor, ArrayList<Book> books){

    int numbooks = books.size();

    out.println("<!DOCTYPE html>");
    out.println("<html lang=\"es\">"); 
    out.println("<meta charset=\"UTF-8\">");
    
    out.println("<title>Servicio de consulta de informacion</title>");
    out.println("<body>");
    out.println("<h1>Servicio de consulta de informaci&oacuten</h1>"); 
    out.println("<h2>Fase 3</h2>");
    out.println("<h3>Consultando informaci&oacute de pa&iacutes:"+pais+"</h3>"); 
    out.println("<h3>Consultando informaci&oacute de autor:"+autor+"</h3>"); 
    out.println("<h3>Lista de libros:</h3>");
    out.println("<ol>"); 

    for(int i=0; i<numbooks; i++){
               
                if(books.get(i).getDisponible().equals("no")){
                out.println("<li>");
                out.println("<h4> <font color=red>"+books.get(i).getnombre()+"</font> ISBN: "+books.get(i).getISBN()+"</h4>");
                out.println("<br>");
                out.println("</li>");
                    
            }
            else{
                
                out.println("<li>");
                out.println("<h4> "+books.get(i).getnombre()+" ISBN: "+books.get(i).getISBN()+" </h4>");
                out.println("<br>");
                out.println("</li>");
                
                }
                
                   
                }

    out.println("</ol>");
    out.println("<h4><a href=\"P2LIB?fase=2&pais="+pais+"\"><ul><li type=\"circle\">Anterior.</li></ul></a></h4>"); //boton para volver a la fase anterior
    out.println("<hr> </hr>");
    out.println("<h4>Autor: Alejandro Comesa&ntildea Almui&ntildea</h4>");
    out.println("</body>");
    out.println("</html>");



        }//fin fase 3
    }//fin front end
}//fin del programa
 
