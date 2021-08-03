
public class Driver {

    public static void main(String[] args) {

        Ticket entrada1 = new Ticket("entrada1.txt");

        entrada1.calcularImpuestos();

        System.out.println("Resultado 1");
        entrada1.imprimirTicket();
        System.out.println();

        Ticket entrada2 = new Ticket("entrada2.txt");

        entrada2.calcularImpuestos();

        System.out.println("Resultado 2");
        entrada2.imprimirTicket();
        System.out.println();

        Ticket entrada3 = new Ticket("entrada3.txt");

        entrada3.calcularImpuestos();

        System.out.println("Resultado 3");
        entrada3.imprimirTicket();

    }

}
