public class Ticket{

    private int row,seat;
    private double price;
    private Person person;

    public Ticket(int row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public double getPrice() {
        return price;
    }

    public Person getPerson() {
        return person;
    }

    void print(){
        System.out.println("Name\t:"+person.getName());
        System.out.println("Surname\t:"+person.getSurname());
        System.out.println("Email:\t"+person.getEmail());
        System.out.println("Row \t:"+row);
        System.out.println("Seat\t:"+seat);
        System.out.println("Price\t:"+price+"\n");
    }
}
