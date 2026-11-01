import java.util.Scanner;

public class Game {



    Player player ;
    Location location;
    Scanner scan= new Scanner(System.in);


    public void login(){

        System.out.println("Macera oyununa hoşgeldiniz ! ");
        System.out.print("Oyuna başlamadan önce isminiz giriniz: ");
        String playerName= scan.nextLine();
        player = new Player(playerName);


        System.out.println("Hoşgeldin "+player.getName());
        player.selectCharacter();

        start();
    }

        public void start(){

            while(true){

                System.out.println("1-Güvenli Ev --> Size ait güvenli bir mekan, düşman yok ! ");
                System.out.println("2-Mağara     --> Karşınıza zombi çıkabilir !");
                System.out.println("3-Orman      --> Karşınıza vampir çıkabilir !");
                System.out.println("4-Nehir      --> Karşınıza ayı çıkabilir !");
                System.out.println("5-Mağaza     --> Silah veya zırh alabilirsiniz !");
                System.out.println();
                System.out.print("Lütfen aksiyon için bir yer seçin: ");
                int selLoc = scan.nextInt();

                while(selLoc<1||selLoc>5){
                    System.out.println("Lütfen geçerli bir yer seçiniz");
                    selLoc=scan.nextInt();


                }

                switch(selLoc){

                    case 1 :
                        location=new SafeHouse(player);
                        break;
                    case 2:
                        location=new Cave(player);
                        break;
                    case 3 :
                        location=new Forest(player);
                        break;
                    case 4:
                        location=new River(player);
                        break;
                    case 5:
                        location = new ToolStore(player);
                        break;
                    default:
                        location=new SafeHouse(player);

                }
                if(location.getClass().getName().equals("SafeHouse")){
                    if(player.getInv().isFirewood()&&player.getInv().isWater()&&player.getInv().isFood()){

                        System.out.println("Tebrikler oyunu kazandınız");
                        break;
                    }

                }

                if(!location.getLocation()){
                    System.out.println("Oyun bitti");
                    break ;

                }

            }
            }
        }





