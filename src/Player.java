import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Player {

    private int damage,healthy,money,rHealthy;

    private String name,cName;

    private Inventory inv ;
    public int getTotalDamage(){

        return this.getDamage()+this.getInv().getDamage();

    }


    public Player(String name) {

        this.inv=new Inventory();
        this.name = name;
    }


    Scanner scan = new Scanner(System.in);

    public void selectCharacter(){
                int id =  chaMenu();
                sayNamewithC(id);}







            public void sayNamewithC(int id){

                Map<Integer,String> characterNames = new HashMap<>();
                characterNames.put(1,"Samuray");
                characterNames.put(2,"Okçu");
                characterNames.put(3,"Şövalye");
                setFeatures(id);
                System.out.println(characterNames.get(id)+ " "+  name + " savaşmaya hazır! ");
                System.out.println("==============================");
                System.out.println();

            }
    private  void setFeatures(int id){
                    switch (id){
                        case 1 :
                        setcName("Samuray");
                        setDamage(5);
                        setHealthy(21);
                        setMoney(15);
                        setrHealthy(21);
                        break ;

                        case 2 :
                            setcName("Okçu");
                            setDamage(7);
                            setHealthy(18);
                            setMoney(20);
                            setrHealthy(18);
                            break;
                        case 3 :
                            setcName("Şövalye");
                            setDamage(8);
                            setHealthy(24);
                            setMoney(5);
                            setrHealthy(24);
                            break;


                        default:
                        case 4 :
                            setcName("Samuray");
                            setDamage(5);
                            setHealthy(21);
                            setMoney(15);
                            setrHealthy(21);
                            break;
                    }




            }


    public  int chaMenu(){
        System.out.println("Lütfen karakter seçiniz: ");
        System.out.println("1-Samuray ,     Hasar : 5,     Sağlık : 21,     Para : 15");
        System.out.println("2-Okçu ,        Hasar : 7,     Sağlık : 18,     Para : 20");
        System.out.println("3-Şövalye ,     Hasar : 8,     Sağlık : 24,     Para : 5");
        System.out.println();
        System.out.print("Seçiminiz       -> ");
        int chaID = scan.nextInt();

       while (chaID<1 || chaID>3){


           System.out.println("1-Samuray ,     Hasar : 5,     Sağlık : 21,     Para : 15");
           System.out.println("2-Okçu ,        Hasar : 7,     Sağlık : 18,     Para : 20");
           System.out.println("3-Şövalye ,     Hasar : 8,     Sağlık : 24,     Para : 5");
           System.out.println("Lütfen geçerli bir karakter seçiniz : ");
           chaID = scan.nextInt();

       }

            return chaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealthy() {
        return healthy;
    }

    public void setHealthy(int healthy) {
        this.healthy = healthy;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Inventory getInventory() {
        return inv;
    }

    public void setInventory(Inventory inventory) {
        this.inv = inventory;
    }
    public int getrHealthy() {
        return rHealthy;
    }

    public void setrHealthy(int rHealthy) {
        this.rHealthy = rHealthy;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }
}




