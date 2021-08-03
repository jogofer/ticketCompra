
enum tipoProducto {
    LIBRO(true,false),
    MEDICINAS(true,false),
    COMIDA(true,false),
    OTROS( false , false),
    LIBRO_IMPORTADO(true,true),
    MEDICINAS_IMPORTADAS(true,true),
    COMIDA_IMPORTADA(true,true),
    OTROS_IMPORTADOS(false,true);

    private boolean exento;
    private boolean importado;

    private tipoProducto(boolean exento , boolean importado){
        this.exento = exento;
        this.importado = importado;
    }

    public boolean Importado(){
        return importado;
    }
    public boolean Exento(){
        return exento;
    }

}

public class Producto {
    private String nombre;
    private float precio;
    private tipoProducto tipo;

    public Producto(String nombre, float precio, tipoProducto tipo){
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;

    }

    public String toString(){
        return this.nombre + this.getPrecio();
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean estaExentoImpuestos() {
        return !this.tipo.Exento();
    }

    public boolean esImportado() {
        return this.tipo.Importado();
    }

}