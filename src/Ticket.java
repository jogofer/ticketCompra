import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.*;
import java.text.DecimalFormat;

public class Ticket {

    private ArrayList<Producto> listaProductos = new ArrayList<Producto>();
    private double total;
    private double impuestoTotal;

    @SuppressWarnings("resource")

    public Ticket(String nombreFicheroEntrada){

        //leemos cada linea del fichero
        try {

            Scanner input = new Scanner(System.in);

            File file = new File(nombreFicheroEntrada);

            input = new Scanner(file);

            while (input.hasNextLine()) {

                String linea = input.nextLine(); //escogemos la linea

                String[] palabras = linea.split(" "); //separamos la linea en palabras

                int cantidadProductos = Integer.parseInt(palabras[0]);

                boolean importado = linea.contains("importado");

                //comprobamos si el producto de la linea actual esta exento
                String[] productosExentosImpuestos =  new String[]{"libro","chocolate","bombones","pastillas"};

                String tipoExento = null;

                for (int i =0; i<productosExentosImpuestos.length;i++){
                   if (linea.contains(productosExentosImpuestos[i])){

                       tipoExento=productosExentosImpuestos[i];

                   }
                }

                int separadorNombreProductoPrecio = linea.lastIndexOf("a");


                if(separadorNombreProductoPrecio == -1){

                    System.out.println("Mal formateado");

                } else {

                    //seleccionamos solamente el precio en cada linea, eliminando el espacio del final y el simbolo €

                    float precioProducto = Float.parseFloat((linea.replace(",", ".").substring(separadorNombreProductoPrecio+2,linea.length()-2)));

                    String nombreProducto = linea.substring(1, separadorNombreProductoPrecio);

                    for(int i = 0;i<cantidadProductos;i++){

                        Producto nuevoProducto = null;

                        if(importado){
                            // el producto es importado
                            if(tipoExento != null){
                                //el producto no es importado y está exento de impuestos

                                if(tipoExento == "libro"){
                                    nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.LIBRO_IMPORTADO);
                                } else if(tipoExento == "pastillas"){
                                    nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.MEDICINAS_IMPORTADAS);
                                } else if(tipoExento == "chocolate" || tipoExento == "bombones"){
                                    nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.COMIDA_IMPORTADA);
                                }

                            } else {
                                // el producto es importado y no esta exento de impuestos
                                nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.OTROS_IMPORTADOS);
                            }

                        } else {
                            //el producto no es importado
                            if(tipoExento != null){
                                // el producto no es importado y esta exento de impuestos

                                if(tipoExento == "libro"){
                                    nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.LIBRO);
                                } else if(tipoExento == "pastillas"){
                                    nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.MEDICINAS);
                                } else if(tipoExento == "chocolate" || tipoExento == "bombones"){
                                    nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.COMIDA);
                                }

                            } else {
                                // el producto no es importado y no esta exento de impuestos
                                nuevoProducto = new Producto(nombreProducto,precioProducto, tipoProducto.OTROS);
                            }
                        }

                        listaProductos.add(nuevoProducto);
                    }
                }

            }
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void calcularImpuestos(){


        BigDecimal sumaActual = new BigDecimal("0");
        BigDecimal sumaActualImpuestos = new BigDecimal("0");

        for(int i = 0;i<listaProductos.size();i++){

            sumaActualImpuestos = BigDecimal.valueOf(0);

            BigDecimal totalAntesImpuestos = new BigDecimal(String.valueOf(this.listaProductos.get(i).getPrecio()));

            sumaActual = sumaActual.add(totalAntesImpuestos);

            if(listaProductos.get(i).estaExentoImpuestos()){
                // este producto no está exento de impuestos, se le aplicará el 10% y se redondeara al 0.05 mas cercano

                BigDecimal porcentajeImpuestoBasico = new BigDecimal(".10");
                BigDecimal impuestoImportado = porcentajeImpuestoBasico.multiply(totalAntesImpuestos);

                impuestoImportado = redondear(impuestoImportado, BigDecimal.valueOf(0.05), RoundingMode.UP);
                sumaActualImpuestos = sumaActualImpuestos.add(impuestoImportado);


            }

            if(listaProductos.get(i).esImportado()){
                // el producto es importado, hay que aplicarle el 5% y redondear a la cifra mas cercana 0.05

                BigDecimal porcentajeImpuestoImportado = new BigDecimal(".05");
                BigDecimal impuestoImportado = porcentajeImpuestoImportado.multiply(totalAntesImpuestos);

                impuestoImportado = redondear(impuestoImportado, BigDecimal.valueOf(0.05), RoundingMode.UP);
                sumaActualImpuestos = sumaActualImpuestos.add(impuestoImportado);

            }

            impuestoTotal += sumaActualImpuestos.doubleValue();

            sumaActual = sumaActual.add(sumaActualImpuestos);
        }
        impuestoTotal = redondearDosCifrasDecimales(impuestoTotal);
        total = sumaActual.doubleValue();
    }

    public void setTotal(BigDecimal cantidad){
        total = cantidad.doubleValue();
    }

    public double getTotal(){
        return total;
    }



    public static BigDecimal redondear(BigDecimal valor, BigDecimal impuesto, RoundingMode modoRedondear) {

            BigDecimal dividido = valor.divide(impuesto, 0, modoRedondear);
            BigDecimal resultado = dividido.multiply(impuesto);
            resultado.setScale(2, RoundingMode.UNNECESSARY);
            return resultado;

    }

    public double redondearDosCifrasDecimales(double d) {
       //redondear a dos cifras decimales
        DecimalFormat dosDecimales = new DecimalFormat("#.##");
        return Double.valueOf(dosDecimales.format(d));
    }

    public void imprimirTicket(){

        for(int i = 0;i<listaProductos.size();i++){
            System.out.printf("1" + listaProductos.get(i).getNombre() + "a ");
            System.out.printf("%.2f"+" €\n", listaProductos.get(i).getPrecio());
        }
        System.out.printf("Impuestos sobre las ventas: %.2f"+" €\n", impuestoTotal);
        System.out.println("Total: " + total + " €");
    }

    public void setImpuestoTotal(BigDecimal cantidad){
        impuestoTotal = cantidad.doubleValue();
    }

    public double getImpuestoTotal(){
        return impuestoTotal;
    }

}