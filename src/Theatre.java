import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Theatre {
    //static objects and variables can use in every static methods
    static Scanner input=new Scanner(System.in);
    static int[][] seats =new int[3][]; //2d array
    static double[] price={30.0,20.0,10.0}; //assign price by row number
    static ArrayList<Ticket> tickets=new ArrayList<>();

    static final String regExName="[A-Z][a-z]+( [A-Z][a-z]+)?";
    static final String regExEmail="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static void main(String[] args) {
        boolean running=true;
        int opt;

        seats[0]=new int[12];
        seats[1]=new int[16];
        seats[2]=new int[20];

        while (running){
            System.out.println("Welcome to the New Theatre");
            System.out.println("-".repeat(40));
            System.out.println("Please select an option:\n1) Buy a ticket\n2) Print seating area\n3) Cancel ticket\n4) List available seats\n5) Save to file\n6) Load from file\n7) Print ticket information and total price\n8) Sort tickets by price\n\t0) Quit");
            System.out.println("-".repeat(40));

            opt= get_int("Enter option:","Invalid Choice",0,8);
            switch (opt){
                case 0:
                    running=false;
                    System.out.println("Program Quit");
                    break;
                case 1:
                    buy_tickets();
                    break;
                case 2:
                    print_seating_area();
                    break;
                case 3:
                    cancel_ticket();
                    break;
                case 4:
                    show_available();
                    break;
                case 5:
                    save();
                    break;
                case 6:
                    load();
                    break;
                case 7:
                    show_tickets_info();
                    break;
                case 8:
                    sort_tickets();
                    break;
            }
        }
    }

    // get integer input with range and handle error
    private static int get_int(String inMsg,String invalidMsg,int min,int max){
        while (true){
            System.out.print(inMsg);
            try {
                int val= input.nextInt();
                if(val>=min&&val<=max){
                    return val;
                }else {
                    System.out.println(invalidMsg);
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid Input");
                input.next();
            }
        }
    }

    // get yes no input
    private static char get_yn(String msg){
        while (true){
            System.out.print(msg);
            char yn=input.next().toLowerCase().charAt(0);

            if (yn=='y' || yn=='n'){
                return yn;
            }else {
                System.out.println("Invalid Input");
            }
        }
    }

    private static String get_string(String msg,String errorMsg,String regex){
        //reference https://stackoverflow.com/questions/3263978/java-regex-to-validate-a-name
        while (true){
            System.out.print(msg);
            String val=input.next();

            if(Pattern.matches(regex,val)){
                return val;
            }
            System.out.println(errorMsg);
        }
    }
    private static void buy_tickets(){

        System.out.println("-------Buying Ticket------");

        String name=get_string("Enter Name :","Invalid Name",regExName);
        String surname=get_string("Enter Surname :","Invalid Surname",regExName);
        String email= get_string("Enter Email :","Invalid Email",regExEmail);

        Person person=new Person(name,surname,email);

        int row_num=get_int("Enter row number (1-3): ","Invalid row number",1,3);
        int row=row_num-1;

        int seat_num=get_int("Enter seat number(1-"+ seats[row].length+ "): ","Invalid seat number",1,seats[row].length);

        int seat=seat_num-1;

        if(seats[row][seat]==0){
            System.out.println("Seat available");
            char yn=get_yn("Do you want to book row "+row_num+" seat "+seat_num+"? (y/n)");
            if(yn=='y'){
                seats[row][seat]=1;
                tickets.add(new Ticket(row_num,seat_num,price[row],person));
                System.out.println("Seat received row "+row_num+ " seat "+seat_num+"\n");
            }else {
                System.out.println("Seat not received");
            }

        }else {
            System.out.println("seat not available\n");
        }
    }
    private static void print_seating_area(){
        System.out.println("     ***********");
        System.out.println("     *  STAGE  *");
        System.out.println("     ***********");
        System.out.print("    ");
        for(int i = 0; i< seats.length; i++){
            if(i==1) System.out.print("  ");
            for (int j = 0; j< seats[i].length; j++){
                if (j== seats[i].length/2){
                    System.out.print(" ");
                }
                if(seats[i][j]==0){
                    System.out.print("0");
                }else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
    private static void cancel_ticket() {
        System.out.println("-------Cancel Ticket------\n");

        int row_num=get_int("Enter row number (1-3): ","Invalid row number",1,3);
        int row=row_num-1;


        int seat_num=get_int("Enter seat number(1-"+ seats[row].length+ "): ","Invalid seat number",1,seats[row].length);
        int seat=seat_num-1;

        if(seats[row][seat]==1){

            int index=-1;
            for(int i=0;i< tickets.size();i++){
                if((tickets.get(i).getRow()==row_num)&&(tickets.get(i).getSeat()==seat_num)) {
                    index=i;
                    break;
                }
            }
            tickets.get(index).print();
            char yn=get_yn("Do you want to cancel ? (y/n)");

            if(yn=='y'){
                seats[row][seat]=0;
                tickets.remove(index);
                System.out.println("Seat canceled row "+row_num+" seat "+seat_num+"\n");
            }else {
                System.out.println("Seat not canceled");
            }

        }else {
            System.out.println("Seat not book yet\n");
        }
    }

    private static void show_available(){
        System.out.println("-------Available------\n");
        for(int i = 0; i< seats.length; i++){
            System.out.print("Seats available in row "+(i+1)+": ");
            String separator = "";
            for(int j = 0; j< seats[i].length; j++){
                    if (seats[i][j]==0) {
                        System.out.print(separator + (j + 1));
                        separator=", ";
                    }
            }
            System.out.println();
        }
    }

    private static void save(){
        //Save seat data
        try{
            FileWriter seatWriter=new FileWriter("seats.txt");
            for (int[] row : seats) {
                for (int seat : row) {
                    seatWriter.write(Integer.toString(seat) + ',');
                }
                seatWriter.write("\n");
            }
            System.out.println("Data successfully saved ");
            seatWriter.close();

        }catch (IOException e){
            System.out.println("An error occurred while saving the seat information to file.");
        }

        //save tickets data
        try {
            FileWriter ticketsWriter=new FileWriter("tickets.txt");
            for(Ticket ticket:tickets){
                ticketsWriter.write(ticket.getPerson().getName()+","+
                        ticket.getPerson().getSurname()+","+
                        ticket.getPerson().getEmail()+","+
                        ticket.getRow()+","+
                        ticket.getSeat()+","+
                        ticket.getPrice()+"\n");
            }
            ticketsWriter.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private static void load(){
        String data;
        String[] temp;
        String[] ticketData;

        //loading seats data
        try {
            Scanner scnSeat = new Scanner(new FileReader("seats.txt"));

            for(int i=0; i<3;i++){
                data=scnSeat.nextLine();
                temp=data.split(",");
                for (int j=0;j<temp.length;j++){
                    seats[i][j]=Integer.parseInt(temp[j]);
                }
            }
            scnSeat.close();
            System.out.println("Data load successfully");
        }catch (Exception e){
            System.out.println("Error loading file:");
            System.out.println(e.getMessage());
        }

        //loading tickets data
        try {
            Scanner scnTickets=new Scanner(new FileReader("tickets.txt"));
            while (scnTickets.hasNext()){
                ticketData=scnTickets.nextLine().split(",");

                Person person=new Person(ticketData[0],ticketData[1],ticketData[2]);

                int row=Integer.parseInt(ticketData[3]);
                int seat=Integer.parseInt(ticketData[4]);
                double price=Double.parseDouble(ticketData[5]);

                Ticket ticket=new Ticket(row,seat,price,person);
                tickets.add(ticket);
            }
            scnTickets.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void show_tickets_info(){
        System.out.println("-------Ticket Info-------");
        double total=0;
        for (Ticket ticket : tickets) {
            ticket.print();
            total+=ticket.getPrice();
        }
        System.out.println("Total price : "+total);
    }

    private static void sort_tickets() {
        System.out.println("----Ticket sorted------");
        tickets= MergeSort(tickets, 0, tickets.size() - 1);
        for (Ticket t:tickets) {
            t.print();
        }
    }


    //Reference - Lecture 8 - Sorting and Search Algorithms
    public static ArrayList<Ticket> MergeSort(ArrayList<Ticket> array, int start, int end) {
        ArrayList<Ticket> sorted;
        if (start < end) {
            int mid = (start + end) / 2;
            ArrayList<Ticket> array_left = MergeSort(array, start, mid);
            ArrayList<Ticket> array_right = MergeSort(array, mid + 1, end);
            sorted = merge(array_left, array_right);
        } else if (start == end) {
            sorted = new ArrayList<>();
            sorted.add(array.get(start));
        } else {        // fix empty array error issue
            sorted = new ArrayList<>();
        }
        return sorted;
    }

    public static ArrayList<Ticket> merge(ArrayList<Ticket> array_1, ArrayList<Ticket> array_2) {
        ArrayList<Ticket> merged = new ArrayList<>();
        int idx_1 = 0;
        int idx_2 = 0;
        while (idx_1 < array_1.size() && idx_2 < array_2.size()) {
            if (array_1.get(idx_1).getPrice() <= array_2.get(idx_2).getPrice()) {
                merged.add(array_1.get(idx_1));
                idx_1++;
            } else {
                merged.add(array_2.get(idx_2));
                idx_2++;
            }
        }
        while (idx_1 < array_1.size()) {
            merged.add(array_1.get(idx_1));
            idx_1++;

        }while (idx_2 < array_2.size()) {
            merged.add(array_2.get(idx_2));
            idx_2++;

        }
        return merged;
    }
}